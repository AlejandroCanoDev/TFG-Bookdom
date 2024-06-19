package com.example.bookdom.retrofit.apis;

import com.example.bookdom.models.Libro;
import com.example.bookdom.models.Usuario;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IUsuarioApi {
    @GET("api/usuarios")
    Call<List<Usuario>> findAll();

    @GET("api/usuarioByNombreUsuario/{username}")
    Call<Usuario> findByNombreUsuario(@Path("username") String username);

    @GET("api/usuarioByEmail/{email}")
    Call<Usuario> findByEmail(@Path("email") String email);

    @GET("api/usernames")
    Call<List<String>> findAllUsernames();

    @GET("api/emails")
    Call<List<String>> findAllEmails();

    @POST("api/usuarios")
    Call<Usuario> addUsuario(@Body Usuario usuario);

    @PUT("api/usuarios/{id}")
    Call<Usuario> saveUsuario(@Path("id") long id, @Body Usuario usuario);
    @PUT("api/saveUsuarioWithImage/{id}")
    Call<Usuario> saveUsuarioWithImage(@Path("id") long id, @Body Map<String, String> urlMap);

    @GET("api/usuarios/{id}")
    Call<Usuario> findById(@Path("id") long id);

    @GET("api/compras/{id}")
    Call<List<Libro>> findAllComprasByUsuarioId(@Path("id") long id);

    @GET("api/carrito/{id}")
    Call<List<Libro>> findAllCarritoByUsuarioId(@Path("id") long id);

    @GET("api/ventas/{id}")
    Call<List<Libro>> findAllVentasByUsuarioId(@Path("id") long id);
}