package com.example.bookdom.dto.usuario;

public class LoginUsuario {
    private String nombreUsuario;
    private String password;

    public LoginUsuario(String nombreUsuario, String password) {
        this.nombreUsuario = nombreUsuario;
        this.password = password;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getPassword() {
        return password;
    }
}
