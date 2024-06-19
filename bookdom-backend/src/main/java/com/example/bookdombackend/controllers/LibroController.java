package com.example.bookdombackend.controllers;

import com.example.bookdombackend.dto.libro.LibroDTO;
import com.example.bookdombackend.dto.libro.LibroListDTO;
import com.example.bookdombackend.mappers.LibroMapper;
import com.example.bookdombackend.mappers.UsuarioMapper;
import com.example.bookdombackend.model.entity.CondicionLibro;
import com.example.bookdombackend.model.entity.Genero;
import com.example.bookdombackend.model.entity.ImgLibro;
import com.example.bookdombackend.model.entity.Libro;
import com.example.bookdombackend.model.service.libro.ILibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/api")
@Tag(name = "Libros", description = "Operaciones relacionadas con los libros")
public class LibroController {

    private static final Logger logger = LoggerFactory.getLogger(LibroController.class);

    @Autowired
    ILibroService libroService;

    @Autowired
    LibroMapper libroMapper;

    @Autowired
    UsuarioMapper usuarioMapper;

    @Operation(summary = "Obtener todos los libros", description = "Devuelve una lista de todos los libros disponibles")
    @GetMapping("/libros")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<List<LibroListDTO>> findAllLibros() {
        logger.info("Inicio de findAllLibros");
        List<Libro> libros = libroService.findAll();
        List<LibroListDTO> librosDTO = new ArrayList<>();
        for (Libro libro : libros) {
            librosDTO.add(new LibroListDTO(libro));
        }
        logger.info("Fin de findAllLibros, {} libros encontrados", librosDTO.size());
        return new ResponseEntity<>(librosDTO, HttpStatus.OK);
    }

    @Operation(summary = "Buscar libros por título o autor", description = "Devuelve una lista de libros que coinciden con el título o autor proporcionado")
    @GetMapping("/librosByTituloOrAutor/{text}")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<List<LibroListDTO>> findByTituloOrAutor(
            @Parameter(description = "Texto a buscar en el título o autor", example = "Harry Potter") @PathVariable String text) {
        logger.info("Inicio de findByTituloOrAutor con texto: {}", text);
        List<Libro> libros = libroService.findByTituloOrAutor(text);
        List<LibroListDTO> librosDTO = new ArrayList<>();
        for (Libro libro : libros) {
            librosDTO.add(new LibroListDTO(libro));
        }
        logger.info("Fin de findByTituloOrAutor, {} libros encontrados", librosDTO.size());
        return new ResponseEntity<>(librosDTO, HttpStatus.OK);
    }

    @Operation(summary = "Buscar libros por genero", description = "Devuelve una lista de libros que coinciden con el genero proporcionado")
    @GetMapping("/librosByGenero/{genero}")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<List<LibroListDTO>> findByGenero(
            @Parameter(description = "Genero del libro a buscar", example = "0") @PathVariable Genero genero) {
        logger.info("Inicio de findByGenero con estado: {}", genero);
        List<Libro> libros = libroService.findByGenero(genero);
        List<LibroListDTO> librosDTO = new ArrayList<>();
        for (Libro libro : libros) {
            librosDTO.add(new LibroListDTO(libro));
        }
        logger.info("Fin de findByGenero, {} libros encontrados", librosDTO.size());
        return new ResponseEntity<>(librosDTO, HttpStatus.OK);
    }

