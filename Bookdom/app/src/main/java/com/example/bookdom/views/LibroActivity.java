package com.example.bookdom.views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.bookdom.R;
import com.example.bookdom.dto.usuario.UsuarioLibroDTO;
import com.example.bookdom.models.ImgLibro;
import com.example.bookdom.models.Libro;
import com.example.bookdom.retrofit.RetrofitService;
import com.example.bookdom.retrofit.apis.ICarritoApi;
import com.example.bookdom.retrofit.apis.IComprasApi;
import com.example.bookdom.retrofit.apis.ILibroApi;
import com.example.bookdom.tools.CustomToast;
import com.example.bookdom.tools.EmailSender;
import com.example.bookdom.tools.ErrorManager;
import com.example.bookdom.tools.ImagenPagerAdapter;
import com.example.bookdom.tools.usuarios.UsuarioManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LibroActivity extends AppCompatActivity {
    private ICarritoApi carritoApi;
    private ILibroApi libroApi;
    private ProgressBar progressBar;
    private Libro libroSeleccionado;
    private List<Bitmap> imagenes;
    private UsuarioLibroDTO usuarioSend;
    private ImagenPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro);

        Button btnAddCarrito = findViewById(R.id.btnComprar);
        CardView usuarioCardView = findViewById(R.id.usuarioCardView);
        progressBar = findViewById(R.id.progressBarLibro);

        imagenes = new ArrayList<>();
        adapter = new ImagenPagerAdapter(this, imagenes);

        inicializarRetrofit();
        configurarViewPager();
        establecerDatosLibro();

        btnAddCarrito.setOnClickListener(v -> {
            if (UsuarioManager.getUsuario().getId() != libroSeleccionado.getVendedor().getId()) {
                agregarLibroAlCarrito();
            } else {
                CustomToast.showToast(getApplicationContext(), "No puedes comprar tu propio libro", Toast.LENGTH_SHORT);
            }
        });

        usuarioCardView.setOnClickListener(v -> {
            manejarClickUsuario();
        });
    }

    private void manejarClickUsuario() {
        Intent intent = new Intent(LibroActivity.this, PerfilUsuarioActivity.class);
        intent.putExtra("usuarioId", usuarioSend.getId());
        startActivity(intent);
    }

    private void agregarLibroAlCarrito() {
        progressBar.setVisibility(View.VISIBLE);

        Call<Void> call = carritoApi.agregarLibroAlCarrito(UsuarioManager.getUsuario().getId(), libroSeleccionado.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    CustomToast.showToast(getApplicationContext(), "Se ha añadido el libro al carrito", Toast.LENGTH_SHORT);
                    new EmailSender(usuarioSend.getEmail(), "Han añadido tu producto a un carrito", UsuarioManager.getUsuario().getNombreUsuario() + " ha añadido " + libroSeleccionado.getTitulo() + " a su carrito.").execute();
                } else {
                    CustomToast.showToast(getApplicationContext(), "No se pudo añadir el libro al carrito", Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                ErrorManager.mensajeDeError("Error al añadir el libro al carrito", t);
            }
        });
    }

    private void configurarViewPager() {
        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
    }

    public void mostrarPopupGaleriaImagenes(List<Bitmap> imagenes) {
        if (imagenes.isEmpty()) {
            Toast.makeText(this, "No hay imágenes para mostrar", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.TopDialog);
        View popupView = getLayoutInflater().inflate(R.layout.popup_galeria_imagenes, null);

        ViewPager viewPagerPopup = popupView.findViewById(R.id.viewPagerPopup);
        ImagenPagerAdapter adapterPopup = new ImagenPagerAdapter(this, imagenes);
        viewPagerPopup.setAdapter(adapterPopup);

        builder.setView(popupView);
        AlertDialog dialog = builder.create();

        dialog.show();

        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.TOP;
            layoutParams.y = 50; // Ajusta este valor según sea necesario para obtener el margen superior deseado
            window.setAttributes(layoutParams);
        }
    }

    private void establecerUsuario(UsuarioLibroDTO usuario) {
        usuarioSend = usuario;
        ImageView imgUsuarioLibro = findViewById(R.id.imgUsuarioLibro);
        TextView usernameLibro = findViewById(R.id.usernameLibro);

        if (usuario.getFotoPerfil() != null) {
            String imageUrl = usuario.getFotoPerfil();
            Glide.with(getApplicationContext())
                    .load(imageUrl)
                    .into(imgUsuarioLibro);
        }

        usernameLibro.setText(usuario.getNombreUsuario());
    }

    private void establecerDatosLibro() {
        TextView txtPrecio = findViewById(R.id.txtPrecio);
        TextView txtTitulo = findViewById(R.id.txtTitulo);
        TextView txtAutor = findViewById(R.id.txtAutor);
        TextView txtCondicion = findViewById(R.id.txtCondicion);
        TextView txtGenero = findViewById(R.id.txtGenero);
        TextView txtDescripcion = findViewById(R.id.txtDescripcion);

        progressBar.setVisibility(View.VISIBLE);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("libroId")) {
            Long libroId = (Long) intent.getSerializableExtra("libroId");
            libroApi.findById(libroId).enqueue(new Callback<Libro>() {
                @Override
                public void onResponse(Call<Libro> call, Response<Libro> response) {
                    if (response.isSuccessful()) {
                        libroSeleccionado = response.body();
                        String precio = libroSeleccionado.getPrecio() + "€";

                        cargarImagenesDesdeFirebase(libroSeleccionado.getImagenesLibro());

                        txtPrecio.setText(precio);
                        txtTitulo.setText(libroSeleccionado.getTitulo());
                        txtAutor.setText(libroSeleccionado.getAutor());
                        txtCondicion.setText(libroSeleccionado.getCondicion().name());
                        txtGenero.setText(libroSeleccionado.getGenero().name());
                        txtDescripcion.setText(libroSeleccionado.getDescripcion());

                        establecerUsuario(libroSeleccionado.getVendedor());

                        progressBar.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(Call<Libro> call, Throwable t) {
                    ErrorManager.mensajeDeError("Error al cargar el libro", t);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private void cargarImagenesDesdeFirebase(List<ImgLibro> imagenesLibro) {
        imagenes.clear();

        for (ImgLibro imgLibro : imagenesLibro) {
            String imageUrl = imgLibro.getUrl();

            Glide.with(getApplicationContext()).asBitmap().load(imageUrl).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    imagenes.add(resource);
                    adapter.updateImages(imagenes);
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                }
            });
        }
    }

    private void inicializarRetrofit() {
        RetrofitService retrofitService = new RetrofitService();
        libroApi = retrofitService.getRetrofit().create(ILibroApi.class);
        carritoApi = retrofitService.getRetrofit().create(ICarritoApi.class);
    }
}
