package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;

import java.util.List;

public interface ServicioSolicitud {

    List<Solicitud> obtenerSolicitudesXBanco(Long idBanco);
    Solicitud agregarSolicitud(Solicitud solicitud1);
    Solicitud buscarSolicitud(int id);
    List<PaqueteDeSangre> obtenerPaquetesDeSangreCompatibles(Solicitud solicitud);
    void rechazarSolicitud(int solicitudId);
    void asignarPaqueteASolicitud(int solicitudId, long paqueteId);
    Banco obtenerBancoXId(Long bancoId);
}
