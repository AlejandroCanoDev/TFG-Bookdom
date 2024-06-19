package com.example.bookdombackend.model.service.libro;

import com.example.bookdombackend.model.entity.CondicionLibro;
import com.example.bookdombackend.model.entity.Genero;
import com.example.bookdombackend.model.entity.Libro;

import java.util.List;

public interface ILibroService {
    List<Libro> findAll();
    Libro findById(long id);
    Libro addLibro(Libro libro);
    Libro modifyLibro(long id, Libro newLibro);
    void deleteLibro(long id) throws Exception;
    List<Libro> findByTituloOrAutor(String text);
    List<Libro> findByGenero(Genero genero);
    List<Libro> findByCondicion(CondicionLibro condicion);
    List<Libro> findAllOrderByPrecioDesc();
    List<Libro> findAllOrderByPrecioAsc();
}
