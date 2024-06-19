package com.example.bookdombackend.model.repository;

import com.example.bookdombackend.model.entity.Libro;
import com.example.bookdombackend.model.entity.Usuario;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Hidden
public interface IUsuarioRepository extends CrudRepository<Usuario, Long> {
    Usuario findByNombreUsuario(String username);
    Usuario findByEmail(String email);

    @Query("SELECT u.nombreUsuario FROM Usuario u")
    List<String> findAllUsernames();
    @Query("SELECT u.email FROM Usuario u")
    List<String> findAllEmails();

    @Query("SELECT c FROM Usuario u JOIN u.compras c WHERE u.id = :id")
    List<Libro> findAllComprasByUsuarioId(long id);

    @Query("SELECT c FROM Usuario u JOIN u.carrito c WHERE u.id = :id")
    List<Libro> findAllCarritoByUsuarioId(long id);

    @Query("SELECT c FROM Usuario u JOIN u.ventas c WHERE u.id = :id")
    List<Libro> findAllVentasByUsuarioId(long id);
}

