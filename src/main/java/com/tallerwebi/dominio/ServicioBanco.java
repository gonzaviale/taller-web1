package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.BancoNoEncontrado;

import java.util.List;

public interface ServicioBanco {


    void agregarPaqueteDeSangre(PaqueteDeSangre paquete, Banco banco) throws BancoNoEncontrado;

    Banco BuscarBancoId(Long idBanco);

    List<PaqueteDeSangre> obtenerPaquetesDeSangrePorBanco(Long idBanco);


    void agregarBanco(Banco banco);
}
