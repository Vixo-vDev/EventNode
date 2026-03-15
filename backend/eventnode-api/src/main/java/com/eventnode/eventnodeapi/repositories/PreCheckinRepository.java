package com.eventnode.eventnodeapi.repositories;

import com.eventnode.eventnodeapi.models.PreCheckin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PreCheckinRepository extends JpaRepository<PreCheckin, Integer> {
    List<PreCheckin> findByUsuario_IdUsuario(Integer idUsuario);
    List<PreCheckin> findByEvento_IdEvento(Integer idEvento);
    Optional<PreCheckin> findByUsuario_IdUsuarioAndEvento_IdEvento(Integer idUsuario, Integer idEvento);
}
