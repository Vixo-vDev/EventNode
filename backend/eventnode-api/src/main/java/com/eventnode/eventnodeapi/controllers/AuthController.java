package com.eventnode.eventnodeapi.controllers;

import com.eventnode.eventnodeapi.dtos.LoginRequest;
import com.eventnode.eventnodeapi.dtos.LoginResponse;
import com.eventnode.eventnodeapi.services.AuthService;
import com.eventnode.eventnodeapi.services.PasswordRecoveryService;
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
    private final PasswordRecoveryService passwordRecoveryService;

    public AuthController(AuthService authService, PasswordRecoveryService passwordRecoveryService) {
        this.authService = authService;
        this.passwordRecoveryService = passwordRecoveryService;
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

    @PostMapping("/recuperar/enviar-codigo")
    public ResponseEntity<?> enviarCodigo(@RequestBody Map<String, String> request) {
        try {
            String correo = request.get("correo");
            if (correo == null || correo.isBlank()) {
                return buildError(HttpStatus.BAD_REQUEST, "El correo es obligatorio");
            }
            passwordRecoveryService.enviarCodigo(correo.trim());
            Map<String, String> body = new HashMap<>();
            body.put("mensaje", "Código enviado al correo");
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("/recuperar/verificar-codigo")
    public ResponseEntity<?> verificarCodigo(@RequestBody Map<String, String> request) {
        try {
            String correo = request.get("correo");
            String codigo = request.get("codigo");
            if (correo == null || codigo == null) {
                return buildError(HttpStatus.BAD_REQUEST, "Correo y código son obligatorios");
            }
            passwordRecoveryService.verificarCodigo(correo.trim(), codigo.trim());
            Map<String, String> body = new HashMap<>();
            body.put("mensaje", "Código verificado correctamente");
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @PostMapping("/recuperar/restablecer")
    public ResponseEntity<?> restablecerPassword(@RequestBody Map<String, String> request) {
        try {
            String correo = request.get("correo");
            String codigo = request.get("codigo");
            String nuevaPassword = request.get("nuevaPassword");
            if (correo == null || codigo == null || nuevaPassword == null) {
                return buildError(HttpStatus.BAD_REQUEST, "Todos los campos son obligatorios");
            }
            passwordRecoveryService.restablecerPassword(correo.trim(), codigo.trim(), nuevaPassword);
            Map<String, String> body = new HashMap<>();
            body.put("mensaje", "Contraseña restablecida exitosamente");
            return ResponseEntity.ok(body);
        } catch (IllegalArgumentException | IllegalStateException ex) {
            return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    private ResponseEntity<Map<String, String>> buildError(HttpStatus status, String mensaje) {
        Map<String, String> body = new HashMap<>();
        body.put("mensaje", mensaje);
        return ResponseEntity.status(status).body(body);
    }
}

