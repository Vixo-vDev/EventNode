package com.eventnode.eventnodeapi.repositories;

import com.eventnode.eventnodeapi.models.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer> {

    List<Asistencia> findByIdEvento(Integer idEvento);
    List<Asistencia> findByIdEventoAndEstado(Integer idEvento, String estado);

    Optional<Asistencia> findByIdUsuarioAndIdEvento(Integer idUsuario, Integer idEvento);

    long countByIdEvento(Integer idEvento);
}
