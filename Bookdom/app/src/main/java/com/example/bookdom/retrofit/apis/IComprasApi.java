package com.example.bookdom.retrofit.apis;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IComprasApi {
    @POST("api/compras/agregar")
    Call<Void> agregarLibroACompras(@Query("usuarioId") Long usuarioId, @Query("libroId") Long libroId);
}
