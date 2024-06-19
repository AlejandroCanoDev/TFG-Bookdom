package com.example.bookdom;

import com.example.bookdom.dto.usuario.UsuarioLibroDTO;
import com.example.bookdom.enums.CondicionLibro;
import com.example.bookdom.enums.Genero;
import com.example.bookdom.models.ImgLibro;
import com.example.bookdom.models.Libro;
import com.example.bookdom.models.Usuario;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LibroTest {
    private Libro libro;

    @Before
    public void setUp() {
        libro = new Libro();
    }

    @Test
    public void testGetSetId() {
        Long id = 1L;
        libro.setId(id);
        assertEquals(id, libro.getId());
    }

    @Test
    public void testGetSetTitulo() {
        String titulo = "Test Title";
        libro.setTitulo(titulo);
        assertEquals(titulo, libro.getTitulo());
    }

    @Test
    public void testGetSetAutor() {
        String autor = "Test Author";
        libro.setAutor(autor);
        assertEquals(autor, libro.getAutor());
    }

    @Test
    public void testGetSetPrecio() {
        Double precio = 29.99;
        libro.setPrecio(precio);
        assertEquals(precio, libro.getPrecio());
    }

    @Test
    public void testGetSetCondicion() {
        CondicionLibro condicion = CondicionLibro.Buen_estado;
        libro.setCondicion(condicion);
        assertEquals(condicion, libro.getCondicion());
    }

    @Test
    public void testGetSetGenero() {
        Genero genero = Genero.Aventura;
        libro.setGenero(genero);
        assertEquals(genero, libro.getGenero());
    }

    @Test
    public void testGetSetDescripcion() {
        String descripcion = "This is a test description.";
        libro.setDescripcion(descripcion);
        assertEquals(descripcion, libro.getDescripcion());
    }

    @Test
    public void testGetSetVendido() {
        Boolean vendido = true;
        libro.setVendido(vendido);
        assertEquals(vendido, libro.getVendido());
    }

    @Test
    public void testGetSetReservado() {
        Boolean reservado = true;
        libro.setReservado(reservado);
        assertEquals(reservado, libro.getReservado());
    }

    @Test
    public void testGetSetImagenesLibro() {
        List<ImgLibro> imagenes = new ArrayList<>();
        imagenes.add(new ImgLibro("url"));
        libro.setImagenesLibro(imagenes);
        assertEquals(imagenes, libro.getImagenesLibro());
    }

    @Test
    public void testGetSetComprador() {
        UsuarioLibroDTO comprador = new UsuarioLibroDTO(new Usuario());
        libro.setComprador(comprador);
        assertEquals(comprador, libro.getComprador());
    }

    @Test
    public void testGetSetVendedor() {
        UsuarioLibroDTO vendedor = new UsuarioLibroDTO(new Usuario());
        libro.setVendedor(vendedor);
        assertEquals(vendedor, libro.getVendedor());
    }

    @Test
    public void testGetSetUsuariosCarrito() {
        List<Usuario> usuariosCarrito = new ArrayList<>();
        usuariosCarrito.add(new Usuario());
        libro.setUsuariosCarrito(usuariosCarrito);
        assertEquals(usuariosCarrito, libro.getUsuariosCarrito());
    }
}
