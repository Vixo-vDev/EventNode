package com.eventnode.eventnodeapi.models;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FLUJO DE DATOS (EventNode)
 * Rol del archivo: prueba comportamiento basico del modelo Alumno usado en backend.
 * Por que existe: previene regresiones en entidad que alimenta contratos de perfil y asistencia.
 */
class AlumnoTest {

    @Test
    void constructorParametrizadoYPersistable() {
        Usuario u = new Usuario();
        u.setIdUsuario(1);
        Alumno a = new Alumno(1, "M1", LocalDate.of(2000, 1, 1), 20, "M", 4, u);
        assertEquals(1, a.getId());
        assertEquals("M1", a.getMatricula());
        assertEquals(u, a.getUsuario());
    }

    @Test
    void isNewYMarkNotNew() {
        Alumno a = new Alumno();
        a.setIdUsuario(5);
        assertTrue(a.isNew());
        a.markNotNew();
        assertFalse(a.isNew());
    }
}
