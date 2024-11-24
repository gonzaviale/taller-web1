package com.tallerwebi.presentacion.DTO;

import java.lang.Long;

public class ChatMessageUsersDTO {
    private Long usuarioEmisor;
    private Long UsuarioReceptor;
    private String mensaje;


    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Long getUsuarioReceptor() {
        return UsuarioReceptor;
    }

    public void setUsuarioReceptor(Long usuarioReceptor) {
        UsuarioReceptor = usuarioReceptor;
    }

    public Long getUsuarioEmisor() {
        return usuarioEmisor;
    }

    public void setUsuarioEmisor(Long usuarioEmisor) {
        this.usuarioEmisor = usuarioEmisor;
    }


}
