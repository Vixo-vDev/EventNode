package com.eventnode.eventnodeapi.repositories;

import com.eventnode.eventnodeapi.models.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Integer> {
    List<Asistencia> findByEvento_IdEvento(Integer idEvento);
    Optional<Asistencia> findByUsuario_IdUsuarioAndEvento_IdEvento(Integer idUsuario, Integer idEvento);
}
