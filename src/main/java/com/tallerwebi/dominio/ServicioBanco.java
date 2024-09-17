package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.BancoNoEncontrado;

public interface ServicioBanco {


    void agregarPaqueteDeSangre(Long idBanco, PaqueteDeSangre paquete) throws BancoNoEncontrado;
}
