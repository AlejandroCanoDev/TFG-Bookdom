package com.example.bookdombackend.model.service.libro;

import com.example.bookdombackend.exceptions.Libro.LibroDeleteException;
import com.example.bookdombackend.exceptions.Libro.LibroNotFoundException;
import com.example.bookdombackend.model.entity.CondicionLibro;
import com.example.bookdombackend.model.entity.Genero;
import com.example.bookdombackend.model.entity.Libro;
import com.example.bookdombackend.model.repository.ILibroRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService implements ILibroService {

    private final ILibroRepository libroRepository;

    public LibroService(ILibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    @Override
    public List<Libro> findAll() {
        return (List<Libro>) libroRepository.findAll();
    }

    @Override
    public Libro findById(long id) {
        return libroRepository.findById(id)
                .orElseThrow(() -> new LibroNotFoundException("No se encontró el libro con id " + id));
    }

    @Override
    public Libro addLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    @Override
    public Libro modifyLibro(long id, Libro newLibro) {
        Libro libro = libroRepository.findById(id)
                .orElseThrow(() -> new LibroNotFoundException("No se encontró el libro con id " + id));
        newLibro.setId(libro.getId());
        return libroRepository.save(newLibro);
    }

    @Override
    public void deleteLibro(long id) {
        try {
            libroRepository.deleteById(id);
        } catch (Exception ex) {
            throw new LibroDeleteException("No se pudo eliminar el libro con id " + id);
        }
    }

    @Override
    public List<Libro> findByTituloOrAutor(String text) {
        return libroRepository.findByTituloOrAutor(text);
    }

    @Override
    public List<Libro> findByGenero(Genero genero) {
        return libroRepository.findByGenero(genero);
    }

    @Override
    public List<Libro> findByCondicion(CondicionLibro condicion) {
        return libroRepository.findByCondicion(condicion);
    }

    @Override
    public List<Libro> findAllOrderByPrecioDesc() {
        return libroRepository.findAllOrderByPrecioDesc();
    }

    @Override
    public List<Libro> findAllOrderByPrecioAsc() {
        return libroRepository.findAllOrderByPrecioAsc();
    }
}
