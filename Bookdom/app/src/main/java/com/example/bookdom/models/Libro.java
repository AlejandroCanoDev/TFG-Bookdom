package com.example.bookdom.models;

import com.example.bookdom.dto.usuario.UsuarioLibroDTO;
import com.example.bookdom.enums.CondicionLibro;
import com.example.bookdom.enums.Genero;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Libro implements Serializable {
    private Long id;
    private String titulo;
    private String autor;
    private Double precio;
    private CondicionLibro condicion;
    private Genero genero;
    private String descripcion;
    private Boolean vendido;
    private Boolean reservado;
    private List<ImgLibro> imagenesLibro;
    private UsuarioLibroDTO comprador;
    private UsuarioLibroDTO vendedor;
    private List<Usuario> usuariosCarrito; // Añadir lista de usuarios que tienen este libro en su carrito

    public Libro() {
        imagenesLibro = new ArrayList<>();
        usuariosCarrito = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public CondicionLibro getCondicion() {
        return condicion;
    }

    public void setCondicion(CondicionLibro condicion) {
        this.condicion = condicion;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getVendido() {
        return vendido;
    }

    public void setVendido(Boolean vendido) {
        this.vendido = vendido;
    }

    public Boolean getReservado() {
        return reservado;
    }

    public void setReservado(Boolean reservado) {
        this.reservado = reservado;
    }

    public List<ImgLibro> getImagenesLibro() {
        return imagenesLibro;
    }

    public void setImagenesLibro(List<ImgLibro> imagenesLibro) {
        this.imagenesLibro = imagenesLibro;
    }

    public UsuarioLibroDTO getComprador() {
        return comprador;
    }

    public void setComprador(UsuarioLibroDTO comprador) {
        this.comprador = comprador;
    }

    public UsuarioLibroDTO getVendedor() {
        return vendedor;
    }

    public void setVendedor(UsuarioLibroDTO vendedor) {
        this.vendedor = vendedor;
    }

    public List<Usuario> getUsuariosCarrito() {
        return usuariosCarrito;
    }

    public void setUsuariosCarrito(List<Usuario> usuariosCarrito) {
        this.usuariosCarrito = usuariosCarrito;
    }
}
