package com.eventnode.eventnodeapi.repositories;

import com.eventnode.eventnodeapi.models.DiplomaEmitido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiplomaEmitidoRepository extends JpaRepository<DiplomaEmitido, Integer> {
    List<DiplomaEmitido> findByUsuario_IdUsuario(Integer idUsuario);
    Optional<DiplomaEmitido> findByDiploma_IdDiplomaAndUsuario_IdUsuario(Integer idDiploma, Integer idUsuario);
}
