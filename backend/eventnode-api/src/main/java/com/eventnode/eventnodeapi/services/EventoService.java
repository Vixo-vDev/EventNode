package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.dtos.EventoCreateRequest;
import com.eventnode.eventnodeapi.models.Categoria;
import com.eventnode.eventnodeapi.models.Evento;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.repositories.CategoriaRepository;
import com.eventnode.eventnodeapi.repositories.EventoRepository;
import com.eventnode.eventnodeapi.repositories.OrganizadorRepository;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final OrganizadorRepository organizadorRepository;

    public EventoService(EventoRepository eventoRepository,
                         CategoriaRepository categoriaRepository,
                         UsuarioRepository usuarioRepository,
                         OrganizadorRepository organizadorRepository) {
        this.eventoRepository = eventoRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.organizadorRepository = organizadorRepository;
    }

    @Transactional
    public void crearEvento(EventoCreateRequest request, Integer idUsuarioCreador) {

        if (eventoRepository.findByNombreAndFechaInicio(request.getNombre(), request.getFechaInicio()).isPresent()) {
            throw new IllegalStateException("Ya existe un evento con ese nombre en ese horario");
        }

        LocalDateTime ahora = LocalDateTime.now();
        if (!request.getFechaInicio().isAfter(ahora)) {
            throw new IllegalArgumentException("La fecha de inicio debe ser posterior a la fecha y hora actual");
        }
        if (!request.getFechaFin().isAfter(request.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        if (request.getCapacidadMaxima() == null || request.getCapacidadMaxima() <= 0) {
            throw new IllegalArgumentException("La capacidad máxima debe ser un número mayor a cero");
        }

        if (request.getTiempoCancelacionHoras() == null || request.getTiempoCancelacionHoras() <= 0) {
            throw new IllegalArgumentException("El tiempo de aceptación de cancelación debe ser mayor a cero");
        }
        long horasDisponibles = Duration.between(ahora, request.getFechaInicio()).toHours();
        if (request.getTiempoCancelacionHoras() > horasDisponibles) {
            throw new IllegalArgumentException("El tiempo de aceptación de cancelación no puede ser mayor al tiempo disponible antes del evento");
        }

        if (request.getTiempoToleranciaMinutos() == null || request.getTiempoToleranciaMinutos() < 0) {
            throw new IllegalArgumentException("El tiempo de tolerancia debe ser un número mayor o igual a cero");
        }

        String banner = request.getBanner();
        if (banner == null || banner.isBlank()
                || !(banner.endsWith(".jpg") || banner.endsWith(".jpeg") || banner.endsWith(".png"))) {
            throw new IllegalArgumentException("El banner del evento debe ser una imagen válida");
        }

        Categoria categoria = categoriaRepository.findById(request.getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        Usuario creadoPor = usuarioRepository.findById(idUsuarioCreador)
                .orElseThrow(() -> new IllegalArgumentException("Usuario creador no encontrado"));

        Evento evento = new Evento();
        evento.setNombre(request.getNombre());
        evento.setDescripcion(request.getDescripcion());
        evento.setUbicacion(request.getUbicacion());
        evento.setCapacidadMaxima(request.getCapacidadMaxima());
        evento.setFechaInicio(request.getFechaInicio());
        evento.setFechaFin(request.getFechaFin());
        evento.setTiempoCancelacionHoras(request.getTiempoCancelacionHoras());
        evento.setTiempoToleranciaMinutos(request.getTiempoToleranciaMinutos());
        evento.setBanner(request.getBanner());
        evento.setEstado("ACTIVO");
        evento.setCategoria(categoria);
        evento.setCreadoPor(creadoPor);
        evento.setFechaCreacion(LocalDateTime.now());

        eventoRepository.save(evento);
    }
}

