package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.BancoNoEncontrado;

public interface ServicioBanco {


    Banco agregarPaqueteDeSangre(Long idBanco, PaqueteDeSangre paquete) throws BancoNoEncontrado;

    Banco BuscarBancoId(Long idBanco);
}
