package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.dtos.EventoCreateRequest;
import com.eventnode.eventnodeapi.dtos.EventoUpdateRequest;
import com.eventnode.eventnodeapi.models.Categoria;
import com.eventnode.eventnodeapi.models.Evento;
import com.eventnode.eventnodeapi.models.Rol;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.repositories.CategoriaRepository;
import com.eventnode.eventnodeapi.repositories.EventoRepository;
import com.eventnode.eventnodeapi.repositories.OrganizadorRepository;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class EventoServiceTest {

    private static EventoService instancia;

    private EventoRepository eventoRepository;
    private CategoriaRepository categoriaRepository;
    private OrganizadorRepository organizadorRepository;
    private UsuarioRepository usuarioRepository;
    private EntityManager entityManager;

    private static final Integer ID_CREADOR = 1;
    private static final Integer ID_CATEGORIA = 10;

    private EventoCreateRequest datosEntradaCrear;
    private EventoUpdateRequest datosEntradaActualizar;
    private List<Evento> resultadoLista;

    private String mensajeNombreDuplicadoEsperado;
    private String mensajeFechaInicioInvalidaEsperado;
    private String mensajeCapacidadInvalidaEsperado;
    private String mensajeTiempoCancelacionInvalidoEsperado;
    private String mensajeEventoYaOcurrioEsperado;

    private Usuario creadorAdmin;
    private Categoria categoria;

    @BeforeAll
    public static void iniciarInstancia() {
        instancia = null;
    }

    @BeforeEach
    void resetearVariables() {
        datosEntradaCrear = null;
        datosEntradaActualizar = null;
        resultadoLista = null;

        eventoRepository = Mockito.mock(EventoRepository.class);
        categoriaRepository = Mockito.mock(CategoriaRepository.class);
        organizadorRepository = Mockito.mock(OrganizadorRepository.class);
        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        entityManager = Mockito.mock(EntityManager.class);
        instancia = new EventoService(eventoRepository, categoriaRepository, organizadorRepository, usuarioRepository, entityManager);

        mensajeNombreDuplicadoEsperado = "Ya existe un evento con ese nombre en ese horario";
        mensajeFechaInicioInvalidaEsperado = "La fecha de inicio debe ser posterior a la fecha y hora actual";
        mensajeCapacidadInvalidaEsperado = "La capacidad máxima debe ser un número mayor a cero";
        mensajeTiempoCancelacionInvalidoEsperado = "El tiempo de aceptación de cancelación debe ser mayor a cero";
        mensajeEventoYaOcurrioEsperado = "No se puede modificar la fecha y hora si el evento ya ocurrió";

        Rol rolAdmin = new Rol();
        rolAdmin.setNombre("ADMINISTRADOR");
        creadorAdmin = new Usuario();
        creadorAdmin.setIdUsuario(ID_CREADOR);
        creadorAdmin.setRol(rolAdmin);

        categoria = new Categoria();
        categoria.setIdCategoria(ID_CATEGORIA);
        categoria.setNombre("Tecnología");
    }

    @Test
    void creacionExitosaEventoTest() {
        // Preparar datos
        datosEntradaCrear = new EventoCreateRequest();
        datosEntradaCrear.setIdCreador(ID_CREADOR);
        datosEntradaCrear.setIdCategoria(ID_CATEGORIA);
        datosEntradaCrear.setNombre("Taller Java");
        datosEntradaCrear.setUbicacion("Aula 1");
        datosEntradaCrear.setDescripcion("Intro a Java");
        datosEntradaCrear.setCapacidadMaxima(50);
        datosEntradaCrear.setTiempoCancelacionHoras(2);
        datosEntradaCrear.setTiempoToleranciaMinutos(10);
        datosEntradaCrear.setFechaInicio(LocalDateTime.now().plusDays(2));
        datosEntradaCrear.setFechaFin(LocalDateTime.now().plusDays(2).plusHours(2));
        datosEntradaCrear.setOrganizadores(List.of());

        when(usuarioRepository.findById(ID_CREADOR)).thenReturn(Optional.of(creadorAdmin));
        when(eventoRepository.findByNombreAndFechaInicio(datosEntradaCrear.getNombre(), datosEntradaCrear.getFechaInicio()))
                .thenReturn(Optional.empty());
        when(categoriaRepository.findById(ID_CATEGORIA)).thenReturn(Optional.of(categoria));

        Evento saved = new Evento();
        saved.setIdEvento(123);
        when(eventoRepository.save(any(Evento.class))).thenReturn(saved);

        // Invocar método
        assertDoesNotThrow(() -> instancia.crearEvento(datosEntradaCrear));

        // Validar resultado con assert
        assertTrue(true);
    }

    @Test
    void rechazoPorCampoObligatorioFaltanteNuloTest() {
        // Preparar datos (caso nulo)
        datosEntradaCrear = new EventoCreateRequest();
        datosEntradaCrear.setIdCreador(ID_CREADOR);
        datosEntradaCrear.setIdCategoria(ID_CATEGORIA);
        datosEntradaCrear.setNombre("Taller Java");
        datosEntradaCrear.setUbicacion("Aula 1");
        datosEntradaCrear.setDescripcion("Intro a Java");
        datosEntradaCrear.setCapacidadMaxima(50);
        datosEntradaCrear.setTiempoCancelacionHoras(2);
        datosEntradaCrear.setTiempoToleranciaMinutos(10);
        datosEntradaCrear.setFechaInicio(null);
        datosEntradaCrear.setFechaFin(LocalDateTime.now().plusDays(2).plusHours(2));

        when(usuarioRepository.findById(ID_CREADOR)).thenReturn(Optional.of(creadorAdmin));
        when(eventoRepository.findByNombreAndFechaInicio(anyString(), any())).thenReturn(Optional.empty());

        // Invocar método y validar resultado con assert
        assertThrows(NullPointerException.class, () -> instancia.crearEvento(datosEntradaCrear));
    }

    @Test
    void rechazoFechaInicioInvalidaTest() {
        // Preparar datos
        datosEntradaCrear = new EventoCreateRequest();
        datosEntradaCrear.setIdCreador(ID_CREADOR);
        datosEntradaCrear.setIdCategoria(ID_CATEGORIA);
        datosEntradaCrear.setNombre("Taller Java");
        datosEntradaCrear.setUbicacion("Aula 1");
        datosEntradaCrear.setDescripcion("Intro a Java");
        datosEntradaCrear.setCapacidadMaxima(50);
        datosEntradaCrear.setTiempoCancelacionHoras(2);
        datosEntradaCrear.setTiempoToleranciaMinutos(10);
        datosEntradaCrear.setFechaInicio(LocalDateTime.now().minusHours(1));
        datosEntradaCrear.setFechaFin(LocalDateTime.now().plusHours(2));

        when(usuarioRepository.findById(ID_CREADOR)).thenReturn(Optional.of(creadorAdmin));
        when(eventoRepository.findByNombreAndFechaInicio(datosEntradaCrear.getNombre(), datosEntradaCrear.getFechaInicio()))
                .thenReturn(Optional.empty());

        // Invocar método
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> instancia.crearEvento(datosEntradaCrear));

        // Validar resultado con assert
        assertEquals(mensajeFechaInicioInvalidaEsperado, ex.getMessage());
    }

    @Test
    void rechazoCapacidadMaximaInvalidaTest() {
        // Preparar datos
        datosEntradaCrear = new EventoCreateRequest();
        datosEntradaCrear.setIdCreador(ID_CREADOR);
        datosEntradaCrear.setIdCategoria(ID_CATEGORIA);
        datosEntradaCrear.setNombre("Taller Java");
        datosEntradaCrear.setUbicacion("Aula 1");
        datosEntradaCrear.setDescripcion("Intro a Java");
        datosEntradaCrear.setCapacidadMaxima(0);
        datosEntradaCrear.setTiempoCancelacionHoras(2);
        datosEntradaCrear.setTiempoToleranciaMinutos(10);
        datosEntradaCrear.setFechaInicio(LocalDateTime.now().plusDays(2));
        datosEntradaCrear.setFechaFin(LocalDateTime.now().plusDays(2).plusHours(2));

        when(usuarioRepository.findById(ID_CREADOR)).thenReturn(Optional.of(creadorAdmin));
        when(eventoRepository.findByNombreAndFechaInicio(datosEntradaCrear.getNombre(), datosEntradaCrear.getFechaInicio()))
                .thenReturn(Optional.empty());

        // Invocar método
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> instancia.crearEvento(datosEntradaCrear));

        // Validar resultado con assert
        assertEquals(mensajeCapacidadInvalidaEsperado, ex.getMessage());
    }

    @Test
    void rechazoTiempoCancelacionInvalidoTest() {
        // Preparar datos
        datosEntradaCrear = new EventoCreateRequest();
        datosEntradaCrear.setIdCreador(ID_CREADOR);
        datosEntradaCrear.setIdCategoria(ID_CATEGORIA);
        datosEntradaCrear.setNombre("Taller Java");
        datosEntradaCrear.setUbicacion("Aula 1");
        datosEntradaCrear.setDescripcion("Intro a Java");
        datosEntradaCrear.setCapacidadMaxima(50);
        datosEntradaCrear.setTiempoCancelacionHoras(0);
        datosEntradaCrear.setTiempoToleranciaMinutos(10);
        datosEntradaCrear.setFechaInicio(LocalDateTime.now().plusDays(2));
        datosEntradaCrear.setFechaFin(LocalDateTime.now().plusDays(2).plusHours(2));

        when(usuarioRepository.findById(ID_CREADOR)).thenReturn(Optional.of(creadorAdmin));
        when(eventoRepository.findByNombreAndFechaInicio(datosEntradaCrear.getNombre(), datosEntradaCrear.getFechaInicio()))
                .thenReturn(Optional.empty());

        // Invocar método
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> instancia.crearEvento(datosEntradaCrear));

        // Validar resultado con assert
        assertEquals(mensajeTiempoCancelacionInvalidoEsperado, ex.getMessage());
    }

    @Test
    void rechazoNombreDuplicadoTest() {
        // Preparar datos
        datosEntradaCrear = new EventoCreateRequest();
        datosEntradaCrear.setIdCreador(ID_CREADOR);
        datosEntradaCrear.setIdCategoria(ID_CATEGORIA);
        datosEntradaCrear.setNombre("Taller Java");
        datosEntradaCrear.setUbicacion("Aula 1");
        datosEntradaCrear.setDescripcion("Intro a Java");
        datosEntradaCrear.setCapacidadMaxima(50);
        datosEntradaCrear.setTiempoCancelacionHoras(2);
        datosEntradaCrear.setTiempoToleranciaMinutos(10);
        datosEntradaCrear.setFechaInicio(LocalDateTime.now().plusDays(2));
        datosEntradaCrear.setFechaFin(LocalDateTime.now().plusDays(2).plusHours(2));

        when(usuarioRepository.findById(ID_CREADOR)).thenReturn(Optional.of(creadorAdmin));
        when(eventoRepository.findByNombreAndFechaInicio(datosEntradaCrear.getNombre(), datosEntradaCrear.getFechaInicio()))
                .thenReturn(Optional.of(new Evento()));

        // Invocar método
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> instancia.crearEvento(datosEntradaCrear));

        // Validar resultado con assert
        assertEquals(mensajeNombreDuplicadoEsperado, ex.getMessage());
    }

    @Test
    void visualizacionPorDefectoConsultarEventosTest() {
        // Preparar datos
        when(eventoRepository.findAll(any(Specification.class))).thenReturn(List.of(new Evento(), new Evento()));

        // Invocar método
        resultadoLista = instancia.consultarEventosDisponibles(null, null, null, null);

        // Validar resultado con assert
        assertNotNull(resultadoLista);
        assertEquals(2, resultadoLista.size());
    }

    @Test
    void filtradoPorNombreMesCategoriaConsultarEventosTest() {
        // Preparar datos
        when(eventoRepository.findAll(any(Specification.class))).thenReturn(List.of(new Evento()));

        // Invocar método
        resultadoLista = instancia.consultarEventosDisponibles("java", 3, 10, "ACTIVO");

        // Validar resultado con assert
        assertEquals(1, resultadoLista.size());
    }

    @Test
    void actualizacionExitosaEventoTest() {
        // Preparar datos
        Evento existente = new Evento();
        existente.setIdEvento(7);
        existente.setNombre("Taller Java");
        existente.setDescripcion("Desc");
        existente.setUbicacion("Aula");
        existente.setCapacidadMaxima(10);
        existente.setFechaInicio(LocalDateTime.now().plusDays(5));
        existente.setFechaFin(LocalDateTime.now().plusDays(5).plusHours(1));
        existente.setTiempoCancelacionHoras(1);
        existente.setTiempoToleranciaMinutos(0);
        when(eventoRepository.findById(7)).thenReturn(Optional.of(existente));
        when(eventoRepository.existsByNombreAndFechaInicioAndIdEventoNot(anyString(), any(), eq(7))).thenReturn(false);

        datosEntradaActualizar = new EventoUpdateRequest();
        datosEntradaActualizar.setDescripcion("Nueva descripción");
        datosEntradaActualizar.setCapacidadMaxima(20);

        // Invocar método
        assertDoesNotThrow(() -> instancia.actualizarEvento(7, datosEntradaActualizar));

        // Validar resultado con assert
        assertTrue(true);
    }

    @Test
    void rechazoCampoVacioActualizarEventoTest() {
        // Preparar datos
        Evento existente = new Evento();
        existente.setIdEvento(7);
        existente.setNombre("Taller Java");
        existente.setFechaInicio(LocalDateTime.now().plusDays(5));
        existente.setFechaFin(LocalDateTime.now().plusDays(5).plusHours(1));
        when(eventoRepository.findById(7)).thenReturn(Optional.of(existente));

        datosEntradaActualizar = new EventoUpdateRequest();
        datosEntradaActualizar.setNombre("   ");

        // Invocar método
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> instancia.actualizarEvento(7, datosEntradaActualizar));

        // Validar resultado con assert
        assertEquals("El nombre del evento no puede quedar vacío", ex.getMessage());
    }

    @Test
    void rechazoEventoYaOcurrioActualizarFechasTest() {
        // Preparar datos
        Evento existente = new Evento();
        existente.setIdEvento(7);
        existente.setNombre("Evento pasado");
        existente.setFechaInicio(LocalDateTime.now().minusDays(3));
        existente.setFechaFin(LocalDateTime.now().minusDays(2));
        when(eventoRepository.findById(7)).thenReturn(Optional.of(existente));

        datosEntradaActualizar = new EventoUpdateRequest();
        datosEntradaActualizar.setFechaInicio(LocalDateTime.now().plusDays(1));

        // Invocar método
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> instancia.actualizarEvento(7, datosEntradaActualizar));

        // Validar resultado con assert
        assertEquals(mensajeEventoYaOcurrioEsperado, ex.getMessage());
    }

    @Test
    void rechazoCapacidadInvalidaActualizarEventoTest() {
        // Preparar datos
        Evento existente = new Evento();
        existente.setIdEvento(7);
        existente.setNombre("Taller");
        existente.setFechaInicio(LocalDateTime.now().plusDays(3));
        existente.setFechaFin(LocalDateTime.now().plusDays(3).plusHours(1));
        when(eventoRepository.findById(7)).thenReturn(Optional.of(existente));

        datosEntradaActualizar = new EventoUpdateRequest();
        datosEntradaActualizar.setCapacidadMaxima(0);

        // Invocar método
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> instancia.actualizarEvento(7, datosEntradaActualizar));

        // Validar resultado con assert
        assertEquals(mensajeCapacidadInvalidaEsperado, ex.getMessage());
    }

    @Test
    void rechazoNombreDuplicadoActualizarEventoTest() {
        // Preparar datos
        Evento existente = new Evento();
        existente.setIdEvento(7);
        existente.setNombre("Taller");
        existente.setFechaInicio(LocalDateTime.now().plusDays(3));
        existente.setFechaFin(LocalDateTime.now().plusDays(3).plusHours(1));
        when(eventoRepository.findById(7)).thenReturn(Optional.of(existente));

        datosEntradaActualizar = new EventoUpdateRequest();
        datosEntradaActualizar.setNombre("Nuevo nombre");
        datosEntradaActualizar.setFechaInicio(existente.getFechaInicio());
        when(eventoRepository.existsByNombreAndFechaInicioAndIdEventoNot(eq("Nuevo nombre"), eq(existente.getFechaInicio()), eq(7)))
                .thenReturn(true);

        // Invocar método
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> instancia.actualizarEvento(7, datosEntradaActualizar));

        // Validar resultado con assert
        assertEquals(mensajeNombreDuplicadoEsperado, ex.getMessage());
    }

    @Test
    void cancelacionExitosaEventoTest() {
        // Preparar datos
        Evento existente = new Evento();
        existente.setIdEvento(9);
        existente.setEstado("ACTIVO");
        when(eventoRepository.findById(9)).thenReturn(Optional.of(existente));

        // Invocar método
        assertDoesNotThrow(() -> instancia.cancelarEvento(9));

        // Validar resultado con assert
        assertEquals("CANCELADO", existente.getEstado());
        verify(eventoRepository).save(existente);
    }
}

