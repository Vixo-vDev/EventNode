package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.dtos.AlumnoActualizarRequest;
import com.eventnode.eventnodeapi.dtos.PerfilResponse;
import com.eventnode.eventnodeapi.models.Administrador;
import com.eventnode.eventnodeapi.models.Alumno;
import com.eventnode.eventnodeapi.models.Rol;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.repositories.AdministradorRepository;
import com.eventnode.eventnodeapi.repositories.AlumnoRepository;
import com.eventnode.eventnodeapi.repositories.RolRepository;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PerfilUsuarioTest {

    private static UsuarioService instanciaUsuarioService;
    private static AlumnoService instanciaAlumnoService;

    private UsuarioRepository usuarioRepository;
    private AlumnoRepository alumnoRepository;
    private AdministradorRepository administradorRepository;
    private RolRepository rolRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Validator validator;

    private Integer idUsuario;

    private Usuario usuarioEntrada;
    private Alumno alumnoEntrada;
    private PerfilResponse perfilResultado;

    private AlumnoActualizarRequest requestActualizar;
    private String mensajeEsperado;

    @BeforeAll
    public static void iniciarInstancias() {
        instanciaUsuarioService = null;
        instanciaAlumnoService = null;
    }

    @BeforeEach
    void resetearVariables() {
        idUsuario = 1;
        mensajeEsperado = null;
        perfilResultado = null;
        requestActualizar = null;

        usuarioRepository = mock(UsuarioRepository.class);
        alumnoRepository = mock(AlumnoRepository.class);
        administradorRepository = mock(AdministradorRepository.class);
        rolRepository = mock(RolRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);

        instanciaUsuarioService = new UsuarioService(usuarioRepository, alumnoRepository, administradorRepository, rolRepository, passwordEncoder);
        instanciaAlumnoService = new AlumnoService(usuarioRepository, alumnoRepository, rolRepository, passwordEncoder);

        usuarioEntrada = new Usuario();
        usuarioEntrada.setIdUsuario(idUsuario);
        usuarioEntrada.setNombre("Juan");
        usuarioEntrada.setApellidoPaterno("Pérez");
        usuarioEntrada.setApellidoMaterno("López");
        usuarioEntrada.setCorreo("juan.perez@test.com");
        usuarioEntrada.setEstado("ACTIVO");

        Rol rol = new Rol();
        rol.setNombre("ALUMNO");
        usuarioEntrada.setRol(rol);

        alumnoEntrada = new Alumno();
        alumnoEntrada.setIdUsuario(idUsuario);
        alumnoEntrada.setMatricula("A01234567");
        alumnoEntrada.setFechaNac(LocalDate.now().minusYears(20));
        alumnoEntrada.setEdad(20);
        alumnoEntrada.setSexo("M");
        alumnoEntrada.setCuatrimestre(4);
    }

    @Test
    void visualizacionCorrectaDatosPerfilTest() {
        // Preparar datos
        when(usuarioRepository.findById(idUsuario)).thenReturn(java.util.Optional.of(usuarioEntrada));
        when(alumnoRepository.findById(idUsuario)).thenReturn(java.util.Optional.of(alumnoEntrada));

        // Invocar método
        perfilResultado = instanciaUsuarioService.obtenerPerfil(idUsuario);

        // Validar resultado con assert
        assertNotNull(perfilResultado);
        assertEquals(idUsuario, perfilResultado.getIdUsuario());
        assertEquals("Juan", perfilResultado.getNombre());
        assertEquals("Pérez", perfilResultado.getApellidoPaterno());
        assertEquals("juan.perez@test.com", perfilResultado.getCorreo());
        assertEquals("ALUMNO", perfilResultado.getRol());
        assertEquals("A01234567", perfilResultado.getMatricula());
        assertEquals(20, perfilResultado.getEdad().intValue());
        assertEquals("M", perfilResultado.getSexo());
        assertEquals(4, perfilResultado.getCuatrimestre().intValue());
    }

    @Test
    void actualizacionExitosaDatosValidosTest() {
        // Preparar datos
        requestActualizar = new AlumnoActualizarRequest();
        requestActualizar.setNombre("Juan");
        requestActualizar.setApellidoPaterno("Pérez");
        requestActualizar.setApellidoMaterno("López");
        requestActualizar.setCorreo("juan.perez@test.com");
        requestActualizar.setSexo("M");
        requestActualizar.setCuatrimestre(4);
        requestActualizar.setEdad(20);

        Set<ConstraintViolation<AlumnoActualizarRequest>> violaciones = validator.validate(requestActualizar);
        assertTrue(violaciones.isEmpty());

        when(usuarioRepository.findById(idUsuario)).thenReturn(java.util.Optional.of(usuarioEntrada));
        when(alumnoRepository.findById(idUsuario)).thenReturn(java.util.Optional.of(alumnoEntrada));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioEntrada);
        when(alumnoRepository.save(any(Alumno.class))).thenReturn(alumnoEntrada);

        boolean actualizadoSinError = false;

        // Invocar método
        try {
            instanciaAlumnoService.actualizarAlumno(idUsuario, requestActualizar);
            actualizadoSinError = true;
        } catch (Exception ex) {
            actualizadoSinError = false;
        }

        // Validar resultado
        assertTrue(actualizadoSinError);
    }

    @Test
    void rechazoActualizacionCamposVaciosTest() {
        // Preparar datos
        requestActualizar = new AlumnoActualizarRequest();
        requestActualizar.setNombre("");
        requestActualizar.setApellidoPaterno("");
        requestActualizar.setCorreo("");
        requestActualizar.setSexo("");
        requestActualizar.setCuatrimestre(4);
        requestActualizar.setEdad(20);

        // Invocar método
        Set<ConstraintViolation<AlumnoActualizarRequest>> violaciones = validator.validate(requestActualizar);

        // Validar resultado
        assertFalse(violaciones.isEmpty());
        boolean existeMensajeNombre = false;
        for (ConstraintViolation<AlumnoActualizarRequest> v : violaciones) {
            if ("El nombre es obligatorio".equals(v.getMessage())) {
                existeMensajeNombre = true;
            }
        }
        assertTrue(existeMensajeNombre);
    }

    @Test
    void validacionFormatoCorreoPerfilTest() {
        // Preparar datos
        requestActualizar = new AlumnoActualizarRequest();
        requestActualizar.setNombre("Juan");
        requestActualizar.setApellidoPaterno("Pérez");
        requestActualizar.setApellidoMaterno("López");
        requestActualizar.setCorreo("correo-invalido");
        requestActualizar.setSexo("M");
        requestActualizar.setCuatrimestre(4);
        requestActualizar.setEdad(20);

        mensajeEsperado = "Ingrese una dirección de correo electrónico válida";

        // Invocar método
        Set<ConstraintViolation<AlumnoActualizarRequest>> violaciones = validator.validate(requestActualizar);

        // Validar resultado
        assertFalse(violaciones.isEmpty());
        boolean existeMensajeCorreo = false;
        for (ConstraintViolation<AlumnoActualizarRequest> v : violaciones) {
            if (mensajeEsperado.equals(v.getMessage())) {
                existeMensajeCorreo = true;
            }
        }
        assertTrue(existeMensajeCorreo);
    }

    @Test
    void validacionEdadFueraDeRangoTest() {
        // Preparar datos
        requestActualizar = new AlumnoActualizarRequest();
        requestActualizar.setNombre("Juan");
        requestActualizar.setApellidoPaterno("Pérez");
        requestActualizar.setApellidoMaterno("López");
        requestActualizar.setCorreo("juan.perez@test.com");
        requestActualizar.setSexo("M");
        requestActualizar.setCuatrimestre(4);
        requestActualizar.setEdad(16); // fuera de rango

        Set<ConstraintViolation<AlumnoActualizarRequest>> violaciones = validator.validate(requestActualizar);
        assertTrue(violaciones.isEmpty());

        when(usuarioRepository.findById(idUsuario)).thenReturn(java.util.Optional.of(usuarioEntrada));
        when(alumnoRepository.findById(idUsuario)).thenReturn(java.util.Optional.of(alumnoEntrada));

        mensajeEsperado = "Edad fuera de rango";

        // Invocar método
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> instanciaAlumnoService.actualizarAlumno(idUsuario, requestActualizar));

        // Validar resultado
        assertEquals(mensajeEsperado, ex.getMessage());
    }
}

