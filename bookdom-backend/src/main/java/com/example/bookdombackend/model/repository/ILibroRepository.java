package com.example.bookdombackend.model.repository;

import com.example.bookdombackend.model.entity.CondicionLibro;
import com.example.bookdombackend.model.entity.Genero;
import com.example.bookdombackend.model.entity.Libro;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Hidden
public interface ILibroRepository extends CrudRepository<Libro, Long> {

    @Query("SELECT l FROM Libro l WHERE l.titulo LIKE %:text% OR l.autor LIKE %:text%")
    List<Libro> findByTituloOrAutor(String text);

    @Query("SELECT l FROM Libro l WHERE l.genero = :genero")
    List<Libro> findByGenero(Genero genero);

    @Query("SELECT l FROM Libro l WHERE l.condicion = :condicion")
    List<Libro> findByCondicion(CondicionLibro condicion);

    @Query("SELECT l FROM Libro l ORDER BY l.precio DESC")
    List<Libro> findAllOrderByPrecioDesc();

    @Query("SELECT l FROM Libro l ORDER BY l.precio ASC")
    List<Libro> findAllOrderByPrecioAsc();
}
