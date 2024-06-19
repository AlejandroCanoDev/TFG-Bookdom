package com.example.bookdombackend.model.service.imgLibro;

import com.example.bookdombackend.model.entity.ImgLibro;
import com.example.bookdombackend.model.repository.ImgLibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImgLibroService implements ILibroImgService{
    @Autowired
    private ImgLibroRepository imgLibroRepository;
    @Override
    public ImgLibro addImgLibro(ImgLibro imgLibro) {
        return imgLibroRepository.save(imgLibro);
    }
}
