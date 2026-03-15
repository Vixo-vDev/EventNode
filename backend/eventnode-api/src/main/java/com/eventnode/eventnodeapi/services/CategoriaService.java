package com.eventnode.eventnodeapi.services;

import com.eventnode.eventnodeapi.dtos.CategoriaRequest;
import com.eventnode.eventnodeapi.models.Categoria;
import com.eventnode.eventnodeapi.repositories.CategoriaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> obtenerTodas() {
        return categoriaRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SUPERADMIN')")
    public Categoria crearCategoria(CategoriaRequest request) {
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        
        if (categoriaRepository.existsByNombre(request.getNombre())) {
            throw new IllegalStateException("Ya existe una categoría con ese nombre");
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(request.getNombre());
        return categoriaRepository.save(categoria);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SUPERADMIN')")
    public Categoria actualizarCategoria(Integer id, CategoriaRequest request) {
        if (request.getNombre() == null || request.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }

        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));

        // Si el nombre enviado es diferente al actual, verificar que no exista ya otra categoria con ese nombre
        if (!categoriaExistente.getNombre().equalsIgnoreCase(request.getNombre())) {
            if (categoriaRepository.existsByNombre(request.getNombre())) {
                throw new IllegalStateException("Ya existe otra categoría con el nombre: " + request.getNombre());
            }
        }

        categoriaExistente.setNombre(request.getNombre());
        return categoriaRepository.save(categoriaExistente);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR','SUPERADMIN')")
    public void eliminarCategoria(Integer id) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada con ID: " + id));
        
        // El delete puede fallar si existen Eventos con este id_categoria por la Foreign Key (IntegrityConstraint).
        // Por simplicidad, dejaremos que Hibernate lance la excepcion DataIntegrityViolationException
        categoriaRepository.delete(categoriaExistente);
    }
}
