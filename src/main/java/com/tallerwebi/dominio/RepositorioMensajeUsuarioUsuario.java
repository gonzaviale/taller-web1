package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.MensajeUsuarioUsuario;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.ArrayList;

public interface RepositorioMensajeUsuarioUsuario {
    MensajeUsuarioUsuario crearMensaje(String mensaje, Usuario usuarioEmisor, Usuario usuarioReceptor);
    ArrayList<MensajeUsuarioUsuario> getMessagesByUser(Usuario usuario);
    ArrayList<MensajeUsuarioUsuario> getMessagesByUserId(Long usuario);
    ArrayList<MensajeUsuarioUsuario> getMessagesByUserUserIds(Long usuarioEmisor, Long usuarioReceptor);
    ArrayList<MensajeUsuarioUsuario> getMessagesByUserUser(Usuario usuario1, Usuario usuario2);
    Usuario searchUser(Long usuario);
}
