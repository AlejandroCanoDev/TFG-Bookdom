package com.example.bookdom.dto.usuario;

import com.example.bookdom.enums.Rol;

public class RegisterUsuario {
    private String nombreUsuario;
    private String email;
    private String password;
    private Rol rolUsuario;

    public RegisterUsuario(String nombreUsuario, String email, String password, Rol rolUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.rolUsuario = rolUsuario;
    }
}
