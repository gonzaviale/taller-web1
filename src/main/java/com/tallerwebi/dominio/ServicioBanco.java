package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.BancoNoEncontrado;

public interface ServicioBanco {


    PaqueteDeSangre agregarPaqueteDeSangre(int idBanco, PaqueteDeSangre paquete) throws BancoNoEncontrado;
}
