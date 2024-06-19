package com.example.bookdombackend.dto.libro;

import com.example.bookdombackend.model.entity.CondicionLibro;
import com.example.bookdombackend.model.entity.Genero;
import com.example.bookdombackend.model.entity.Libro;
import com.example.bookdombackend.model.entity.ImgLibro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroListDTO {
    private Long id;
    private String titulo;
    private String autor;
    private Double precio;
    private List<ImgLibro> imagenesLibro;

    public LibroListDTO(Libro libro) {
        this.id = libro.getId();
        this.titulo = libro.getTitulo();
        this.autor = libro.getAutor();
        this.precio = libro.getPrecio();
        this.imagenesLibro = libro.getImagenesLibro();
    }
}
