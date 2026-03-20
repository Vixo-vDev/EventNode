package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.models.Alumno;
import com.eventnode.eventnodeapi.models.Evento;
import com.eventnode.eventnodeapi.models.PreCheckin;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.repositories.AlumnoRepository;
import com.eventnode.eventnodeapi.repositories.EventoRepository;
import com.eventnode.eventnodeapi.repositories.PreCheckinRepository;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
class PreCheckinServiceTest {

    private static PreCheckinService instancia;

    private PreCheckinRepository preCheckinRepository;
    private EventoRepository eventoRepository;
    private UsuarioRepository usuarioRepository;
    private AlumnoRepository alumnoRepository;

    private Integer idUsuario;
    private Integer idEvento;

    private Usuario usuarioEntrada;
    private Alumno alumnoEntrada;
    private Evento eventoEntrada;

    private PreCheckin preCheckinEntrada;

    private long capacidadInscritos;
    private boolean resultadoGuardadoEsperado;
    private String mensajeEsperado;

    @BeforeAll
    public static void iniciarInstancia() {
        instancia = null;
    }

    @BeforeEach
    void resetearVariables() {
        idUsuario = 1;
        idEvento = 10;

        usuarioEntrada = new Usuario();
        usuarioEntrada.setIdUsuario(idUsuario);
        usuarioEntrada.setCorreo("alumno@test.com");

        alumnoEntrada = new Alumno();
        alumnoEntrada.setIdUsuario(idUsuario);
        alumnoEntrada.setMatricula("A01234567");

        eventoEntrada = new Evento();
        eventoEntrada.setIdEvento(idEvento);
        eventoEntrada.setEstado("ACTIVO");
        eventoEntrada.setCapacidadMaxima(5);
        eventoEntrada.setTiempoCancelacionHoras(3);
        eventoEntrada.setFechaInicio(LocalDateTime.now().plusHours(5));

        preCheckinEntrada = new PreCheckin();
        preCheckinEntrada.setIdPrecheckin(100);
        preCheckinEntrada.setIdUsuario(idUsuario);
        preCheckinEntrada.setIdEvento(idEvento);
        preCheckinEntrada.setEstado("ACTIVO");

        capacidadInscritos = 0;
        resultadoGuardadoEsperado = false;
        mensajeEsperado = null;

        preCheckinRepository = mock(PreCheckinRepository.class);
        eventoRepository = mock(EventoRepository.class);
        usuarioRepository = mock(UsuarioRepository.class);
        alumnoRepository = mock(AlumnoRepository.class);

        instancia = new PreCheckinService(preCheckinRepository, eventoRepository, usuarioRepository, alumnoRepository);
    }

