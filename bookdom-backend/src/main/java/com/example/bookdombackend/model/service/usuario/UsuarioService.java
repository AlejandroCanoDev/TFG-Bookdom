package com.example.bookdombackend.model.service.usuario;

import com.example.bookdombackend.dto.libro.LibroListDTO;
import com.example.bookdombackend.exceptions.Usuario.UsuarioDeleteException;
import com.example.bookdombackend.exceptions.Usuario.UsuarioNotFoundException;
import com.example.bookdombackend.exceptions.Usuario.UsuarioUpdateException;
import com.example.bookdombackend.model.entity.Libro;
import com.example.bookdombackend.model.entity.Usuario;
import com.example.bookdombackend.model.repository.ILibroRepository;
import com.example.bookdombackend.model.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
public class UsuarioService implements IUsuarioService {
    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private ILibroRepository libroRepository;

    @Override
    public List<Usuario> findAll() {
        return (List<Usuario>) usuarioRepository.findAll();
    }

    @Override
    public Usuario findById(long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNotFoundException("No se encontro el usuario con id " + id));
    }

    @Override
    public Usuario addUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario modifyUsuario(long id, Usuario newUsuario) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioUpdateException("No se encontro el usuario con id " + id));
        newUsuario.setId(usuario.getId());
        return usuarioRepository.save(newUsuario);
    }

    @Override
    public void deleteUsuario(long id) {
        try {
            usuarioRepository.deleteById(id);
        } catch (Exception ex) {
            throw new UsuarioDeleteException("No se pudo eliminar el usuario con id " + id);
        }
    }

    @Override
    public Usuario findByNombreUsuario(String username) {
        return usuarioRepository.findByNombreUsuario(username);
    }

    @Override
    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public List<String> findAllUsernames() {
        return usuarioRepository.findAllUsernames();
    }

    @Override
    public List<String> findAllEmails() {
        return usuarioRepository.findAllEmails();
    }

    @Override
    public List<Libro> findAllComprasByUsuarioId(@PathVariable long id) {
        return usuarioRepository.findAllComprasByUsuarioId(id);
    }

    @Override
    public List<Libro> findAllCarritoByUsuarioId(@PathVariable long id) {
        return usuarioRepository.findAllCarritoByUsuarioId(id);
    }

    @Override
    public List<Libro> findAllVentasByUsuarioId(@PathVariable long id) {
        return usuarioRepository.findAllVentasByUsuarioId(id);
    }

    @Override
    public Usuario saveUsuarioWithLibro(long id, Libro libro) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("No se encontro el usuario con id " + id));

        usuario.getVentas().add(libro);

        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario saveUsuarioWithImage(long id, String url) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("No se encontro el usuario con id " + id));
        usuario.setFotoPerfil(url);

        return usuarioRepository.save(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public void agregarLibroACompras(Long usuarioId, Long libroId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new UsuarioNotFoundException("No se encontro el usuario con id " + usuarioId));
        Libro libro = libroRepository.findById(libroId).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        usuario.getCompras().add(libro);
        usuarioRepository.save(usuario);
    }

    public void agregarLibroAlCarrito(Long usuarioId, Long libroId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new UsuarioNotFoundException("No se encontro el usuario con id " + usuarioId));
        Libro libro = libroRepository.findById(libroId).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        usuario.getCarrito().add(libro);
        usuarioRepository.save(usuario);
    }

    public void eliminarLibroDelCarrito(Long usuarioId, Long libroId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElseThrow(() -> new UsuarioNotFoundException("No se encontro el usuario con id " + usuarioId));
        Libro libro = libroRepository.findById(libroId).orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        usuario.getCarrito().remove(libro);
        usuarioRepository.save(usuario);
    }
}
