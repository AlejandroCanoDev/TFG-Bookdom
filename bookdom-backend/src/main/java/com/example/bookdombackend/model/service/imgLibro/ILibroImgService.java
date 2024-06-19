package com.example.bookdombackend.model.service.imgLibro;

import com.example.bookdombackend.model.entity.ImgLibro;
import com.example.bookdombackend.model.entity.Libro;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ILibroImgService {
    ImgLibro addImgLibro(ImgLibro imgLibro);

}
