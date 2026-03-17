package com.eventnode.eventnodeapi.controllers;

import com.eventnode.eventnodeapi.services.DiplomaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diplomas")
public class DiplomaController {

    private final DiplomaService diplomaService;

    public DiplomaController(DiplomaService diplomaService) {
        this.diplomaService = diplomaService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearDiploma(@RequestBody Map<String, String> body) {
        try {
            Integer idEvento = Integer.parseInt(body.get("idEvento"));
            String firma = body.get("firma");
            String diseno = body.get("diseno");

            if (idEvento == null || firma == null || diseno == null) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "idEvento, firma y diseno son requeridos");
                return ResponseEntity.badRequest().body(error);
            }

            diplomaService.crearDiploma(idEvento, firma, diseno);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Diploma creado exitosamente");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (IllegalStateException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", ex.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error interno");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> listarDiplomas() {
        try {
            List<Map<String, Object>> diplomas = diplomaService.listarDiplomas();
            return ResponseEntity.ok(diplomas);
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error interno");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{idDiploma}")
    public ResponseEntity<?> obtenerDiploma(@PathVariable Integer idDiploma) {
        try {
            Map<String, Object> diploma = diplomaService.obtenerDiploma(idDiploma);
            return ResponseEntity.ok(diploma);
        } catch (IllegalArgumentException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error interno");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/{idDiploma}/emitir")
    public ResponseEntity<?> emitirDiplomas(@PathVariable Integer idDiploma) {
        try {
            long count = diplomaService.emitirDiplomas(idDiploma);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Diplomas emitidos exitosamente");
            response.put("totalEmitidos", count);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error interno");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/estudiante/{idUsuario}")
    public ResponseEntity<?> listarDiplomasEstudiante(@PathVariable Integer idUsuario) {
        try {
            List<Map<String, Object>> diplomas = diplomaService.listarDiplomasEstudiante(idUsuario);
            return ResponseEntity.ok(diplomas);
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error interno");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
