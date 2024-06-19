package com.example.bookdombackend.dto.libro;

import com.example.bookdombackend.model.entity.ImgLibro;
import com.example.bookdombackend.model.entity.Libro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroCarritoDTO {
    private Long id;
    private String titulo;
    private String autor;
    private Double precio;
    private List<ImgLibro> imagenesLibro;

    public LibroCarritoDTO(Libro libro) {
        this.id = libro.getId();
        this.titulo = libro.getTitulo();
        this.autor = libro.getAutor();
        this.precio = libro.getPrecio();
        this.imagenesLibro = new ArrayList<>();
        if (libro.getImagenesLibro() != null && !libro.getImagenesLibro().isEmpty()) {
            this.imagenesLibro.add(libro.getImagenesLibro().get(0));
    }
}
}
