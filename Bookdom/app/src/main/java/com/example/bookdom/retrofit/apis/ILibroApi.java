package com.example.bookdom.retrofit.apis;

import com.example.bookdom.enums.CondicionLibro;
import com.example.bookdom.enums.Genero;
import com.example.bookdom.models.Libro;
import com.example.bookdom.models.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ILibroApi {
    @GET("api/libros")
    Call<List<Libro>> findAll();

    @GET("api/librosById/{id}")
    Call<Libro> findById(@Path("id") long id);

    @GET("api/librosByTituloOrAutor/{text}")
    Call<List<Libro>> findByTituloOrAutor(@Path("text") String text);

    @GET("api/librosByCondicion/{condicion}")
    Call<List<Libro>> findByCondicion(@Path("condicion") CondicionLibro condicion);

    @GET("api/librosByGenero/{genero}")
    Call<List<Libro>> findByGenero(@Path("genero") Genero genero);

    @GET("api/librosNormal")
    Call<List<Libro>> listarLibrosNormal();

    @GET("api/librosInverso")
    Call<List<Libro>> listarLibrosInversa();

    @GET("api/librosMasVendidos")
    Call<List<Libro>> listarMasVendidos();

    @GET("api/librosPorPrecioDesc")
    Call<List<Libro>> listarPorPrecioDescendente();

    @GET("api/librosPorPrecioAsc")
    Call<List<Libro>> listarPorPrecioAscendente();

    @POST("api/libros")
    Call<Libro> addLibros(@Body Libro libro);

    @DELETE("api/libros/{id}")
    Call<Void> deleteLibro(@Path("id") long id);

    @PUT("api/libros/{id}")
    Call<Libro> modifyLibro(@Path("id") long id, @Body Libro libro);

    @PUT("api/librosAddComprador/{id}")
    Call<Libro> modifyLibro(@Path("id") long id, @Body Usuario usuario);
}
