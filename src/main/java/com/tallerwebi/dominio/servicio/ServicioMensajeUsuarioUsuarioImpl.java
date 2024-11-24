package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioMensajeUsuarioUsuario;
import com.tallerwebi.dominio.entidad.MensajeUsuarioUsuario;
import com.tallerwebi.dominio.entidad.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service("servicioMensajeUsuarioUsuario")
@Transactional
public class ServicioMensajeUsuarioUsuarioImpl implements ServicioMensajeUsuarioUsuario {
    private RepositorioMensajeUsuarioUsuario repositorioMensajeUsuarioUsuario;

    @Autowired
    public ServicioMensajeUsuarioUsuarioImpl(RepositorioMensajeUsuarioUsuario repositorioMensajeUsuarioUsuario)
    {this.repositorioMensajeUsuarioUsuario = repositorioMensajeUsuarioUsuario;}

    @Override
    public MensajeUsuarioUsuario enviarMensaje(String mensaje, Usuario usuarioEmisor, Usuario usuarioReceptor) throws Exception {
        return repositorioMensajeUsuarioUsuario.crearMensaje(mensaje, usuarioEmisor, usuarioReceptor);
    }

    @Override
    public ArrayList<MensajeUsuarioUsuario> getMessages(Usuario usuario) {
        ArrayList<MensajeUsuarioUsuario> listaMensajes = new ArrayList<>();
        listaMensajes.addAll(this.repositorioMensajeUsuarioUsuario.getMessagesByUser(usuario));
        return listaMensajes;
    }

    @Override
    public ArrayList<MensajeUsuarioUsuario> getMessagesByIds(Long usuarioEmisor, Long usuarioReceptor) {
        return null;
    }

    @Override
    public ArrayList<MensajeUsuarioUsuario> getMessagesByUsers(Usuario usuario1, Usuario usuario2) {
        return repositorioMensajeUsuarioUsuario.getMessagesByUserUser(usuario1, usuario2);
    }

    @Override
    public Usuario searchUser(Long userId) {
        return repositorioMensajeUsuarioUsuario.searchUser(userId);
    }

}
