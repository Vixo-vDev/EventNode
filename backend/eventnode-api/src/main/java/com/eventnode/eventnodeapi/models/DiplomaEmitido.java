package com.eventnode.eventnodeapi.models;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.time.LocalDateTime;

@Entity
@Table(name = "diplomas_emitidos", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_diploma", "id_usuario"})
})
public class DiplomaEmitido implements Persistable<Integer> {

    @Transient
    private boolean isNew = true;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emitido")
    private Integer idEmitido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_diploma", nullable = false)
    private Diploma diploma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "estado_envio", columnDefinition = "ENUM('ENVIADO','ERROR')")
    private String estadoEnvio;

    public DiplomaEmitido() {
    }

    public Integer getIdEmitido() {
        return idEmitido;
    }

    public void setIdEmitido(Integer idEmitido) {
        this.idEmitido = idEmitido;
    }

    public Diploma getDiploma() {
        return diploma;
    }

    public void setDiploma(Diploma diploma) {
        this.diploma = diploma;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getEstadoEnvio() {
        return estadoEnvio;
    }

    public void setEstadoEnvio(String estadoEnvio) {
        this.estadoEnvio = estadoEnvio;
    }

    @Override
    public Integer getId() {
        return idEmitido;
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
