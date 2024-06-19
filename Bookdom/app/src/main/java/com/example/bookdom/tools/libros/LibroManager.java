package com.example.bookdom.tools.libros;

import com.example.bookdom.models.Libro;

import java.util.List;

public class LibroManager {
    private static List<Libro> libros;

    public static List<Libro> getLibros() {
        return libros;
    }

    public static void setLibros(List<Libro> libros) {
        LibroManager.libros = libros;
    }
}
