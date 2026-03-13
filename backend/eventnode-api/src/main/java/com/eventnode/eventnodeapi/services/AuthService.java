package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.dtos.LoginRequest;
import com.eventnode.eventnodeapi.dtos.LoginResponse;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public LoginResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas"));

        LocalDateTime ahora = LocalDateTime.now();

        if (usuario.getBloqueadoHasta() != null && usuario.getBloqueadoHasta().isAfter(ahora)) {
            throw new LockedException("Cuenta bloqueada, intente más tarde");
        }

        if (!"ACTIVO".equalsIgnoreCase(usuario.getEstado())) {
            throw new DisabledException("Cuenta inactiva, contacte al administrador");
        }

        String passwordIngresada = request.getPassword();
        String passwordAlmacenada = usuario.getPassword();

        if (!passwordAlmacenada.equals(passwordIngresada)) {
            int intentos = usuario.getIntentosFallidos() == null ? 0 : usuario.getIntentosFallidos();
            intentos++;
            usuario.setIntentosFallidos(intentos);

            if (intentos >= 3) {
                usuario.setBloqueadoHasta(ahora.plusMinutes(15));
            }

            usuarioRepository.save(usuario);
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        usuario.setIntentosFallidos(0);
        usuario.setBloqueadoHasta(null);
        usuarioRepository.save(usuario);

        String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombre() : null;

        return new LoginResponse(
                "Inicio de sesión exitoso",
                rolNombre,
                usuario.getIdUsuario()
        );
    }
}

