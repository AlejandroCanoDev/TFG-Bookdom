package com.example.bookdombackend.controllers;

import com.example.bookdombackend.model.service.usuario.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api/compras")
@Tag(name = "Compras", description = "Operaciones relacionadas con las compras")
public class ComprasController {

    @Autowired
    private UsuarioService usuarioService;

    @Operation(summary = "Añadir un nuevo libro al carrito", description = "Agrega un nuevo libro al carrito del usuario")
    @PostMapping("/agregar")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<?> agregarLibroACompras(@RequestParam Long usuarioId, @RequestParam Long libroId) {
        usuarioService.agregarLibroACompras(usuarioId, libroId);
        return ResponseEntity.ok().build();
    }
}
