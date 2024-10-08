package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;

public interface ServicioMensajeUsuarioBanco {
    public MensajeUsuarioBanco enviarMensaje(String mensaje, String emisor, Usuario usuario, Long banco) throws Exception;
}
