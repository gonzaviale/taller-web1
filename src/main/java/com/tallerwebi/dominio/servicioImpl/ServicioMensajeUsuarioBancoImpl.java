package com.tallerwebi.dominio.servicioImpl;

import com.tallerwebi.dominio.RepositorioMensajeUsuarioBanco;
import com.tallerwebi.dominio.servicio.ServicioMensajeUsuarioBanco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import com.tallerwebi.dominio.entidad.*;

import java.util.ArrayList;

@Service("servicioMensajeUsuarioBanco")
@Transactional
public class ServicioMensajeUsuarioBancoImpl implements ServicioMensajeUsuarioBanco {

    private RepositorioMensajeUsuarioBanco repositorioMensajeUsuarioBanco;

    @Autowired
    public ServicioMensajeUsuarioBancoImpl(RepositorioMensajeUsuarioBanco repositorioMensajeUsuarioBanco) {
        this.repositorioMensajeUsuarioBanco = repositorioMensajeUsuarioBanco;
    }

    @Override
    public MensajeUsuarioBanco enviarMensaje(String mensaje, String emisor, Usuario usuario, Long banco) throws Exception {
        Banco foundBank = repositorioMensajeUsuarioBanco.searchBankById(banco);
        if (foundBank != null) {
            MensajeUsuarioBanco createdMessage = repositorioMensajeUsuarioBanco
                    .crearMensaje(mensaje, emisor, usuario, foundBank);
            if (createdMessage != null) {
                return createdMessage;
            } else{
                throw new Exception("No se pudo crear el mensaje");
            }
        } else {
            throw new Exception("El destinatario no existe");
        }
    }

    @Override
    public ArrayList<MensajeUsuarioBanco> getMessages(Usuario usuario) {
        try {
            ArrayList<MensajeUsuarioBanco> messages = new ArrayList<>();
            messages.addAll(this.repositorioMensajeUsuarioBanco.getMessagesByUser(usuario));
            return messages;
        }catch (RuntimeException e){
            return new ArrayList<MensajeUsuarioBanco>();
        }
    }

    @Override
    public ArrayList<MensajeUsuarioBanco> getMessagesByIds(Long usuarioId, Long bancoId) {
        try{
            ArrayList<MensajeUsuarioBanco> messages;
            messages = this.repositorioMensajeUsuarioBanco.getMessagesByUserAndBank(usuarioId, bancoId);
            return messages;
        } catch (RuntimeException e){
            return new ArrayList<MensajeUsuarioBanco>();
        }
    }

    @Override
    public Usuario searchUser(Long userId) {
        try {
            Usuario user = this.repositorioMensajeUsuarioBanco.searchUser(userId);
            return user;
        } catch (RuntimeException e){
            return null;
        }
    }
}
