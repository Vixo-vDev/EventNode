package com.eventnode.eventnodeapi.models;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Entity
@Table(name = "diplomas")
public class Diploma implements Persistable<Integer> {

    @Transient
    private boolean isNew = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_diploma")
    private Integer idDiploma;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", nullable = false, unique = true)
    private Evento evento;

    @Column(name = "nombre_evento", nullable = false, length = 200)
    private String nombreEvento;

    @Column(name = "firma", nullable = false, length = 255)
    private String firma;

    @Column(name = "diseno", nullable = false, length = 255)
    private String diseno;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "estado", columnDefinition = "ENUM('ACTIVO','ELIMINADO')")
    private String estado;

    public Diploma() {
    }

    public Integer getIdDiploma() {
        return idDiploma;
    }

    public void setIdDiploma(Integer idDiploma) {
        this.idDiploma = idDiploma;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getFirma() {
        return firma;
    }

    public void setFirma(String firma) {
        this.firma = firma;
    }

    public String getDiseno() {
        return diseno;
    }

    public void setDiseno(String diseno) {
        this.diseno = diseno;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public Integer getId() {
        return idDiploma;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }

    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }
}
