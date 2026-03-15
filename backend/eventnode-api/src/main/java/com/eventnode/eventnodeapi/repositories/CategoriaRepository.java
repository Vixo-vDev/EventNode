package com.eventnode.eventnodeapi.repositories;

import com.eventnode.eventnodeapi.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    boolean existsByNombre(String nombre);
}

