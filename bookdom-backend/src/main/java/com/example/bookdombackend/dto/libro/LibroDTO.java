package com.example.bookdombackend.dto.libro;

import com.example.bookdombackend.dto.usuario.UsuarioLibroDTO;
import com.example.bookdombackend.mappers.UsuarioMapper;
import com.example.bookdombackend.model.entity.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroDTO {
    private Long id;

    private String titulo;

    private String autor;

    private Double precio;

    private CondicionLibro condicion;

    private Genero genero;

    private String descripcion;

    private Boolean vendido;

    private Boolean reservado;

    private List<ImgLibro> imagenesLibro;

    private UsuarioLibroDTO vendedor;

    public LibroDTO(Libro libro) {
        this.id = libro.getId();
        this.titulo = libro.getTitulo();
        this.autor = libro.getAutor();
        this.precio = libro.getPrecio();
        this.condicion = libro.getCondicion();
        this.genero = libro.getGenero();
        this.descripcion = libro.getDescripcion();
        this.vendido = libro.getVendido();
        this.reservado = libro.getReservado();
        this.imagenesLibro = libro.getImagenesLibro();
        UsuarioMapper usuarioMapper = new UsuarioMapper(new ModelMapper());
        this.vendedor = usuarioMapper.toUsuarioLibroDTO(libro.getVendedor());

    }

}
