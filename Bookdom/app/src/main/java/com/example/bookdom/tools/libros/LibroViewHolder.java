package com.example.bookdom.tools.libros;

import android.health.connect.datatypes.units.Length;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bookdom.R;
import com.example.bookdom.enums.Rol;
import com.example.bookdom.models.ImgLibro;
import com.example.bookdom.models.Libro;
import com.example.bookdom.models.Usuario;
import com.example.bookdom.retrofit.RetrofitService;
import com.example.bookdom.retrofit.apis.IAuthApi;
import com.example.bookdom.retrofit.apis.ILibroApi;
import com.example.bookdom.tools.CustomToast;
import com.example.bookdom.tools.usuarios.UsuarioManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibroViewHolder extends RecyclerView.ViewHolder {
    private TextView txtTituloLibro;
    private TextView txtAutorLibro;
    private TextView txtPrecioLibro;
    private ImageView imgLibro;
    private ImageView imgBasuraLibro;
    private CardView cardView;
    private List<ImgLibro> urlsImagenesLibro;
    private int imagenActualIndex = 0;
    private Handler handler = new Handler();
    private Runnable runnable;
    private Long libroId;

    public LibroViewHolder(@NonNull View itemView) {
        super(itemView);
        txtTituloLibro = itemView.findViewById(R.id.txtTituloLibro);
        txtAutorLibro = itemView.findViewById(R.id.txtAutorLibro);
        txtPrecioLibro = itemView.findViewById(R.id.txtPrecioLibro);
        imgLibro = itemView.findViewById(R.id.imgLibro);
        imgBasuraLibro = itemView.findViewById(R.id.imgBasuraLibro);
        cardView = itemView.findViewById(R.id.cardContainer);

        Rol rolUsuario = UsuarioManager.getUsuario().getRolUsuario();

        if (rolUsuario.equals(Rol.USUARIO)) {
            cardView.setOnLongClickListener(v -> {
                iniciarCambioDeImagenes();
                return true;
            });
        } else {
            cardView.setOnLongClickListener(v -> {
                imgBasuraLibro.setVisibility(View.VISIBLE);
                imgBasuraLibro.setOnClickListener(v1 -> eliminarLibro());
                return true;
            });
        }

        cardView.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                detenerCambioDeImagenes();
            }
            return false;
        });
    }

    public void render(Libro libro) {
        this.libroId = libro.getId();  // Almacenar el ID del libro
        this.urlsImagenesLibro = libro.getImagenesLibro();

        if (urlsImagenesLibro != null && !urlsImagenesLibro.isEmpty()) {
            cargarImagenDesdeUrl(urlsImagenesLibro.get(0));
        }

        txtTituloLibro.setText(libro.getTitulo());
        txtAutorLibro.setText(libro.getAutor());
        txtPrecioLibro.setText(String.valueOf(libro.getPrecio()) + "€");
    }

    private void eliminarLibro() {
        RetrofitService retrofitService = new RetrofitService();
        ILibroApi libroApi = retrofitService.getRetrofit().create(ILibroApi.class);

        libroApi.deleteLibro(libroId)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            CustomToast.showToast(txtPrecioLibro.getContext(), "Libro eliminado correctamente", Toast.LENGTH_SHORT);
                        } else {
                            CustomToast.showToast(txtPrecioLibro.getContext(), "Error al eliminar el libro", Toast.LENGTH_SHORT);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        CustomToast.showToast(txtPrecioLibro.getContext(), "Error al eliminar el libro", Toast.LENGTH_SHORT);
                    }
                });
    }

    private void cargarImagenDesdeUrl(ImgLibro img) {
        Glide.with(itemView.getContext())
                .load(img.getUrl())
                .into(imgLibro);
    }

    private void iniciarCambioDeImagenes() {
        runnable = new Runnable() {
            @Override
            public void run() {
                if (urlsImagenesLibro != null && !urlsImagenesLibro.isEmpty()) {
                    cargarImagenDesdeUrl(urlsImagenesLibro.get(imagenActualIndex));
                    imagenActualIndex = (imagenActualIndex + 1) % urlsImagenesLibro.size();
                    handler.postDelayed(this, 1000);
                }
            }
        };
        handler.post(runnable);
    }

    public CardView getCardView() {
        return cardView;
    }

    private void detenerCambioDeImagenes() {
        handler.removeCallbacks(runnable);
        cargarImagenDesdeUrl(urlsImagenesLibro.get(0));
    }
}

