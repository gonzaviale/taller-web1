package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.*;

import java.util.List;

public interface ServicioSolicitud {

    List<Solicitud> obtenerSolicitudesXBanco(Long idBanco);
    Solicitud agregarSolicitud(Solicitud solicitud1);
    Solicitud buscarSolicitud(int id);
    List<PaqueteDeSangre> obtenerPaquetesDeSangreCompatibles(Solicitud solicitud);
    void rechazarSolicitud(int solicitudId);
    Entrega asignarPaqueteASolicitud(int solicitudId, long paqueteId);
    Banco obtenerBancoXId(Long bancoId);
    Usuario buscarUsuarioXId(long usuarioId);
}
