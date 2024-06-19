package com.example.bookdombackend.controllers;

import com.example.bookdombackend.dto.libro.LibroDTO;
import com.example.bookdombackend.dto.usuario.UsuarioDTO;
import com.example.bookdombackend.mappers.UsuarioMapper;
import com.example.bookdombackend.model.entity.Libro;
import com.example.bookdombackend.model.entity.Usuario;
import com.example.bookdombackend.model.service.usuario.IUsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
@Tag(name = "Usuarios", description = "Operaciones relacionadas con los usuarios")
public class UsuarioController {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @Autowired
    IUsuarioService usuarioService;

    @Autowired
    UsuarioMapper usuarioMapper;

    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios")
    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Usuario>> findAllUsuarios() {
        logger.info("Inicio de findAllUsuarios");
        List<Usuario> usuarios = usuarioService.findAll();
        logger.info("Fin de findAllUsuarios, {} usuarios encontrados", usuarios.size());
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @Operation(summary = "Buscar usuario por ID", description = "Devuelve un usuario que coincide con el ID proporcionado")
    @GetMapping("/usuarios/{id}")
    @PreAuthorize("hasRole('USUARIO')")
    public ResponseEntity<UsuarioDTO> findUsuarioById(
            @Parameter(description = "ID del usuario a buscar", example = "1") @PathVariable long id) {
        logger.info("Inicio de findUsuarioById con ID: {}", id);
        Usuario usuario = usuarioService.findById(id);
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
        logger.info("Fin de findUsuarioById");
        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @Operation(summary = "Buscar usuario por nombre de usuario", description = "Devuelve un usuario que coincide con el nombre de usuario proporcionado")
    @GetMapping("/usuarioByNombreUsuario/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> findUsuarioByNombre(
            @Parameter(description = "Nombre de usuario a buscar", example = "johndoe") @PathVariable String username) {
        logger.info("Inicio de findUsuarioByNombre con nombre de usuario: {}", username);
        Usuario usuario = usuarioService.findByNombreUsuario(username);
        UsuarioDTO usuarioDTO = new UsuarioDTO(usuario);
        logger.info("Fin de findUsuarioByNombre");
        return new ResponseEntity<>(usuarioDTO, HttpStatus.OK);
    }

    @Operation(summary = "Buscar usuario por correo electrónico", description = "Devuelve un usuario que coincide con el correo electrónico proporcionado")
    @GetMapping("/usuarioByEmail/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> findUsuarioByEmail(
            @Parameter(description = "Correo electrónico a buscar", example = "example@example.com") @PathVariable String email) {
        logger.info("Inicio de findUsuarioByEmail con correo electrónico: {}", email);
        Usuario usuario = usuarioService.findByEmail(email);
        logger.info("Fin de findUsuarioByEmail");
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }

    @Operation(summary = "Obtener todos los nombres de usuario", description = "Devuelve una lista de todos los nombres de usuario")
    @GetMapping("/usernames")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> findAllUsernames() {
        logger.info("Inicio de findAllUsernames");
        List<String> usernames = usuarioService.findAllUsernames();
        logger.info("Fin de findAllUsernames, {} nombres de usuario encontrados", usernames.size());
        return new ResponseEntity<>(usernames, HttpStatus.OK);
    }

    @Operation(summary = "Obtener todos los correos electrónicos", description = "Devuelve una lista de todos los correos electrónicos")
    @GetMapping("/emails")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<String>> findAllEmails() {
        logger.info("Inicio de findAllEmails");
        List<String> emails = usuarioService.findAllEmails();
        logger.info("Fin de findAllEmails, {} correos electrónicos encontrados", emails.size());
        return new ResponseEntity<>(emails, HttpStatus.OK);
    }

    @Operation(summary = "Obtener todas las compras por ID de usuario", description = "Devuelve una lista de todas las compras realizadas por el usuario con el ID proporcionado")
    @GetMapping("/compras/{id}")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<List<LibroDTO>> findAllComprasByUsuarioId(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable long id) {
        logger.info("Inicio de findAllComprasByUsuarioId con ID de usuario: {}", id);
        List<Libro> compras = usuarioService.findAllComprasByUsuarioId(id);
        List<LibroDTO> comprasDto = new ArrayList<>();
        for (Libro libro : compras) {
            comprasDto.add(new LibroDTO(libro));
        }
        logger.info("Fin de findAllComprasByUsuarioId, {} compras encontradas", comprasDto.size());
        return new ResponseEntity<>(comprasDto, HttpStatus.OK);
    }

    @Operation(summary = "Obtener todas las compras por ID de usuario", description = "Devuelve una lista de todas las compras realizadas por el usuario con el ID proporcionado")
    @GetMapping("/carrito/{id}")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<List<LibroDTO>> findAllCarritoByUsuarioId(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable long id) {
        logger.info("Inicio de findAllComprasByUsuarioId con ID de usuario: {}", id);
        List<Libro> carrito = usuarioService.findAllCarritoByUsuarioId(id);
        List<LibroDTO> carritoDto = new ArrayList<>();
        for (Libro libro : carrito) {
            carritoDto.add(new LibroDTO(libro));
        }
        logger.info("Fin de findAllComprasByUsuarioId, {} compras encontradas", carritoDto.size());
        return new ResponseEntity<>(carritoDto, HttpStatus.OK);
    }

    @Operation(summary = "Obtener todas las ventas por ID de usuario", description = "Devuelve una lista de todas las ventas realizadas por el usuario con el ID proporcionado")
    @GetMapping("/ventas/{id}")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<List<LibroDTO>> findAllVentasByUsuarioId(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable long id) {
        logger.info("Inicio de findAllVentasByUsuarioId con ID de usuario: {}", id);
        List<Libro> carrito = usuarioService.findAllVentasByUsuarioId(id);
        List<LibroDTO> carritoDto = new ArrayList<>();
        for (Libro libro : carrito) {
            carritoDto.add(new LibroDTO(libro));
        }
        logger.info("Fin de findAllVentasByUsuarioId, {} compras encontradas", carritoDto.size());
        return new ResponseEntity<>(carritoDto, HttpStatus.OK);
    }

    @Operation(summary = "Añadir un nuevo usuario", description = "Agrega un nuevo usuario a la base de datos")
    @PostMapping("/usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Usuario> addUsuario(
            @Parameter(description = "Usuario a añadir") @RequestBody @Valid Usuario usuario) {
        logger.info("Inicio de addUsuario con usuario: {}", usuario);
        Usuario addUsuario = usuarioService.addUsuario(usuario);
        logger.info("Fin de addUsuario, usuario añadido con ID: {}", addUsuario.getId());
        return new ResponseEntity<>(addUsuario, HttpStatus.OK);
    }

    @Operation(summary = "Modificar un usuario existente", description = "Modifica un usuario existente en la base de datos")
    @PutMapping("/usuarios/{id}")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<Usuario> saveUsuario(
            @Parameter(description = "ID del usuario a modificar", example = "1") @PathVariable long id,
            @Parameter(description = "Datos actualizados del usuario") @RequestBody @Valid Usuario usuario) {
        logger.info("Inicio de saveUsuario con ID: {}", id);
        Usuario savedUsuario = usuarioService.modifyUsuario(id, usuario);
        logger.info("Fin de saveUsuario, usuario modificado con ID: {}", savedUsuario.getId());
        return new ResponseEntity<>(savedUsuario, HttpStatus.OK);
    }

    @Operation(summary = "Modificar la imagen de un usuario existente", description = "Modifica la imagen de un usuario existente en la base de datos")
    @PutMapping("/saveUsuarioWithImage/{id}")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<UsuarioDTO> saveUsuarioWithImage(
            @Parameter(description = "ID del usuario a modificar", example = "1") @PathVariable long id,
            @Parameter(description = "URL de la nueva imagen") @RequestBody Map<String, String> urlMap) {
        logger.info("Inicio de saveUsuarioWithImage con ID: {}", id);

        String url = urlMap.get("url");
        if (url == null || url.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Usuario savedUsuario = usuarioService.saveUsuarioWithImage(id, url);
        UsuarioDTO savedUsuarioDto = new UsuarioDTO(savedUsuario);
        logger.info("Fin de saveUsuarioWithImage, usuario modificado con ID: {}", savedUsuario.getId());
        return new ResponseEntity<>(savedUsuarioDto, HttpStatus.OK);
    }

    @Operation(summary = "Guardar usuario con libro", description = "Asocia un libro a un usuario existente")
    @PutMapping("/usuariosWithLibro/{id}")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<Usuario> saveUsuarioWithLibro(
            @Parameter(description = "ID del usuario", example = "1") @PathVariable long id,
            @Parameter(description = "Libro a asociar con el usuario") @RequestBody @Valid Libro libro) {
        logger.info("Inicio de saveUsuarioWithLibro con ID de usuario: {}", id);
        Usuario saveUsuario = usuarioService.saveUsuarioWithLibro(id, libro);
        logger.info("Fin de saveUsuarioWithLibro, usuario modificado con ID: {}", saveUsuario.getId());
        return new ResponseEntity<>(saveUsuario, HttpStatus.OK);
    }
}
