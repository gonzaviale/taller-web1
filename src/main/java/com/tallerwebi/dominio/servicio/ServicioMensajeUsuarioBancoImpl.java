package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioMensajeUsuarioBanco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import com.tallerwebi.dominio.entidad.*;

@Service("servicioMensajeUsuarioBanco")
@Transactional
public class ServicioMensajeUsuarioBancoImpl implements ServicioMensajeUsuarioBanco{

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
}
