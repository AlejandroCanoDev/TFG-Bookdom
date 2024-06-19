package com.example.bookdom.models;

import com.example.bookdom.enums.Rol;
import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class Usuario implements Serializable {
    private Long id;
    private String nombreUsuario;
    private String email;
    private String password;
    private String fotoPerfil;
    private Rol rolUsuario;
    private List<Chat> chats;
    private List<Mensaje> mensajes;
    private List<Comunidad> comunidades;
    private List<Libro> compras;
    private List<Libro> ventas;
    private List<Libro> carrito; // Añadir la lista de libros en el carrito

    public Usuario(String nombreUsuario, String email, String password, Rol rolUsuario) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.rolUsuario = rolUsuario;
    }

    public Usuario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Rol getRolUsuario() {
        return rolUsuario;
    }

    public void setRolUsuario(Rol rolUsuario) {
        this.rolUsuario = rolUsuario;
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void setChats(List<Chat> chats) {
        this.chats = chats;
    }

    public List<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public List<Comunidad> getComunidades() {
        return comunidades;
    }

    public void setComunidades(List<Comunidad> comunidades) {
        this.comunidades = comunidades;
    }

    public List<Libro> getCompras() {
        return compras;
    }

    public void setCompras(List<Libro> compras) {
        this.compras = compras;
    }

    public List<Libro> getVentas() {
        return ventas;
    }

    public void setVentas(List<Libro> ventas) {
        this.ventas = ventas;
    }

    public List<Libro> getCarrito() {
        return carrito;
    }

    public void setCarrito(List<Libro> carrito) {
        this.carrito = carrito;
    }
}
