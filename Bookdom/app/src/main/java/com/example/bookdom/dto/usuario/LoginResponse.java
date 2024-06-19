package com.example.bookdom.dto.usuario;

import com.example.bookdom.models.Usuario;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("user")
    private Usuario usuario;

    @SerializedName("token")
    private String token;

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
