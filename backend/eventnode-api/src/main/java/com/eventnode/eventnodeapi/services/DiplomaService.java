package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.dtos.DiplomaRequest;
import com.eventnode.eventnodeapi.models.Diploma;
import com.eventnode.eventnodeapi.models.Evento;
import com.eventnode.eventnodeapi.repositories.DiplomaRepository;
import com.eventnode.eventnodeapi.repositories.EventoRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DiplomaService {

    private final DiplomaRepository diplomaRepository;
    private final EventoRepository eventoRepository;

    public DiplomaService(DiplomaRepository diplomaRepository, EventoRepository eventoRepository) {
        this.diplomaRepository = diplomaRepository;
        this.eventoRepository = eventoRepository;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SUPERADMIN')")
    public Diploma configurarDiploma(DiplomaRequest request) {
        if (request.getIdEvento() == null || request.getNombreEvento() == null || 
            request.getFirma() == null || request.getDiseno() == null) {
            throw new IllegalArgumentException("Todos los campos del diploma son obligatorios");
        }

        Evento evento = eventoRepository.findById(request.getIdEvento())
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado con ID: " + request.getIdEvento()));

        if (diplomaRepository.findByEvento_IdEvento(evento.getIdEvento()).isPresent()) {
            throw new IllegalStateException("El evento ya tiene un diploma configurado");
        }

        Diploma diploma = new Diploma();
        diploma.setEvento(evento);
        diploma.setNombreEvento(request.getNombreEvento());
        diploma.setFirma(request.getFirma());
        diploma.setDiseno(request.getDiseno());
        diploma.setFechaCreacion(LocalDateTime.now());
        diploma.setEstado("ACTIVO");

        return diplomaRepository.save(diploma);
    }
}