    @Test
    void preCheckInExitosoConCupoDisponibleTest() {
        // Preparar datos
        capacidadInscritos = 2;
        int capacidadMaxima = eventoEntrada.getCapacidadMaxima();
        assertTrue(capacidadInscritos < capacidadMaxima);

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuarioEntrada));
        when(alumnoRepository.findById(idUsuario)).thenReturn(Optional.of(alumnoEntrada));
        when(eventoRepository.findById(idEvento)).thenReturn(Optional.of(eventoEntrada));
        when(preCheckinRepository.countByIdEventoAndEstado(idEvento, "ACTIVO")).thenReturn(capacidadInscritos);
        when(preCheckinRepository.findByIdUsuarioAndIdEvento(idUsuario, idEvento)).thenReturn(Optional.empty());
        when(preCheckinRepository.save(any(PreCheckin.class))).thenAnswer(invocation -> {
            resultadoGuardadoEsperado = true;
            return invocation.getArgument(0);
        });

        // Invocar método
        instancia.inscribirse(idUsuario, idEvento);

        // Validar resultado
        assertTrue(resultadoGuardadoEsperado);
    }

    @Test
    void rechazoEventoEstaLlenoTest() {
        // Preparar datos
        capacidadInscritos = eventoEntrada.getCapacidadMaxima();
        mensajeEsperado = "El evento está lleno";

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuarioEntrada));
        when(alumnoRepository.findById(idUsuario)).thenReturn(Optional.of(alumnoEntrada));
        when(eventoRepository.findById(idEvento)).thenReturn(Optional.of(eventoEntrada));
        when(preCheckinRepository.countByIdEventoAndEstado(idEvento, "ACTIVO")).thenReturn(capacidadInscritos);
        when(preCheckinRepository.findByIdUsuarioAndIdEvento(idUsuario, idEvento)).thenReturn(Optional.empty());

        // Invocar método y validar con assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> instancia.inscribirse(idUsuario, idEvento));
        assertEquals(mensajeEsperado, ex.getMessage());
        assertFalse(resultadoGuardadoEsperado);
    }

    @Test
    void rechazoEventoYaInicioTest() {
        // Preparar datos
        mensajeEsperado = "El pre-check-in ya no está disponible, el evento ya ha iniciado";
        eventoEntrada.setFechaInicio(LocalDateTime.now().minusMinutes(1));

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuarioEntrada));
        when(alumnoRepository.findById(idUsuario)).thenReturn(Optional.of(alumnoEntrada));
        when(eventoRepository.findById(idEvento)).thenReturn(Optional.of(eventoEntrada));

        // Invocar método y validar con assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> instancia.inscribirse(idUsuario, idEvento));
        assertEquals(mensajeEsperado, ex.getMessage());
    }

    @Test
    void rechazoPreCheckInDuplicadoTest() {
        // Preparar datos
        mensajeEsperado = "Ya cuentas con un lugar en este evento";
        capacidadInscritos = 0;
        preCheckinEntrada.setEstado("ACTIVO");

        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuarioEntrada));
        when(alumnoRepository.findById(idUsuario)).thenReturn(Optional.of(alumnoEntrada));
        when(eventoRepository.findById(idEvento)).thenReturn(Optional.of(eventoEntrada));
        when(preCheckinRepository.countByIdEventoAndEstado(idEvento, "ACTIVO")).thenReturn(capacidadInscritos);
        when(preCheckinRepository.findByIdUsuarioAndIdEvento(idUsuario, idEvento)).thenReturn(Optional.of(preCheckinEntrada));

        // Invocar método y validar con assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> instancia.inscribirse(idUsuario, idEvento));
        assertEquals(mensajeEsperado, ex.getMessage());
    }

    @Test
    void cancelacionInscripcionExitosaDentroDelTiempoPermitidoTest() {
        // Preparar datos
        mensajeEsperado = "OK";
        resultadoGuardadoEsperado = false;

        eventoEntrada.setFechaInicio(LocalDateTime.now().plusHours(10));
        eventoEntrada.setTiempoCancelacionHoras(3);

        when(preCheckinRepository.findByIdUsuarioAndIdEvento(idUsuario, idEvento)).thenReturn(Optional.of(preCheckinEntrada));
        when(eventoRepository.findById(idEvento)).thenReturn(Optional.of(eventoEntrada));
        when(preCheckinRepository.save(any(PreCheckin.class))).thenAnswer(invocation -> {
            PreCheckin guardado = invocation.getArgument(0);
            if ("CANCELADO".equals(guardado.getEstado())) {
                resultadoGuardadoEsperado = true;
            }
            return invocation.getArgument(0);
        });

        // Invocar método
        instancia.cancelarInscripcion(idUsuario, idEvento);

        // Validar resultado
        assertEquals("OK", mensajeEsperado);
        assertTrue(resultadoGuardadoEsperado);
    }

    @Test
    void rechazoCancelacionFueraDelTiempoPermitidoTest() {
        // Preparar datos
        mensajeEsperado = "Ya no es posible cancelar la inscripción. El tiempo límite ha expirado";

        eventoEntrada.setFechaInicio(LocalDateTime.now().plusHours(2));
        eventoEntrada.setTiempoCancelacionHoras(3);

        when(preCheckinRepository.findByIdUsuarioAndIdEvento(idUsuario, idEvento)).thenReturn(Optional.of(preCheckinEntrada));
        when(eventoRepository.findById(idEvento)).thenReturn(Optional.of(eventoEntrada));

        // Invocar método y validar con assert
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> instancia.cancelarInscripcion(idUsuario, idEvento));
        assertEquals(mensajeEsperado, ex.getMessage());
        assertFalse(resultadoGuardadoEsperado);
    }
}

