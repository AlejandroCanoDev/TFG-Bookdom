package com.example.bookdom.views;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bookdom.R;
import com.example.bookdom.databinding.FragmentComprasBinding;
import com.example.bookdom.databinding.FragmentVentasBinding;
import com.example.bookdom.models.Libro;
import com.example.bookdom.retrofit.RetrofitService;
import com.example.bookdom.retrofit.apis.IUsuarioApi;
import com.example.bookdom.tools.libros.LibroCarritoAdapter;
import com.example.bookdom.tools.libros.LibroListener;
import com.example.bookdom.tools.usuarios.UsuarioManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VentasFragment extends Fragment implements LibroListener {

    private FragmentVentasBinding binding;
    private LibroCarritoAdapter adapter;
    private IUsuarioApi usuarioApi;
    private List<Libro> ventas;

    public VentasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentVentasBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inicializarRetrofit();
        establecerCompras();
    }

    private void establecerCompras() {
        usuarioApi.findAllVentasByUsuarioId(UsuarioManager.getUsuario().getId())
                .enqueue(new Callback<List<Libro>>() {
                    @Override
                    public void onResponse(Call<List<Libro>> call, Response<List<Libro>> response) {
                        if (response.isSuccessful()) {
                            ventas = response.body();
                            inicializarRecyclerView();
                        } else {
                            Log.e("Error al obtener las ventas", "Response code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Libro>> call, Throwable t) {
                        Log.e("Error al obtener las ventas", t.getMessage());
                    }
                });
    }

    private void inicializarRecyclerView() {
        if (isAdded()) {
            adapter = new LibroCarritoAdapter(ventas, this);
            binding.recyclerViewVentas.setLayoutManager(new LinearLayoutManager(requireContext()));
            binding.recyclerViewVentas.setAdapter(adapter);
        }
    }

    private void inicializarRetrofit() {
        RetrofitService retrofitService = new RetrofitService();
        usuarioApi = retrofitService.getRetrofit().create(IUsuarioApi.class);
    }

    @Override
    public void onItemClicked(Libro libro) {

    }
}