package com.example.bookdombackend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comunidad")
public class Comunidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @ManyToMany(mappedBy = "comunidades", cascade = CascadeType.ALL)
    private List<Usuario> participantes;

    @OneToMany(mappedBy = "comunidad", cascade = CascadeType.ALL)
    private List<Chat> chats;

    @OneToMany(mappedBy = "comunidad", cascade = CascadeType.ALL)
    private List<Personaje> personajes;

    @OneToMany(mappedBy = "comunidad", cascade = CascadeType.ALL)
    private List<Artefacto> artefactos;

    @OneToOne(mappedBy = "comunidad", cascade = CascadeType.ALL)
    private Historia historia;

    @OneToOne(mappedBy = "comunidad", cascade = CascadeType.ALL)
    private Mundo mundo;
}