    @Operation(summary = "Buscar libros por condición", description = "Devuelve una lista de libros que coinciden con la condición proporcionada")
    @GetMapping("/librosByCondicion/{condicion}")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<List<LibroListDTO>> findByCondicion(
            @Parameter(description = "Condición del libro a buscar", example = "0") @PathVariable CondicionLibro condicion) {
        logger.info("Inicio de findByCondicion con condición: {}", condicion);
        List<Libro> libros = libroService.findByCondicion(condicion);
        List<LibroListDTO> librosDTO = new ArrayList<>();
        for (Libro libro : libros) {
            librosDTO.add(new LibroListDTO(libro));
        }
        logger.info("Fin de findByCondicion, {} libros encontrados", librosDTO.size());
        return new ResponseEntity<>(librosDTO, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar libro", description = "Elimina un libro por su ID")
    @DeleteMapping("/libros/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteLibroById(
            @Parameter(description = "Id del libro a eliminar", example = "1") @PathVariable long id) {
        logger.info("Inicio de deleteLibroById con id: {}", id);

        Map<String, String> response = new HashMap<>();

        try {
            libroService.deleteLibro(id);
            logger.info("Libro con id {} eliminado correctamente", id);
            response.put("message", "Libro eliminado correctamente");
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException e) {
            logger.error("Libro con id {} no encontrado", id);
            response.put("error", "Libro no encontrado");
            return ResponseEntity.status(404).body(response);
        } catch (Exception e) {
            logger.error("Error al eliminar el libro con id {}", id, e);
            response.put("error", "Error interno del servidor");
            return ResponseEntity.status(500).body(response);
        }
    }

    @Operation(summary = "Buscar libro por ID", description = "Devuelve un libro que coincide con el ID proporcionado")
    @GetMapping("/librosById/{id}")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<LibroDTO> findLibroById(
            @Parameter(description = "ID del libro a buscar", example = "1") @PathVariable long id) {
        logger.info("Inicio de findLibroById con ID: {}", id);
        Libro libro = libroService.findById(id);
        LibroDTO libroDTO = new LibroDTO(libro);
        logger.info("Fin de findLibroById");
        return new ResponseEntity<>(libroDTO, HttpStatus.OK);
    }

    @Operation(summary = "Añadir un nuevo libro", description = "Agrega un nuevo libro a la base de datos")
    @PostMapping("/libros")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<LibroDTO> addLibro(
            @Parameter(description = "Libro a añadir") @RequestBody @Valid Libro libro) {
        logger.info("Inicio de addLibro con libro: {}", libro);
        List<ImgLibro> imagenesLibro = libro.getImagenesLibro();
        for (ImgLibro imgLibro : imagenesLibro) {
            imgLibro.setLibro(libro);
        }
        Libro addLibro = libroService.addLibro(libro);
        LibroDTO libroDTO = libroMapper.toLibroDTO(addLibro);
        logger.info("Fin de addLibro, libro añadido con ID: {}", addLibro.getId());
        return new ResponseEntity<>(libroDTO, HttpStatus.OK);
    }

    @Operation(summary = "Modificar un libro existente", description = "Modifica un libro existente en la base de datos")
    @PutMapping("/libros/{id}")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<LibroDTO> modifyLibro(
            @Parameter(description = "ID del libro a modificar", example = "1") @PathVariable long id,
            @Parameter(description = "Datos actualizados del libro") @RequestBody @Valid Libro newLibro) {
        logger.info("Inicio de modifyLibro con ID: {}", id);
        Libro libro = libroService.modifyLibro(id, newLibro);
        LibroDTO libroDTO = libroMapper.toLibroDTO(libro);
        logger.info("Fin de modifyLibro, libro modificado con ID: {}", libro.getId());
        return new ResponseEntity<>(libroDTO, HttpStatus.OK);
    }

    @Operation(summary = "Obtener libros en orden normal", description = "Devuelve una lista de libros en orden normal")
    @GetMapping("/librosNormal")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<List<LibroListDTO>> listarLibrosNormal() {
        logger.info("Inicio de listarLibrosNormal");
        List<Libro> libros = libroService.findAll();
        List<LibroListDTO> librosDTO = new ArrayList<>();
        for (Libro libro : libros) {
            librosDTO.add(new LibroListDTO(libro));
        }
        logger.info("Fin de listarLibrosNormal, {} libros encontrados", librosDTO.size());
        return new ResponseEntity<>(librosDTO, HttpStatus.OK);
    }

    @Operation(summary = "Obtener libros en orden inverso", description = "Devuelve una lista de libros en orden inverso")
    @GetMapping("/librosInverso")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<List<LibroListDTO>> listarLibrosInversa() {
        logger.info("Inicio de listarLibrosInversa");
        List<Libro> libros = libroService.findAll();
        Collections.reverse(libros);
        List<LibroListDTO> librosDTO = new ArrayList<>();
        for (Libro libro : libros) {
            librosDTO.add(new LibroListDTO(libro));
        }
        logger.info("Fin de listarLibrosInversa, {} libros encontrados", librosDTO.size());
        return new ResponseEntity<>(librosDTO, HttpStatus.OK);
    }

    @Operation(summary = "Obtener libros por precio descendente", description = "Devuelve una lista de libros ordenada por precio descendente")
    @GetMapping("/librosPorPrecioDesc")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<List<LibroListDTO>> listarPorPrecioDescendente() {
        logger.info("Inicio de listarPorPrecioDescendente");
        List<Libro> libros = libroService.findAllOrderByPrecioDesc();
        List<LibroListDTO> librosDTO = new ArrayList<>();
        for (Libro libro : libros) {
            librosDTO.add(new LibroListDTO(libro));
        }
        logger.info("Fin de listarPorPrecioDescendente, {} libros encontrados", librosDTO.size());
        return new ResponseEntity<>(librosDTO, HttpStatus.OK);
    }

    @Operation(summary = "Obtener libros por precio ascendente", description = "Devuelve una lista de libros ordenada por precio ascendente")
    @GetMapping("/librosPorPrecioAsc")
    @PreAuthorize("hasAnyRole('USUARIO', 'ADMIN')")
    public ResponseEntity<List<LibroListDTO>> listarPorPrecioAscendente() {
        logger.info("Inicio de listarPorPrecioAscendente");
        List<Libro> libros = libroService.findAllOrderByPrecioAsc();
        List<LibroListDTO> librosDTO = new ArrayList<>();
        for (Libro libro : libros) {
            librosDTO.add(new LibroListDTO(libro));
        }
        logger.info("Fin de listarPorPrecioAscendente, {} libros encontrados", librosDTO.size());
        return new ResponseEntity<>(librosDTO, HttpStatus.OK);
    }
}
