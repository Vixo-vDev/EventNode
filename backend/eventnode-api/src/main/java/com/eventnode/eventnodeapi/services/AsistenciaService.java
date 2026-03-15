package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.dtos.AsistenciaRequest;
import com.eventnode.eventnodeapi.models.Asistencia;
import com.eventnode.eventnodeapi.models.Evento;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.repositories.AsistenciaRepository;
import com.eventnode.eventnodeapi.repositories.EventoRepository;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    public AsistenciaService(AsistenciaRepository asistenciaRepository,
                             EventoRepository eventoRepository,
                             UsuarioRepository usuarioRepository) {
        this.asistenciaRepository = asistenciaRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SUPERADMIN')")
    public List<Asistencia> obtenerPorEvento(Integer idEvento) {
        return asistenciaRepository.findByEvento_IdEvento(idEvento);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SUPERADMIN')")
    public Asistencia registrarAsistencia(AsistenciaRequest request) {
        if (request.getIdEvento() == null || request.getIdUsuario() == null || request.getMetodo() == null) {
            throw new IllegalArgumentException("El ID del evento, ID del usuario y método son obligatorios");
        }

        if (!request.getMetodo().equals("MANUAL") && !request.getMetodo().equals("QR")) {
            throw new IllegalArgumentException("El método debe ser 'MANUAL' o 'QR'");
        }

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + request.getIdUsuario()));

        Evento evento = eventoRepository.findById(request.getIdEvento())
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado con ID: " + request.getIdEvento()));

        if (asistenciaRepository.findByUsuario_IdUsuarioAndEvento_IdEvento(usuario.getIdUsuario(), evento.getIdEvento()).isPresent()) {
            throw new IllegalStateException("El usuario ya tiene asistencia registrada para este evento");
        }

        Asistencia asistencia = new Asistencia();
        asistencia.setUsuario(usuario);
        asistencia.setEvento(evento);
        asistencia.setFechaCheckin(LocalDateTime.now());
        asistencia.setMetodo(request.getMetodo());

        return asistenciaRepository.save(asistencia);
    }
}
