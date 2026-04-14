package com.eventnode.eventnodeapi.schedulers;

import com.eventnode.eventnodeapi.models.Evento;
import com.eventnode.eventnodeapi.repositories.EventoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventoScheduler {

    private final EventoRepository eventoRepository;

    public EventoScheduler(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void actualizarEstadosEventos() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Evento> eventos = eventoRepository.findAll();
        for (Evento evento : eventos) {
            String estado = evento.getEstado();

            if ("PRÓXIMO".equals(estado) && evento.getFechaInicio() != null
                    && !evento.getFechaInicio().isAfter(ahora)) {
                evento.setEstado("ACTIVO");
                eventoRepository.save(evento);
            } else if ("ACTIVO".equals(estado) && evento.getFechaFin() != null
                    && evento.getFechaFin().isBefore(ahora)) {
                evento.setEstado("FINALIZADO");
                eventoRepository.save(evento);
            }
        }
    }
}
