package com.eventnode.eventnodeapi.repositories;

import com.eventnode.eventnodeapi.models.EventoOrganizador;
import com.eventnode.eventnodeapi.models.EventoOrganizadorId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoOrganizadorRepository extends JpaRepository<EventoOrganizador, EventoOrganizadorId> {
    List<EventoOrganizador> findByEvento_IdEvento(Integer idEvento);
    void deleteByEvento_IdEvento(Integer idEvento);
}
