package com.example.bookdombackend.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginUsuario {
    private String nombreUsuario;
    private String password;
}
