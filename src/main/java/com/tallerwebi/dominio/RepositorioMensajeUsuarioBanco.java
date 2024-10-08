package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;

public interface RepositorioMensajeUsuarioBanco {
    public MensajeUsuarioBanco crearMensaje(String mensaje, String emisor, Usuario usuario, Banco banco);
    Banco searchBankById(Long banco);
}
