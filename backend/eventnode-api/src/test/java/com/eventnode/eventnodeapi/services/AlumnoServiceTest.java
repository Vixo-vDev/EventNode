package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.dtos.AlumnoRegistroRequest;
import com.eventnode.eventnodeapi.models.Rol;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.repositories.AlumnoRepository;
import com.eventnode.eventnodeapi.repositories.RolRepository;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AlumnoServiceTest {

    private static AlumnoService instancia;

    private UsuarioRepository usuarioRepository;
    private AlumnoRepository alumnoRepository;
    private RolRepository rolRepository;
    private PasswordEncoder passwordEncoder;

    private static final String PASSWORD_VALIDA = "Abcd1234!";
    private static final String PASSWORD_INVALIDA = "abcd";
    private static final String CORREO_VALIDO = "alumno@test.com";
    private static final String MATRICULA_VALIDA = "A01234567";

    private AlumnoRegistroRequest datosEntrada;
    private String mensajeEsperado;

    @BeforeAll
    public static void iniciarInstancia() {
        instancia = null;
    }

    @BeforeEach
    void resetearVariables() {
        datosEntrada = null;
        mensajeEsperado = null;

        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        alumnoRepository = Mockito.mock(AlumnoRepository.class);
        rolRepository = Mockito.mock(RolRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        instancia = new AlumnoService(usuarioRepository, alumnoRepository, rolRepository, passwordEncoder);
    }

    @Test
    public void creacionExitosaCuentaAlumnoTest() {
        // Preparar datos
        datosEntrada = new AlumnoRegistroRequest();
        datosEntrada.setNombre("Juan");
        datosEntrada.setApellidoPaterno("Pérez");
        datosEntrada.setApellidoMaterno("López");
        datosEntrada.setMatricula(MATRICULA_VALIDA);
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_VALIDA);
        datosEntrada.setFechaNacimiento(LocalDate.now().minusYears(20));
        datosEntrada.setSexo("M");
        datosEntrada.setCuatrimestre(4);

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.empty());
        when(alumnoRepository.findByMatricula(MATRICULA_VALIDA)).thenReturn(Optional.empty());

        Rol rol = new Rol();
        rol.setNombre("ALUMNO");
        when(rolRepository.findByNombre("ALUMNO")).thenReturn(Optional.of(rol));
        when(passwordEncoder.encode(PASSWORD_VALIDA)).thenReturn("$2a$fakehash");

        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setIdUsuario(99);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        // Invocar método
        assertDoesNotThrow(() -> instancia.registrarAlumno(datosEntrada));

        // Validar resultado con assert
        assertTrue(true);
    }

    @Test
    public void rechazoRegistroCorreoDuplicadoTest() {
        // Preparar datos
        datosEntrada = new AlumnoRegistroRequest();
        datosEntrada.setNombre("Juan");
        datosEntrada.setApellidoPaterno("Pérez");
        datosEntrada.setApellidoMaterno("López");
        datosEntrada.setMatricula(MATRICULA_VALIDA);
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_VALIDA);
        datosEntrada.setFechaNacimiento(LocalDate.now().minusYears(20));
        datosEntrada.setSexo("M");
        datosEntrada.setCuatrimestre(4);

        mensajeEsperado = "Matrícula o correo ya registrados";

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.of(new Usuario()));
        when(alumnoRepository.findByMatricula(MATRICULA_VALIDA)).thenReturn(Optional.empty());

        // Invocar método
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> instancia.registrarAlumno(datosEntrada));

        // Validar resultado con assert
        assertEquals(mensajeEsperado, ex.getMessage());
    }

    @Test
    public void rechazoRegistroMatriculaDuplicadaTest() {
        // Preparar datos
        datosEntrada = new AlumnoRegistroRequest();
        datosEntrada.setNombre("Juan");
        datosEntrada.setApellidoPaterno("Pérez");
        datosEntrada.setApellidoMaterno("López");
        datosEntrada.setMatricula(MATRICULA_VALIDA);
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_VALIDA);
        datosEntrada.setFechaNacimiento(LocalDate.now().minusYears(20));
        datosEntrada.setSexo("M");
        datosEntrada.setCuatrimestre(4);

        mensajeEsperado = "Matrícula o correo ya registrados";

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.empty());
        when(alumnoRepository.findByMatricula(MATRICULA_VALIDA))
                .thenReturn(Optional.of(Mockito.mock(com.eventnode.eventnodeapi.models.Alumno.class)));

        // Invocar método
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> instancia.registrarAlumno(datosEntrada));

        // Validar resultado con assert
        assertEquals(mensajeEsperado, ex.getMessage());
    }

    @Test
    public void rechazoPasswordInvalidaTest() {
        // Preparar datos
        datosEntrada = new AlumnoRegistroRequest();
        datosEntrada.setNombre("Juan");
        datosEntrada.setApellidoPaterno("Pérez");
        datosEntrada.setApellidoMaterno("López");
        datosEntrada.setMatricula(MATRICULA_VALIDA);
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_INVALIDA);
        datosEntrada.setFechaNacimiento(LocalDate.now().minusYears(20));
        datosEntrada.setSexo("M");
        datosEntrada.setCuatrimestre(4);

        mensajeEsperado = "La contraseña no es válida, debe tener mínimo 8 caracteres, incluir mayúsculas, minúsculas, números y un símbolo";

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.empty());
        when(alumnoRepository.findByMatricula(MATRICULA_VALIDA)).thenReturn(Optional.empty());

        // Invocar método
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> instancia.registrarAlumno(datosEntrada));

        // Validar resultado con assert
        assertEquals(mensajeEsperado, ex.getMessage());
    }

    @Test
    public void rechazoEdadFueraDeRangoTest() {
        // Preparar datos (límite inferior)
        datosEntrada = new AlumnoRegistroRequest();
        datosEntrada.setNombre("Juan");
        datosEntrada.setApellidoPaterno("Pérez");
        datosEntrada.setApellidoMaterno("López");
        datosEntrada.setMatricula(MATRICULA_VALIDA);
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_VALIDA);
        datosEntrada.setFechaNacimiento(LocalDate.now().minusYears(16));
        datosEntrada.setSexo("M");
        datosEntrada.setCuatrimestre(4);

        mensajeEsperado = "La edad ingresada no es válida para el registro académico";

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.empty());
        when(alumnoRepository.findByMatricula(MATRICULA_VALIDA)).thenReturn(Optional.empty());

        // Invocar método
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> instancia.registrarAlumno(datosEntrada));

        // Validar resultado con assert
        assertEquals(mensajeEsperado, ex.getMessage());
    }

    @Test
    public void rechazoEdadLimiteSuperiorFueraDeRangoTest() {
        // Preparar datos (límite superior)
        datosEntrada = new AlumnoRegistroRequest();
        datosEntrada.setNombre("Juan");
        datosEntrada.setApellidoPaterno("Pérez");
        datosEntrada.setApellidoMaterno("López");
        datosEntrada.setMatricula(MATRICULA_VALIDA);
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_VALIDA);
        datosEntrada.setFechaNacimiento(LocalDate.now().minusYears(100));
        datosEntrada.setSexo("M");
        datosEntrada.setCuatrimestre(4);

        mensajeEsperado = "La edad ingresada no es válida para el registro académico";

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.empty());
        when(alumnoRepository.findByMatricula(MATRICULA_VALIDA)).thenReturn(Optional.empty());

        // Invocar método
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> instancia.registrarAlumno(datosEntrada));

        // Validar resultado con assert
        assertEquals(mensajeEsperado, ex.getMessage());
    }

    @Test
    public void rechazoCuatrimestreNuloTest() {
        // Preparar datos
        datosEntrada = new AlumnoRegistroRequest();
        datosEntrada.setNombre("Juan");
        datosEntrada.setApellidoPaterno("Pérez");
        datosEntrada.setApellidoMaterno("López");
        datosEntrada.setMatricula(MATRICULA_VALIDA);
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_VALIDA);
        datosEntrada.setFechaNacimiento(LocalDate.now().minusYears(20));
        datosEntrada.setSexo("M");
        datosEntrada.setCuatrimestre(null);

        mensajeEsperado = "Cuatrimestre fuera de rango";

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.empty());
        when(alumnoRepository.findByMatricula(MATRICULA_VALIDA)).thenReturn(Optional.empty());

        // Invocar método
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> instancia.registrarAlumno(datosEntrada));

        // Validar resultado con assert
        assertEquals(mensajeEsperado, ex.getMessage());
    }

    @Test
    public void rechazoCuatrimestreFueraDeRangoTest() {
        // Preparar datos
        datosEntrada = new AlumnoRegistroRequest();
        datosEntrada.setNombre("Juan");
        datosEntrada.setApellidoPaterno("Pérez");
        datosEntrada.setApellidoMaterno("López");
        datosEntrada.setMatricula(MATRICULA_VALIDA);
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_VALIDA);
        datosEntrada.setFechaNacimiento(LocalDate.now().minusYears(20));
        datosEntrada.setSexo("M");
        datosEntrada.setCuatrimestre(0);

        mensajeEsperado = "Cuatrimestre fuera de rango";

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.empty());
        when(alumnoRepository.findByMatricula(MATRICULA_VALIDA)).thenReturn(Optional.empty());

        // Invocar método
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> instancia.registrarAlumno(datosEntrada));

        // Validar resultado con assert
        assertEquals(mensajeEsperado, ex.getMessage());
    }

    @Test
    public void rechazoCuatrimestreLimiteNoPermitidoCincoTest() {
        // Preparar datos (caso límite explícito)
        datosEntrada = new AlumnoRegistroRequest();
        datosEntrada.setNombre("Juan");
        datosEntrada.setApellidoPaterno("Pérez");
        datosEntrada.setApellidoMaterno("López");
        datosEntrada.setMatricula(MATRICULA_VALIDA);
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_VALIDA);
        datosEntrada.setFechaNacimiento(LocalDate.now().minusYears(20));
        datosEntrada.setSexo("M");
        datosEntrada.setCuatrimestre(5);

        mensajeEsperado = "Cuatrimestre fuera de rango";

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.empty());
        when(alumnoRepository.findByMatricula(MATRICULA_VALIDA)).thenReturn(Optional.empty());

        // Invocar método
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> instancia.registrarAlumno(datosEntrada));

        // Validar resultado con assert
        assertEquals(mensajeEsperado, ex.getMessage());
    }

    @Test
    public void rechazoCuatrimestreLimiteNoPermitidoDiezTest() {
        // Preparar datos (caso límite explícito)
        datosEntrada = new AlumnoRegistroRequest();
        datosEntrada.setNombre("Juan");
        datosEntrada.setApellidoPaterno("Pérez");
        datosEntrada.setApellidoMaterno("López");
        datosEntrada.setMatricula(MATRICULA_VALIDA);
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_VALIDA);
        datosEntrada.setFechaNacimiento(LocalDate.now().minusYears(20));
        datosEntrada.setSexo("M");
        datosEntrada.setCuatrimestre(10);

        mensajeEsperado = "Cuatrimestre fuera de rango";

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.empty());
        when(alumnoRepository.findByMatricula(MATRICULA_VALIDA)).thenReturn(Optional.empty());

        // Invocar método
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> instancia.registrarAlumno(datosEntrada));

        // Validar resultado con assert
        assertEquals(mensajeEsperado, ex.getMessage());
    }
}

