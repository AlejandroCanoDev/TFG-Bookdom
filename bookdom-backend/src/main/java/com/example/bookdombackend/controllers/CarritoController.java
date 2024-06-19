package com.example.bookdombackend.controllers;

import com.example.bookdombackend.model.service.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/carrito")
@Tag(name = "Carritos", description = "Operaciones relacionadas con los carritos")
public class CarritoController {

    private final UsuarioService usuarioService;

    public CarritoController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Añadir un nuevo libro al carrito", description = "Agrega un nuevo libro al carrito del usuario")
    @PostMapping("/agregar")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<?> agregarLibroAlCarrito(@RequestParam Long usuarioId, @RequestParam Long libroId) {
        usuarioService.agregarLibroAlCarrito(usuarioId, libroId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Elimina un libro del carrito", description = "Elimina un libro del carrito del usuario")
    @DeleteMapping("/eliminar")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<?> eliminarLibroDelCarrito(@RequestParam Long usuarioId, @RequestParam Long libroId) {
        usuarioService.eliminarLibroDelCarrito(usuarioId, libroId);
        return ResponseEntity.ok().build();
    }
}
