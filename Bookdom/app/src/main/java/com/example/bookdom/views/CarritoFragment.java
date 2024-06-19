package com.example.bookdom.views;

import android.health.connect.datatypes.units.Length;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.bookdom.R;
import com.example.bookdom.databinding.FragmentCarritoBinding;
import com.example.bookdom.models.Libro;
import com.example.bookdom.retrofit.RetrofitService;
import com.example.bookdom.retrofit.apis.ICarritoApi;
import com.example.bookdom.retrofit.apis.IComprasApi;
import com.example.bookdom.retrofit.apis.IUsuarioApi;
import com.example.bookdom.tools.CustomToast;
import com.example.bookdom.tools.usuarios.UsuarioManager;
import com.example.bookdom.tools.libros.LibroCarritoAdapter;
import com.example.bookdom.tools.libros.LibroListener;

import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CarritoFragment extends Fragment implements LibroListener {
    private FragmentCarritoBinding binding;
    private LibroCarritoAdapter adapter;
    private IUsuarioApi usuarioApi;
    private IComprasApi comprasApi;
    private ICarritoApi carritoApi;
    private List<Libro> carrito;
    private TextView txtPrecioTotal;
    public static ImageView imgBasura;
    private Button btnComprar;
    private int operacionesPendientes = 0;
    private final Object lock = new Object();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCarritoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txtPrecioTotal = view.findViewById(R.id.txtPrecioTotal);
        imgBasura = view.findViewById(R.id.imgBasura);
        btnComprar = view.findViewById(R.id.btnComprar);

        inicializarRetrofit();
        establecerCarrito();

        imgBasura.setOnClickListener(v -> showConfirmationDialog());
        btnComprar.setOnClickListener(v -> realizarCompra());
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(getContext())
                .setTitle("Confirmación")
                .setMessage("¿Estás seguro de que deseas eliminar los libros seleccionados?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        eliminarSeleccionados();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void establecerCarrito() {
        usuarioApi.findAllCarritoByUsuarioId(UsuarioManager.getUsuario().getId())
                .enqueue(new Callback<List<Libro>>() {
                    @Override
                    public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                        if (response.isSuccessful()) {
                            carrito = response.body();
                            inicializarRecyclerView();
                            establecerPrecioTotal();
                        } else {
                            Log.e("Error al obtener el carrito", "Response code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Libro>> call, Throwable t) {
                        Log.e("Error al obtener el carrito", t.getMessage());
                    }
                });
    }

    private void establecerPrecioTotal() {
        double sumaPrecios = carrito.stream().mapToDouble(Libro::getPrecio).sum();
        txtPrecioTotal.setText(String.valueOf(sumaPrecios) + "€");
    }

    private void inicializarRecyclerView() {
        if (isAdded()) {
            adapter = new LibroCarritoAdapter(carrito, this);
            binding.recyclerViewLibrosCarrito.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.recyclerViewLibrosCarrito.setAdapter(adapter);
        }
    }

    private void inicializarRetrofit() {
        RetrofitService retrofitService = new RetrofitService();
        usuarioApi = retrofitService.getRetrofit().create(IUsuarioApi.class);
        carritoApi = retrofitService.getRetrofit().create(ICarritoApi.class);
        comprasApi = retrofitService.getRetrofit().create(IComprasApi.class);
    }

    private void realizarCompra() {
        operacionesPendientes = 0;

        for (Libro libro : carrito) {
            synchronized (lock) {
                operacionesPendientes += 2; // Dos operaciones por libro: agregar a compras y eliminar del carrito
            }

            comprasApi.agregarLibroACompras(UsuarioManager.getUsuario().getId(), libro.getId())
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                CustomToast.showToast(getContext(), "Compra realizada con éxito", Toast.LENGTH_SHORT);
                            } else {
                                Log.e("Error en la compra", "Response code: " + response.code());
                            }
                            verificarEstadoFinal();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Error en la compra", t.getMessage());
                            verificarEstadoFinal();
                        }
                    });

            carritoApi.eliminarLibroDelCarrito(UsuarioManager.getUsuario().getId(), libro.getId())
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (!response.isSuccessful()) {
                                Log.e("Error al eliminar del carrito", "Response code: " + response.code());
                            }
                            verificarEstadoFinal();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Log.e("Error al eliminar del carrito", t.getMessage());
                            verificarEstadoFinal();
                        }
                    });
        }
    }

    private synchronized void verificarEstadoFinal() {
        synchronized (lock) {
            operacionesPendientes--;
            if (operacionesPendientes <= 0) {
                carrito.clear();
                adapter.setSelectionMode(false);
                adapter.notifyDataSetChanged();
                establecerPrecioTotal();
            }
        }
    }

    private void eliminarSeleccionados() {
        Set<Integer> selectedItems = adapter.getSelectedItems();
        List<Libro> itemsToRemove = new ArrayList<>();

        for (int position : selectedItems) {
            itemsToRemove.add(carrito.get(position));
        }

        operacionesPendientes = itemsToRemove.size();

        for (Libro libro : itemsToRemove) {
            carritoApi.eliminarLibroDelCarrito(UsuarioManager.getUsuario().getId(), libro.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        synchronized (lock) {
                            carrito.remove(libro);
                            operacionesPendientes--;
                            if (operacionesPendientes == 0) {
                                actualizarCarrito();
                            }
                        }
                    } else {
                        Log.e("Error al eliminar el libro del carrito", "Response code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("Error al eliminar el libro del carrito", t.getMessage());
                    synchronized (lock) {
                        operacionesPendientes--;
                        if (operacionesPendientes == 0) {
                            actualizarCarrito();
                        }
                    }
                }
            });
        }
    }

    private void actualizarCarrito() {
        adapter.setSelectionMode(false);
        adapter.notifyDataSetChanged();
        establecerPrecioTotal();
    }

    @Override
    public void onItemClicked(Libro libro) {
        // Maneja el evento de clic en un ítem si es necesario
    }
}

