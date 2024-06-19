package com.example.bookdom.dto.usuario;

import com.example.bookdom.enums.Rol;
import com.example.bookdom.models.Usuario;

public class UsuarioLibroDTO {
    private Long id;
    private String nombreUsuario;
    private String email;
    private Rol rolUsuario;
    private String fotoPerfil;

    public UsuarioLibroDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.email = usuario.getEmail();
        this.rolUsuario = usuario.getRolUsuario();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Rol getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(Rol rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
}
