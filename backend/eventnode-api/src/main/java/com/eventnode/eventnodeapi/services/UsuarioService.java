package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.dtos.AdminRegistroRequest;
import com.eventnode.eventnodeapi.dtos.PerfilResponse;
import com.eventnode.eventnodeapi.models.Administrador;
import com.eventnode.eventnodeapi.models.Alumno;
import com.eventnode.eventnodeapi.models.Rol;
import com.eventnode.eventnodeapi.models.Usuario;
import com.eventnode.eventnodeapi.repositories.AdministradorRepository;
import com.eventnode.eventnodeapi.repositories.AlumnoRepository;
import com.eventnode.eventnodeapi.repositories.RolRepository;
import com.eventnode.eventnodeapi.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final AlumnoRepository alumnoRepository;
    private final AdministradorRepository administradorRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          AlumnoRepository alumnoRepository,
                          AdministradorRepository administradorRepository,
                          RolRepository rolRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.alumnoRepository = alumnoRepository;
        this.administradorRepository = administradorRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<PerfilResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> obtenerPerfil(usuario.getIdUsuario()))
                .collect(Collectors.toList());
    }

    public PerfilResponse obtenerPerfil(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        PerfilResponse perfil = new PerfilResponse();
        perfil.setIdUsuario(usuario.getIdUsuario());
        perfil.setNombre(usuario.getNombre());
        perfil.setApellidoPaterno(usuario.getApellidoPaterno());
        perfil.setApellidoMaterno(usuario.getApellidoMaterno());
        perfil.setCorreo(usuario.getCorreo());
        perfil.setEstado(usuario.getEstado());

        String rolNombre = usuario.getRol() != null ? usuario.getRol().getNombre() : null;
        perfil.setRol(rolNombre);

        // Si es ALUMNO, cargar datos del alumno
        if ("ALUMNO".equals(rolNombre)) {
            Optional<Alumno> alumnoOpt = alumnoRepository.findById(idUsuario);
            alumnoOpt.ifPresent(alumno -> {
                perfil.setMatricula(alumno.getMatricula());
                perfil.setFechaNacimiento(alumno.getFechaNac());
                perfil.setEdad(alumno.getEdad());
                perfil.setSexo(alumno.getSexo());
                perfil.setCuatrimestre(alumno.getCuatrimestre());
            });
        }

        // Si es ADMINISTRADOR o SUPERADMIN, cargar datos del admin
        if ("ADMINISTRADOR".equals(rolNombre) || "SUPERADMIN".equals(rolNombre)) {
            Optional<Administrador> adminOpt = administradorRepository.findById(idUsuario);
            adminOpt.ifPresent(admin -> {
                perfil.setEsPrincipal(admin.getEsPrincipal());
            });
        }

        return perfil;
    }

    @Transactional
    public PerfilResponse registrarAdmin(AdminRegistroRequest request) {
        // 1. Validar que el solicitante existe y es SUPERADMIN
        Usuario solicitante = usuarioRepository.findById(request.getIdSolicitante())
                .orElseThrow(() -> new IllegalArgumentException("Solicitante no encontrado"));

        String rolSolicitante = solicitante.getRol() != null ? solicitante.getRol().getNombre() : null;
        if (!"SUPERADMIN".equals(rolSolicitante)) {
            throw new SecurityException("Solo el Super Administrador puede crear administradores");
        }

        // 2. Verificar que no exista un correo duplicado
        if (usuarioRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un usuario con ese correo electrónico");
        }

        // 3. Validar formato de contraseña (mínimo 8 caracteres, al menos 1 mayúscula, 1 número, 1 especial)
        if (!request.getPassword().matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres, una mayúscula, un número y un carácter especial");
        }

        // 4. Buscar rol ADMINISTRADOR (nunca SUPERADMIN)
        Rol rolAdmin = rolRepository.findByNombre("ADMINISTRADOR")
                .orElseThrow(() -> new IllegalStateException("Rol ADMINISTRADOR no encontrado en la base de datos"));

        // 5. Crear usuario
        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre());
        usuario.setApellidoPaterno(request.getApellidoPaterno());
        usuario.setApellidoMaterno(request.getApellidoMaterno());
        usuario.setCorreo(request.getCorreo());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setEstado("ACTIVO");
        usuario.setIntentosFallidos(0);
        usuario.setRol(rolAdmin);
        usuario.setFechaCreacion(LocalDateTime.now());

        Usuario savedUsuario = usuarioRepository.save(usuario);

        // 6. Crear registro en administradores (es_principal = false siempre)
        Administrador admin = new Administrador();
        admin.setUsuario(savedUsuario);
        admin.setIdUsuario(savedUsuario.getIdUsuario());
        admin.setEsPrincipal(false);
        administradorRepository.save(admin);

        // 7. Devolver perfil completo
        return obtenerPerfil(savedUsuario.getIdUsuario());
    }
}
