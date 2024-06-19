package com.example.bookdombackend.model.repository;

import com.example.bookdombackend.model.entity.Comunidad;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Hidden
public interface IComunidadRepository extends CrudRepository<Comunidad, Long> {
}
