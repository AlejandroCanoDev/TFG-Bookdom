package com.example.bookdombackend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "img-mundo")
public class ImgMundo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    private byte[] imagen;

    @Column
    private String nombreArchivo;

    @Column
    private String tipoArchivo;

    @Column
    private Long tamanoArchivo;

    @ManyToOne
    @JoinColumn(name = "mundo_id", referencedColumnName = "id")
    private Mundo mundo;
}
