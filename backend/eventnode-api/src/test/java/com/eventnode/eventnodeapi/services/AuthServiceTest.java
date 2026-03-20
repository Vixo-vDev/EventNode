package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.dtos.LoginRequest;
import com.eventnode.eventnodeapi.dtos.LoginResponse;
import com.eventnode.eventnodeapi.models.Rol;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.repositories.AlumnoRepository;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import com.eventnode.eventnodeapi.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthServiceTest {

    private static AuthService instancia;

    private static final String CORREO_VALIDO = "test@test.com";
    private static final String PASSWORD_PLANA = "rawPassword";
    private static final String PASSWORD_CIFRADA = "$2a$10$abcdefghijklmnopqrstuvwxyz0123456789ABCDE";
    private static final String TOKEN_ESPERADO = "fake-jwt-token";

    private UsuarioRepository usuarioRepository;
    private AlumnoRepository alumnoRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    private LoginRequest datosEntrada;
    private LoginResponse resultado;
    private Usuario usuario;

    @BeforeAll
    public static void iniciarInstancia() {
        // La instancia real se crea en @BeforeEach para que use mocks frescos.
        instancia = null;
    }

    @BeforeEach
    void resetearVariables() {
        datosEntrada = null;
        resultado = null;
        usuario = null;

        usuarioRepository = Mockito.mock(UsuarioRepository.class);
        alumnoRepository = Mockito.mock(AlumnoRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        jwtTokenProvider = Mockito.mock(JwtTokenProvider.class);
        instancia = new AuthService(usuarioRepository, alumnoRepository, passwordEncoder, jwtTokenProvider);
    }

    @Test
    public void accesoExitosoCredencialesValidasTest() {
        // Preparar datos
        Rol rol = new Rol();
        rol.setNombre("ALUMNO");

        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setCorreo(CORREO_VALIDO);
        usuario.setPassword(PASSWORD_CIFRADA);
        usuario.setEstado("ACTIVO");
        usuario.setRol(rol);
        usuario.setIntentosFallidos(2);
        usuario.setBloqueadoHasta(LocalDateTime.now().plusMinutes(5));

        datosEntrada = new LoginRequest();
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_PLANA);

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(PASSWORD_PLANA, PASSWORD_CIFRADA)).thenReturn(true);
        when(jwtTokenProvider.generateToken(any(UsernamePasswordAuthenticationToken.class))).thenReturn(TOKEN_ESPERADO);
        when(alumnoRepository.findById(1)).thenReturn(Optional.empty());

        // Invocar método
        resultado = instancia.login(datosEntrada);

        // Validar resultado con assert
        assertNotNull(resultado);
        assertEquals(TOKEN_ESPERADO, resultado.getToken());
        assertEquals("ALUMNO", resultado.getRol());
        assertEquals(0, usuario.getIntentosFallidos());
        assertNull(usuario.getBloqueadoHasta());
    }

    @Test
    public void rechazoCredencialesIncorrectasTest() {
        // Preparar datos
        Rol rol = new Rol();
        rol.setNombre("ALUMNO");
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setCorreo(CORREO_VALIDO);
        usuario.setPassword(PASSWORD_CIFRADA);
        usuario.setEstado("ACTIVO");
        usuario.setRol(rol);
        usuario.setIntentosFallidos(0);

        datosEntrada = new LoginRequest();
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword("passwordIncorrecta");

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("passwordIncorrecta", PASSWORD_CIFRADA)).thenReturn(false);

        // Invocar método y validar resultado con assert
        assertThrows(BadCredentialsException.class, () -> instancia.login(datosEntrada));
        assertEquals(1, usuario.getIntentosFallidos());
    }

    @Test
    public void rechazoCuentaInactivaTest() {
        // Preparar datos
        Rol rol = new Rol();
        rol.setNombre("ALUMNO");
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setCorreo(CORREO_VALIDO);
        usuario.setPassword(PASSWORD_CIFRADA);
        usuario.setEstado("INACTIVO");
        usuario.setRol(rol);

        datosEntrada = new LoginRequest();
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_PLANA);

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.of(usuario));

        // Invocar método y validar resultado con assert
        assertThrows(DisabledException.class, () -> instancia.login(datosEntrada));
    }

    @Test
    public void bloqueoTrasTresIntentosFallidosTest() {
        // Preparar datos
        Rol rol = new Rol();
        rol.setNombre("ALUMNO");
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setCorreo(CORREO_VALIDO);
        usuario.setPassword(PASSWORD_CIFRADA);
        usuario.setEstado("ACTIVO");
        usuario.setRol(rol);
        usuario.setIntentosFallidos(0);
        usuario.setBloqueadoHasta(null);

        datosEntrada = new LoginRequest();
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword("passwordIncorrecta");

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("passwordIncorrecta", PASSWORD_CIFRADA)).thenReturn(false);

        // Invocar método (3 veces)
        assertThrows(BadCredentialsException.class, () -> instancia.login(datosEntrada));
        assertThrows(BadCredentialsException.class, () -> instancia.login(datosEntrada));
        assertThrows(BadCredentialsException.class, () -> instancia.login(datosEntrada));

        // Validar resultado con assert
        assertEquals(3, usuario.getIntentosFallidos());
        assertNotNull(usuario.getBloqueadoHasta());
    }

    @Test
    public void rechazoPorCuentaBloqueadaTest() {
        // Preparar datos
        Rol rol = new Rol();
        rol.setNombre("ALUMNO");
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setCorreo(CORREO_VALIDO);
        usuario.setPassword(PASSWORD_CIFRADA);
        usuario.setEstado("ACTIVO");
        usuario.setRol(rol);
        usuario.setBloqueadoHasta(LocalDateTime.now().plusMinutes(10));

        datosEntrada = new LoginRequest();
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_PLANA);

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.of(usuario));

        // Invocar método y validar resultado con assert
        assertThrows(LockedException.class, () -> instancia.login(datosEntrada));
    }

    @Test
    public void accesoExitosoConPasswordLegacyMigraABcryptTest() {
        // Preparar datos (password legacy sin $2)
        Rol rol = new Rol();
        rol.setNombre("ALUMNO");
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setCorreo(CORREO_VALIDO);
        usuario.setPassword(PASSWORD_PLANA);
        usuario.setEstado("ACTIVO");
        usuario.setRol(rol);

        datosEntrada = new LoginRequest();
        datosEntrada.setCorreo(CORREO_VALIDO);
        datosEntrada.setPassword(PASSWORD_PLANA);

        when(usuarioRepository.findByCorreo(CORREO_VALIDO)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(PASSWORD_PLANA)).thenReturn(PASSWORD_CIFRADA);
        when(jwtTokenProvider.generateToken(any())).thenReturn(TOKEN_ESPERADO);
        when(alumnoRepository.findById(1)).thenReturn(Optional.empty());

        // Invocar método
        resultado = instancia.login(datosEntrada);

        // Validar resultado con assert
        assertNotNull(resultado);
        assertEquals(PASSWORD_CIFRADA, usuario.getPassword());
    }
}
