package com.example.bookdombackend.mappers;

import com.example.bookdombackend.dto.usuario.LoginUsuario;
import com.example.bookdombackend.dto.usuario.RegisterUsuario;
import com.example.bookdombackend.dto.usuario.UsuarioDTO;
import com.example.bookdombackend.dto.usuario.UsuarioLibroDTO;
import com.example.bookdombackend.model.entity.Usuario;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UsuarioMapper {
    @Autowired
    ModelMapper modelMapper;

    public Usuario fromRegisterUsuario(RegisterUsuario registerUsuario) {
        return modelMapper.map(registerUsuario, Usuario.class);
    }

    public Usuario fromLoginUsuario(LoginUsuario loginUsuario) {
        return modelMapper.map(loginUsuario, Usuario.class);
    }

    public UsuarioLibroDTO toUsuarioLibroDTO(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioLibroDTO.class);
    }
    public UsuarioDTO toUsuarioDTO(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    public Usuario fromDTO(UsuarioLibroDTO usuarioDTO) {
        return modelMapper.map(usuarioDTO, Usuario.class);
    }
}
