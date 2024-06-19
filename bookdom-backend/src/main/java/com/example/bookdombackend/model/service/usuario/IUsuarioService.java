package com.example.bookdombackend.model.service.usuario;

import com.example.bookdombackend.dto.libro.LibroListDTO;
import com.example.bookdombackend.dto.usuario.UsuarioDTO;
import com.example.bookdombackend.model.entity.Libro;
import com.example.bookdombackend.model.entity.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface IUsuarioService {
    List<Usuario> findAll();
    Usuario findById(long id);
    Usuario addUsuario(Usuario usuario);
    Usuario modifyUsuario(long id, Usuario newUsuario);
    void deleteUsuario(long id);
    Usuario findByNombreUsuario(String username);
    Usuario findByEmail(String email);
    List<String> findAllUsernames();
    List<String> findAllEmails();
    List<Libro> findAllComprasByUsuarioId(@PathVariable long id);
    List<Libro> findAllCarritoByUsuarioId(@PathVariable long id);
    List<Libro> findAllVentasByUsuarioId(long id);
    Usuario saveUsuarioWithLibro(long id, Libro libro);
    Usuario saveUsuarioWithImage(long id, String url);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
