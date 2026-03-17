package com.eventnode.eventnodeapi.repositories;

import com.eventnode.eventnodeapi.models.Organizador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizadorRepository extends JpaRepository<Organizador, Integer> {

    List<Organizador> findByNombreContainingIgnoreCase(String nombre);
}

