package com.example.bookdom.retrofit.apis;

import com.example.bookdom.dto.usuario.LoginResponse;
import com.example.bookdom.dto.usuario.LoginUsuario;
import com.example.bookdom.dto.usuario.RegisterUsuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAuthApi {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginUsuario usuario);
    @POST("auth/register")
    Call<LoginResponse> register(@Body RegisterUsuario registerUsuario);
}
