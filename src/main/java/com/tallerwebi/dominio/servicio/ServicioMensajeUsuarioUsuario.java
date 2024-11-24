package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.MensajeUsuarioUsuario;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.ArrayList;

public interface ServicioMensajeUsuarioUsuario {
    MensajeUsuarioUsuario enviarMensaje(String mensaje, Usuario usuarioEmisor, Usuario usuarioReceptor) throws Exception;
    ArrayList<MensajeUsuarioUsuario> getMessages(Usuario usuario);
    ArrayList<MensajeUsuarioUsuario> getMessagesByIds(Long usuarioEmisor, Long usuarioReceptor);
    ArrayList<MensajeUsuarioUsuario> getMessagesByUsers(Usuario usuario1, Usuario usuario2);
    Usuario searchUser(Long userId);
}
