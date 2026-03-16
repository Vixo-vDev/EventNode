package com.eventnode.eventnodeapi.dtos;

public class LoginResponse {

    private String mensaje;
    private String rol;
    private Integer idUsuario;
    private String nombre;
    private String correo;

    public LoginResponse() {
    }

    public LoginResponse(String mensaje, String rol, Integer idUsuario, String nombre, String correo) {
        this.mensaje = mensaje;
        this.rol = rol;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
    }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}

