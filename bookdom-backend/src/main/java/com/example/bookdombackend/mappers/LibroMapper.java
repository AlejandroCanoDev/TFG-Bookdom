package com.example.bookdombackend.mappers;
import com.example.bookdombackend.dto.libro.LibroDTO;
import com.example.bookdombackend.dto.libro.LibroListDTO;
import com.example.bookdombackend.model.entity.Libro;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
@Component
@AllArgsConstructor
public class LibroMapper {
    @Autowired
    ModelMapper modelMapper;

    public LibroListDTO toLibroListDTO(Libro libro) {
        return modelMapper.map(libro, LibroListDTO.class);
    }
    public LibroDTO toLibroDTO(Libro libro) {
        return modelMapper.map(libro, LibroDTO.class);
    }

    public List<LibroListDTO> mapToDTOList(List<Libro> libros) {
        return libros.stream()
                .map(this::toLibroListDTO)
                .collect(Collectors.toList());
    }
    public List<LibroDTO> mapToDTO(List<Libro> libros) {
        return libros.stream()
                .map(this::toLibroDTO)
                .collect(Collectors.toList());
    }
}