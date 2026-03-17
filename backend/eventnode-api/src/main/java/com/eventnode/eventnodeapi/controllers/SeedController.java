package com.eventnode.eventnodeapi.controllers;

import com.eventnode.eventnodeapi.models.Administrador;
import com.eventnode.eventnodeapi.models.Rol;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.repositories.AdministradorRepository;
import com.eventnode.eventnodeapi.repositories.RolRepository;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/seed")
public class SeedController {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;

    public SeedController(RolRepository rolRepository,
                          UsuarioRepository usuarioRepository,
                          AdministradorRepository administradorRepository,
                          PasswordEncoder passwordEncoder) {
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.administradorRepository = administradorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/init")
    @Transactional
    public ResponseEntity<Map<String, String>> seedInitialData() {
        Map<String, String> result = new HashMap<>();

        // Crear roles si no existen
        createRolIfNotExists("ALUMNO");
        createRolIfNotExists("ADMINISTRADOR");
        createRolIfNotExists("SUPERADMIN");

        // Crear SuperAdmin maestro si no existe
        if (usuarioRepository.findByCorreo("admin@eventnode.com").isEmpty()) {
            Rol rolSuperAdmin = rolRepository.findByNombre("SUPERADMIN")
                    .orElseThrow(() -> new IllegalStateException("Rol SUPERADMIN no encontrado"));

            Usuario admin = new Usuario();
            admin.setNombre("Admin");
            admin.setApellidoPaterno("EventNode");
            admin.setApellidoMaterno("Principal");
            admin.setCorreo("admin@eventnode.com");
            admin.setPassword(passwordEncoder.encode("Admin@1234"));
            admin.setEstado("ACTIVO");
            admin.setIntentosFallidos(0);
            admin.setRol(rolSuperAdmin);
            admin.setFechaCreacion(LocalDateTime.now());

            Usuario saved = usuarioRepository.save(admin);

            Administrador administrador = new Administrador();
            administrador.setUsuario(saved);
            administrador.setEsPrincipal(true);
            administradorRepository.save(administrador);

            result.put("mensaje", "Datos iniciales creados exitosamente. SuperAdmin: admin@eventnode.com / Admin@1234");
        } else {
            result.put("mensaje", "Los datos iniciales ya existen");
        }

        return ResponseEntity.ok(result);
    }

    private void createRolIfNotExists(String nombre) {
        if (rolRepository.findByNombre(nombre).isEmpty()) {
            Rol rol = new Rol();
            rol.setNombre(nombre);
            rolRepository.save(rol);
        }
    }
}
