package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.models.Evento;
import com.eventnode.eventnodeapi.models.PreCheckin;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.models.Alumno;
import com.eventnode.eventnodeapi.repositories.PreCheckinRepository;
import com.eventnode.eventnodeapi.repositories.EventoRepository;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import com.eventnode.eventnodeapi.repositories.AlumnoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class PreCheckinService {

    private final PreCheckinRepository preCheckinRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;

    public PreCheckinService(PreCheckinRepository preCheckinRepository,
                           EventoRepository eventoRepository,
                           UsuarioRepository usuarioRepository,
                           AlumnoRepository alumnoRepository) {
        this.preCheckinRepository = preCheckinRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.alumnoRepository = alumnoRepository;
    }

    @Transactional
    public void inscribirse(Integer idUsuario, Integer idEvento) {
        // Validate user exists and is ALUMNO
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Alumno alumno = alumnoRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario no es un alumno"));

        // Validate event exists and is ACTIVO
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));

        if (!"ACTIVO".equals(evento.getEstado())) {
            throw new IllegalStateException("El evento no está activo");
        }

        // Check if event is full
        long countInscritos = preCheckinRepository.countByIdEventoAndEstado(idEvento, "ACTIVO");
        if (countInscritos >= evento.getCapacidadMaxima()) {
            throw new IllegalStateException("El evento está lleno");
        }

        // Check if already enrolled
        if (preCheckinRepository.findByIdUsuarioAndIdEvento(idUsuario, idEvento).isPresent()) {
            throw new IllegalStateException("El usuario ya está inscrito en este evento");
        }

        // Create PreCheckin
        PreCheckin preCheckin = new PreCheckin();
        preCheckin.setIdUsuario(idUsuario);
        preCheckin.setIdEvento(idEvento);
        preCheckin.setFechaRegistro(LocalDateTime.now());
        preCheckin.setEstado("ACTIVO");

        preCheckinRepository.save(preCheckin);
    }

    @Transactional
    public void cancelarInscripcion(Integer idUsuario, Integer idEvento) {
        PreCheckin preCheckin = preCheckinRepository.findByIdUsuarioAndIdEvento(idUsuario, idEvento)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró la inscripción"));

        if (!"ACTIVO".equals(preCheckin.getEstado())) {
            throw new IllegalStateException("La inscripción no está activa");
        }

        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));

        // Validate cancellation time
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime minCancelTime = evento.getFechaInicio().minusHours(evento.getTiempoCancelacionHoras());

        if (now.isAfter(minCancelTime)) {
            throw new IllegalStateException("Ya no es posible cancelar la inscripción. El tiempo límite ha expirado");
        }

        preCheckin.setEstado("CANCELADO");
        preCheckinRepository.save(preCheckin);
    }

    public List<Map<String, Object>> listarInscritos(Integer idEvento) {
        List<PreCheckin> preCheckins = preCheckinRepository.findByIdEvento(idEvento);
        List<PreCheckin> activosOnly = preCheckins.stream()
                .filter(pc -> "ACTIVO".equals(pc.getEstado()))
                .collect(Collectors.toList());

        return activosOnly.stream().map(pc -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idPrecheckin", pc.getIdPrecheckin());
            map.put("idUsuario", pc.getIdUsuario());

            Usuario usuario = usuarioRepository.findById(pc.getIdUsuario()).orElse(null);
            if (usuario != null) {
                String fullName = usuario.getNombre() + " " + usuario.getApellidoPaterno();
                if (usuario.getApellidoMaterno() != null && !usuario.getApellidoMaterno().isEmpty()) {
                    fullName += " " + usuario.getApellidoMaterno();
                }
                map.put("nombre", fullName);
                map.put("correo", usuario.getCorreo());

                // If user is ALUMNO, get matricula
                Alumno alumno = alumnoRepository.findById(pc.getIdUsuario()).orElse(null);
                if (alumno != null) {
                    map.put("matricula", alumno.getMatricula());
                }
            }

            map.put("fechaRegistro", pc.getFechaRegistro());
            map.put("estado", pc.getEstado());

            return map;
        }).collect(Collectors.toList());
    }

    public List<Map<String, Object>> listarEventosInscritos(Integer idUsuario) {
        List<PreCheckin> preCheckins = preCheckinRepository.findByIdUsuario(idUsuario);

        return preCheckins.stream().map(pc -> {
            Map<String, Object> map = new HashMap<>();

            Evento evento = eventoRepository.findById(pc.getIdEvento()).orElse(null);
            if (evento != null) {
                map.put("idEvento", evento.getIdEvento());
                map.put("nombre", evento.getNombre());
                map.put("ubicacion", evento.getUbicacion());
                map.put("fechaInicio", evento.getFechaInicio());
                map.put("fechaFin", evento.getFechaFin());
                map.put("estado", evento.getEstado());
                map.put("banner", evento.getBanner());

                if (evento.getCategoria() != null) {
                    map.put("categoriaNombre", evento.getCategoria().getNombre());
                }
            }

            map.put("inscripcionEstado", pc.getEstado());

            return map;
        }).collect(Collectors.toList());
    }

    public long contarInscritos(Integer idEvento) {
        return preCheckinRepository.countByIdEventoAndEstado(idEvento, "ACTIVO");
    }
}
