package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioBanco {
    void agregarPaqueteDeSangre(PaqueteDeSangre paquete, Banco banco);
    void agregarBanco(Banco banco);
    Banco BuscarBancoId(Long idBanco);
    List<PaqueteDeSangre> obtenerPaquetesDeSangrePorBanco(Long idBanco);

}
