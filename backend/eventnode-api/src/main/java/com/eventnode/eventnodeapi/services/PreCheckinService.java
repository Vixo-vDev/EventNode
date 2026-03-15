package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.dtos.PreCheckinRequest;
import com.eventnode.eventnodeapi.models.Evento;
import com.eventnode.eventnodeapi.models.PreCheckin;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.repositories.EventoRepository;
import com.eventnode.eventnodeapi.repositories.PreCheckinRepository;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PreCheckinService {

    private final PreCheckinRepository preCheckinRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    public PreCheckinService(PreCheckinRepository preCheckinRepository,
                             EventoRepository eventoRepository,
                             UsuarioRepository usuarioRepository) {
        this.preCheckinRepository = preCheckinRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SUPERADMIN')")
    public List<PreCheckin> obtenerPorEvento(Integer idEvento) {
        return preCheckinRepository.findByEvento_IdEvento(idEvento);
    }

    @PreAuthorize("hasRole('ESTUDIANTE')")
    public List<PreCheckin> obtenerPorEstudiante(String correoEstudiante) {
        Usuario usuario = usuarioRepository.findByCorreo(correoEstudiante)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return preCheckinRepository.findByUsuario_IdUsuario(usuario.getIdUsuario());
    }

    @PreAuthorize("hasRole('ESTUDIANTE')")
    public PreCheckin realizarPreCheckin(PreCheckinRequest request, String correoEstudiante) {
        if (request.getIdEvento() == null) {
            throw new IllegalArgumentException("El ID del evento es obligatorio");
        }

        Usuario usuario = usuarioRepository.findByCorreo(correoEstudiante)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Evento evento = eventoRepository.findById(request.getIdEvento())
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));

        if (preCheckinRepository.findByUsuario_IdUsuarioAndEvento_IdEvento(usuario.getIdUsuario(), evento.getIdEvento()).isPresent()) {
            throw new IllegalStateException("El estudiante ya está registrado (Pre-Checkin) en este evento");
        }

        PreCheckin pc = new PreCheckin();
        pc.setUsuario(usuario);
        pc.setEvento(evento);
        pc.setFechaRegistro(LocalDateTime.now());
        pc.setEstado("ACTIVO");

        return preCheckinRepository.save(pc);
    }
}
