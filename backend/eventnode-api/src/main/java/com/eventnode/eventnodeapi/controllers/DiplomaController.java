package com.eventnode.eventnodeapi.controllers;

import com.eventnode.eventnodeapi.services.DiplomaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    public ResponseEntity<?> crearDiploma(@RequestBody Map<String, Object> body) {
        try {
            Integer idEvento = body.get("idEvento") != null ? Integer.parseInt(body.get("idEvento").toString()) : null;
            String firma = body.get("firma") != null ? body.get("firma").toString() : "";
            String diseno = body.get("diseno") != null ? body.get("diseno").toString() : "Personalizado";
            String plantillaPdf = body.get("plantillaPdf") != null ? body.get("plantillaPdf").toString() : null;
            String firmaImagen = body.get("firmaImagen") != null ? body.get("firmaImagen").toString() : null;

            if (idEvento == null) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "idEvento es requerido");
                return ResponseEntity.badRequest().body(error);
            }

            if (plantillaPdf == null || plantillaPdf.isBlank()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "La plantilla PDF es requerida");
                return ResponseEntity.badRequest().body(error);
            }

            if (firmaImagen == null || firmaImagen.isBlank()) {
                Map<String, String> error = new HashMap<>();
                error.put("mensaje", "La firma es requerida");
                return ResponseEntity.badRequest().body(error);
            }

            diplomaService.crearDiploma(idEvento, firma, diseno, plantillaPdf, firmaImagen);

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
            error.put("mensaje", "Error interno: " + ex.getMessage());
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
            error.put("mensaje", "Error interno: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PutMapping("/{idDiploma}")
    public ResponseEntity<?> actualizarDiploma(@PathVariable Integer idDiploma, @RequestBody Map<String, Object> body) {
        try {
            String firma = body.get("firma") != null ? body.get("firma").toString() : null;
            String diseno = body.get("diseno") != null ? body.get("diseno").toString() : null;
            String plantillaPdf = body.get("plantillaPdf") != null ? body.get("plantillaPdf").toString() : null;
            String firmaImagen = body.get("firmaImagen") != null ? body.get("firmaImagen").toString() : null;

            diplomaService.actualizarDiploma(idDiploma, firma, diseno, plantillaPdf, firmaImagen);

            Map<String, Object> response = new HashMap<>();
            response.put("mensaje", "Diploma actualizado exitosamente. Se notificó a todos los destinatarios.");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error interno: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @DeleteMapping("/{idDiploma}")
    public ResponseEntity<?> eliminarDiploma(@PathVariable Integer idDiploma) {
        try {
            diplomaService.eliminarDiploma(idDiploma);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Diploma eliminado exitosamente");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error interno: " + ex.getMessage());
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

    @GetMapping("/{idDiploma}/descargar/{idUsuario}")
    public ResponseEntity<?> descargarDiploma(@PathVariable Integer idDiploma, @PathVariable Integer idUsuario) {
        try {
            byte[] pdfBytes = diplomaService.generarDiplomaPdf(idDiploma, idUsuario);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "diploma.pdf");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);

        } catch (IllegalArgumentException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", ex.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception ex) {
            Map<String, String> error = new HashMap<>();
            error.put("mensaje", "Error al generar diploma: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}
