package com.eventnode.eventnodeapi.repositories;

import com.eventnode.eventnodeapi.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: repositorio de acceso a categorias para reglas de eventos en backend.
 * Por que existe: desacopla consultas de persistencia y mantiene contrato estable Service -> DB.
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    Optional<Categoria> findByNombreIgnoreCase(String nombre);
    boolean existsByNombreIgnoreCaseAndIdCategoriaIsNot(String nombre, Integer idCategoria);
}
