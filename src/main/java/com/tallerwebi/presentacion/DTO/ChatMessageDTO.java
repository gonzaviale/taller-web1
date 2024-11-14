package com.tallerwebi.presentacion.DTO;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Usuario;

public class ChatMessageDTO {
    private Long usuarioId;
    private Long bancoId;
    private String mensaje;
    private Banco bankInSession;
    private Usuario userInSession;

    public Usuario getUserInSession() {
        return userInSession;
    }

    public void setUserInSession(Usuario userInSession) {
        this.userInSession = userInSession;
    }

    public Banco getBankInSession() {
        return bankInSession;
    }

    public void setBankInSession(Banco bankInSession) {
        this.bankInSession = bankInSession;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getBancoId() {
        return bancoId;
    }

    public void setBancoId(Long bancoId) {
        this.bancoId = bancoId;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
