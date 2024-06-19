package com.example.bookdombackend.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioLibroDTO {
    private Long id;

    private String nombreUsuario;

    private String email;

    private String fotoPerfil;
}
