package com.eventnode.eventnodeapi;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (DisabledException ex) {
            return buildError(HttpStatus.FORBIDDEN, "Cuenta inactiva, contacte al administrador");
        } catch (LockedException ex) {
            return buildError(HttpStatus.FORBIDDEN, "Cuenta bloqueada, intente nuevamente en 15 minutos");
        } catch (BadCredentialsException ex) {
            return buildError(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
        }
    }

    private ResponseEntity<Map<String, String>> buildError(HttpStatus status, String mensaje) {
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", mensaje);
        return ResponseEntity.status(status).body(body);
    }
}

