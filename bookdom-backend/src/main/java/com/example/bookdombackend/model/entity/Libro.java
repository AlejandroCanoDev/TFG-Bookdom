package com.example.bookdombackend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "libro")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private CondicionLibro condicion;

    @Column(nullable = false)
    private Genero genero;

    @Column(nullable = false)
    private String descripcion;

    @Column
    private Boolean vendido;

    @Column
    private Boolean reservado;

    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImgLibro> imagenesLibro;

    @ManyToOne
    @JoinColumn(name = "vendedor_id", referencedColumnName = "id")
    private Usuario vendedor;

    @ManyToMany(mappedBy = "compras")
    @JsonIgnore
    private List<Usuario> usuariosCompra;

    @ManyToMany(mappedBy = "carrito", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Usuario> usuariosCarrito;
}
