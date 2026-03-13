package com.eventnode.eventnodeapi.dtos;

public class LoginResponse {

    private String mensaje;
    private String rol;
    private Integer idUsuario;

    public LoginResponse() {
    }

    public LoginResponse(String mensaje, String rol, Integer idUsuario) {
        this.mensaje = mensaje;
        this.rol = rol;
        this.idUsuario = idUsuario;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
}

