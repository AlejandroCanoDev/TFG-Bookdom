package com.example.bookdom.models;

import com.example.bookdom.enums.EstadoChat;
import com.example.bookdom.enums.TipoChat;

import java.time.LocalDateTime;
import java.util.List;

public class Chat {
    private String nombre;

    private LocalDateTime fechaCreacion;

    private EstadoChat estado;

    private TipoChat tipo;

    private List<Mensaje> mensajes;

    private List<Usuario> participantes;

    private Comunidad comunidad;
}
