package com.example.bookdombackend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "img-historia")
public class ImgHistoria {
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
    @JoinColumn(name = "historia_id", referencedColumnName = "id")
    private Historia historia;
}
