package com.example.bookdombackend.dto.usuario;

import com.example.bookdombackend.dto.libro.LibroListDTO;
import com.example.bookdombackend.mappers.LibroMapper;
import com.example.bookdombackend.model.entity.Rol;
import com.example.bookdombackend.model.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long id;

    private String nombreUsuario;

    private String email;

    private String password;

    private String fotoPerfil;

    private Rol rolUsuario;

    private List<LibroListDTO> ventas;

    private List<LibroListDTO> compras;

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombreUsuario = usuario.getNombreUsuario();
        this.email = usuario.getEmail();
        this.password = usuario.getPassword();
        this.fotoPerfil = usuario.getFotoPerfil();
        rolUsuario = usuario.getRolUsuario();
        LibroMapper libroMapper = new LibroMapper(new ModelMapper());
        this.ventas = libroMapper.mapToDTOList(usuario.getVentas());
        this.compras = libroMapper.mapToDTOList(usuario.getCompras());
    }
}
