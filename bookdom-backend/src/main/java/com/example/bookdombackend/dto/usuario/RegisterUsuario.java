package com.example.bookdombackend.dto.usuario;

import com.example.bookdombackend.model.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUsuario {
    private String nombreUsuario;
    private String email;
    private String password;
}
