package com.example.bookdom.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookdom.R;
import com.example.bookdom.databinding.ActivityPerfilUsuarioBinding;
import com.example.bookdom.models.Libro;
import com.example.bookdom.models.Usuario;
import com.example.bookdom.retrofit.RetrofitService;
import com.example.bookdom.retrofit.apis.IUsuarioApi;
import com.example.bookdom.tools.ErrorManager;
import com.example.bookdom.tools.libros.LibroCarritoAdapter;
import com.example.bookdom.tools.libros.LibroListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilUsuarioActivity extends AppCompatActivity implements LibroListener {
    private ActivityPerfilUsuarioBinding binding;
    private IUsuarioApi usuarioApi;
    private Usuario usuarioSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPerfilUsuarioBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inicializarRetrofit();

        establecerDatosUsuario();
    }

    private void establecerDatosUsuario() {
        ImageView imgUsuario = findViewById(R.id.imgUsuarioPerfil);
        TextView txtUsernamePerfil = findViewById(R.id.txtUsernamePerfil);

        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("usuarioId")) {
            Long usuarioId = (Long) intent.getSerializableExtra("usuarioId");
            usuarioApi.findById(usuarioId)
                    .enqueue(new Callback<Usuario>() {
                        @Override
                        public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                            if (response.isSuccessful()) {
                                usuarioSeleccionado = response.body();

                                if (usuarioSeleccionado.getFotoPerfil() != null) {
                                    String imageUrl = usuarioSeleccionado.getFotoPerfil();
                                    Glide.with(getApplicationContext())
                                            .load(imageUrl)
                                            .into(imgUsuario);
                                }

                                txtUsernamePerfil.setText(usuarioSeleccionado.getNombreUsuario());

                                inicializarRecyclerView();
                            }
                        }

                        @Override
                        public void onFailure(Call<Usuario> call, Throwable t) {
                            ErrorManager.mensajeDeError("Error al cargar el usuarios", t);
                        }
                    });
        }
    }

    private void inicializarRecyclerView() {
        binding.recyclerViewPerfilUsuario.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerViewPerfilUsuario.setAdapter(new LibroCarritoAdapter(usuarioSeleccionado.getVentas(), this));
    }

    private void inicializarRetrofit() {
        RetrofitService retrofitService = new RetrofitService();
        usuarioApi = retrofitService.getRetrofit().create(IUsuarioApi.class);
    }

    @Override
    public void onItemClicked(Libro libro) {

    }
}