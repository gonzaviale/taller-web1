package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Campana;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;

import java.util.List;

public interface ServicioBanco {
    void agregarPaqueteDeSangre(PaqueteDeSangre paquete, Banco banco);
    void agregarBanco(Banco banco);
    Banco BuscarBancoId(Long idBanco);
    List<PaqueteDeSangre> obtenerPaquetesDeSangrePorBanco(Long idBanco);
    PaqueteDeSangre BuscarPaqueteSangreXId(int paqueteId);

}
