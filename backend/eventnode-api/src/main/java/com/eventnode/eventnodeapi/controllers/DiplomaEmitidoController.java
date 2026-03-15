package com.eventnode.eventnodeapi.controllers;

import com.eventnode.eventnodeapi.models.DiplomaEmitido;
import com.eventnode.eventnodeapi.repositories.DiplomaEmitidoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diplomas-emitidos")
public class DiplomaEmitidoController {

    private final DiplomaEmitidoRepository diplomaEmitidoRepository;

    public DiplomaEmitidoController(DiplomaEmitidoRepository diplomaEmitidoRepository) {
        this.diplomaEmitidoRepository = diplomaEmitidoRepository;
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<DiplomaEmitido>> obtenerPorUsuario(@PathVariable Integer idUsuario) {
        List<DiplomaEmitido> emitidos = diplomaEmitidoRepository.findByUsuario_IdUsuario(idUsuario);
        if (emitidos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(emitidos);
    }
}
