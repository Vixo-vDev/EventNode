package com.eventnode.eventnodeapi.models;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "evento_organizador")
@IdClass(EventoOrganizadorId.class)
public class EventoOrganizador implements Persistable<EventoOrganizadorId> {

    @Transient
    private boolean isNew = true;

    @Id
    @Column(name = "id_evento")
    private Integer idEvento;

    @Id
    @Column(name = "id_organizador")
    private Integer idOrganizador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evento", insertable = false, updatable = false)
    private Evento evento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_organizador", insertable = false, updatable = false)
    private Organizador organizador;

    public EventoOrganizador() {
    }

    public EventoOrganizador(Integer idEvento, Integer idOrganizador) {
        this.idEvento = idEvento;
        this.idOrganizador = idOrganizador;
    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    public Integer getIdOrganizador() {
        return idOrganizador;
    }

    public void setIdOrganizador(Integer idOrganizador) {
        this.idOrganizador = idOrganizador;
    }

    public Evento getEvento() {
        return evento;
    }

    public Organizador getOrganizador() {
        return organizador;
    }

    @Override
    public EventoOrganizadorId getId() {
        return new EventoOrganizadorId(idEvento, idOrganizador);
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
