package com.example.bookdom.retrofit.apis;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ICarritoApi {
        @POST("api/carrito/agregar")
        Call<Void> agregarLibroAlCarrito(@Query("usuarioId") Long usuarioId, @Query("libroId") Long libroId);

        @DELETE("api/carrito/eliminar")
        Call<Void> eliminarLibroDelCarrito(@Query("usuarioId") Long usuarioId, @Query("libroId") Long libroId);
}
