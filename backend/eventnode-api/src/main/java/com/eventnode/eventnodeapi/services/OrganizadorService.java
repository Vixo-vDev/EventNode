package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.dtos.OrganizadorRequest;
import com.eventnode.eventnodeapi.models.Organizador;
import com.eventnode.eventnodeapi.repositories.OrganizadorRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizadorService {

    private final OrganizadorRepository organizadorRepository;

    public OrganizadorService(OrganizadorRepository organizadorRepository) {
        this.organizadorRepository = organizadorRepository;
    }

    public List<Organizador> obtenerTodos() {
        return organizadorRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SUPERADMIN')")
    public Organizador crearOrganizador(OrganizadorRequest request) {
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del organizador es obligatorio");
        }

        Organizador organizador = new Organizador();
        organizador.setNombre(request.getNombre());
        organizador.setDescripcion(request.getDescripcion());
        organizador.setCorreo(request.getCorreo());
        
        return organizadorRepository.save(organizador);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SUPERADMIN')")
    public Organizador actualizarOrganizador(Integer id, OrganizadorRequest request) {
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del organizador es obligatorio");
        }

        Organizador organizadorExistente = organizadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Organizador no encontrado con ID: " + id));

        organizadorExistente.setNombre(request.getNombre());
        organizadorExistente.setDescripcion(request.getDescripcion());
        organizadorExistente.setCorreo(request.getCorreo());

        return organizadorRepository.save(organizadorExistente);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SUPERADMIN')")
    public void eliminarOrganizador(Integer id) {
        Organizador organizadorExistente = organizadorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Organizador no encontrado con ID: " + id));

        // Let DataIntegrityViolationException propagate if Organizer is linked to an Event
        organizadorRepository.delete(organizadorExistente);
    }
}
