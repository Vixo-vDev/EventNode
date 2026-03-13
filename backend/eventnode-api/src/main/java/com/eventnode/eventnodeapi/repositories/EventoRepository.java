package com.eventnode.eventnodeapi.repositories;

import com.eventnode.eventnodeapi.models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EventoRepository extends JpaRepository<Evento, Integer> {

    Optional<Evento> findByNombreAndFechaInicio(String nombre, LocalDateTime fechaInicio);
}

