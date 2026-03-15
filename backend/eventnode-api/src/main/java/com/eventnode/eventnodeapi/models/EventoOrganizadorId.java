package com.eventnode.eventnodeapi.models;

import java.io.Serializable;
import java.util.Objects;

public class EventoOrganizadorId implements Serializable {

    private Integer idEvento;
    private Integer idOrganizador;

    public EventoOrganizadorId() {
    }

    public EventoOrganizadorId(Integer idEvento, Integer idOrganizador) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventoOrganizadorId that = (EventoOrganizadorId) o;
        return Objects.equals(idEvento, that.idEvento) &&
               Objects.equals(idOrganizador, that.idOrganizador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEvento, idOrganizador);
    }
}
