package com.example.bookdombackend.model.service.authentication;

import com.example.bookdombackend.dto.usuario.UsuarioDTO;
import com.example.bookdombackend.model.entity.Usuario;

public interface IAuthenticationService {
    Usuario signup(Usuario newUser);
    UsuarioDTO authenticate(Usuario user);
}
