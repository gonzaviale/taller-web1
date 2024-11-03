package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;

import java.util.ArrayList;

public interface ServicioMensajeUsuarioBanco {
    public MensajeUsuarioBanco enviarMensaje(String mensaje, String emisor, Usuario usuario, Long banco) throws Exception;
    public ArrayList<MensajeUsuarioBanco> getMessages(Usuario usuario);
    public ArrayList<MensajeUsuarioBanco> getMessagesByIds(Long usuarioId, Long bancoId);
    public Usuario searchUser(Long userId);
    public ArrayList<MensajeUsuarioBanco> getMessagesBank(Long bankId);

}
