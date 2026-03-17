package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.models.Diploma;
import com.eventnode.eventnodeapi.models.DiplomaEmitido;
import com.eventnode.eventnodeapi.models.Asistencia;
import com.eventnode.eventnodeapi.models.Evento;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.repositories.DiplomaRepository;
import com.eventnode.eventnodeapi.repositories.DiplomaEmitidoRepository;
import com.eventnode.eventnodeapi.repositories.AsistenciaRepository;
import com.eventnode.eventnodeapi.repositories.EventoRepository;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class DiplomaService {

    private final DiplomaRepository diplomaRepository;
    private final DiplomaEmitidoRepository diplomaEmitidoRepository;
    private final AsistenciaRepository asistenciaRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    public DiplomaService(DiplomaRepository diplomaRepository,
                         DiplomaEmitidoRepository diplomaEmitidoRepository,
                         AsistenciaRepository asistenciaRepository,
                         EventoRepository eventoRepository,
                         UsuarioRepository usuarioRepository) {
        this.diplomaRepository = diplomaRepository;
        this.diplomaEmitidoRepository = diplomaEmitidoRepository;
        this.asistenciaRepository = asistenciaRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void crearDiploma(Integer idEvento, String firma, String diseno) {
        // Validate event exists
        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));

        // Check if diploma already exists for this event
        if (diplomaRepository.findByIdEvento(idEvento).isPresent()) {
            throw new IllegalStateException("Ya existe un diploma para este evento");
        }

        // Create Diploma
        Diploma diploma = new Diploma();
        diploma.setIdEvento(idEvento);
        diploma.setNombreEvento(evento.getNombre());
        diploma.setFirma(firma);
        diploma.setDiseno(diseno);
        diploma.setFechaCreacion(LocalDateTime.now());
        diploma.setEstado("ACTIVO");

        diplomaRepository.save(diploma);
    }

    public List<Map<String, Object>> listarDiplomas() {
        List<Diploma> diplomas = diplomaRepository.findByEstado("ACTIVO");

        return diplomas.stream().map(d -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idDiploma", d.getIdDiploma());
            map.put("idEvento", d.getIdEvento());
            map.put("nombreEvento", d.getNombreEvento());
            map.put("firma", d.getFirma());
            map.put("diseno", d.getDiseno());
            map.put("fechaCreacion", d.getFechaCreacion());

            long totalEmitidos = diplomaEmitidoRepository.countByIdDiploma(d.getIdDiploma());
            map.put("totalEmitidos", totalEmitidos);

            // Get count of asistencias for this event
            long countAsistencias = asistenciaRepository.countByIdEvento(d.getIdEvento());
            long totalPendientes = countAsistencias - totalEmitidos;
            map.put("totalPendientes", totalPendientes);

            return map;
        }).collect(Collectors.toList());
    }

    @Transactional
    public long emitirDiplomas(Integer idDiploma) {
        Diploma diploma = diplomaRepository.findById(idDiploma)
                .orElseThrow(() -> new IllegalArgumentException("Diploma no encontrado"));

        List<Asistencia> asistencias = asistenciaRepository.findByIdEvento(diploma.getIdEvento());

        long count = 0;
        for (Asistencia asistencia : asistencias) {
            // Check if DiplomaEmitido already exists
            if (diplomaEmitidoRepository.findByIdDiploma(idDiploma).stream()
                    .noneMatch(de -> de.getIdUsuario().equals(asistencia.getIdUsuario()))) {

                DiplomaEmitido diplomaEmitido = new DiplomaEmitido();
                diplomaEmitido.setIdDiploma(idDiploma);
                diplomaEmitido.setIdUsuario(asistencia.getIdUsuario());
                diplomaEmitido.setFechaEnvio(LocalDateTime.now());
                diplomaEmitido.setEstadoEnvio("ENVIADO");

                diplomaEmitidoRepository.save(diplomaEmitido);
                count++;
            }
        }

        return count;
    }

    public List<Map<String, Object>> listarDiplomasEstudiante(Integer idUsuario) {
        List<DiplomaEmitido> diplomasEmitidos = diplomaEmitidoRepository.findByIdUsuario(idUsuario);

        return diplomasEmitidos.stream().map(de -> {
            Map<String, Object> map = new HashMap<>();
            map.put("idEmitido", de.getIdEmitido());
            map.put("idDiploma", de.getIdDiploma());
            map.put("fechaEnvio", de.getFechaEnvio());
            map.put("estadoEnvio", de.getEstadoEnvio());

            Diploma diploma = diplomaRepository.findById(de.getIdDiploma()).orElse(null);
            if (diploma != null) {
                map.put("nombreEvento", diploma.getNombreEvento());
                map.put("diseno", diploma.getDiseno());
            }

            return map;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> obtenerDiploma(Integer idDiploma) {
        Diploma diploma = diplomaRepository.findById(idDiploma)
                .orElseThrow(() -> new IllegalArgumentException("Diploma no encontrado"));

        Map<String, Object> map = new HashMap<>();
        map.put("idDiploma", diploma.getIdDiploma());
        map.put("idEvento", diploma.getIdEvento());
        map.put("nombreEvento", diploma.getNombreEvento());
        map.put("firma", diploma.getFirma());
        map.put("diseno", diploma.getDiseno());
        map.put("fechaCreacion", diploma.getFechaCreacion());
        map.put("estado", diploma.getEstado());

        List<DiplomaEmitido> emitidos = diplomaEmitidoRepository.findByIdDiploma(idDiploma);
        List<Map<String, Object>> emitidosList = emitidos.stream().map(de -> {
            Map<String, Object> emMap = new HashMap<>();
            emMap.put("idEmitido", de.getIdEmitido());
            emMap.put("idUsuario", de.getIdUsuario());
            emMap.put("fechaEnvio", de.getFechaEnvio());
            emMap.put("estadoEnvio", de.getEstadoEnvio());

            Usuario usuario = usuarioRepository.findById(de.getIdUsuario()).orElse(null);
            if (usuario != null) {
                String fullName = usuario.getNombre() + " " + usuario.getApellidoPaterno();
                if (usuario.getApellidoMaterno() != null && !usuario.getApellidoMaterno().isEmpty()) {
                    fullName += " " + usuario.getApellidoMaterno();
                }
                emMap.put("nombre", fullName);
            }

            return emMap;
        }).collect(Collectors.toList());

        map.put("emitidos", emitidosList);

        return map;
    }
}
