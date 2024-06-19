package com.example.bookdom.tools.usuarios;

import com.example.bookdom.models.Usuario;

public class UsuarioManager {
    private static Usuario usuario;

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        UsuarioManager.usuario = usuario;
    }
}
