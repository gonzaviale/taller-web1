package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;

import java.util.List;

public interface RepositorioSolicitud {
    Solicitud guardarSolicitud(Solicitud solicitud1);

    Solicitud buscarSolicitudPorId(int id);

    List<Solicitud> solicitudesPorBanco(Long idBanco);

    List<PaqueteDeSangre> obtenerPaquetesDeSangreCompatible(Solicitud solicitud);

    void rechazarSolicitud(int solicitudId);

    void solicitudAprobar(int solicitudId);

    Banco buscarPorId(Long bancoId);

    Entrega guardarEntrega(Entrega entrega);

    Entrega buscarEntregaPorSolicitudId(int id);

    Usuario buscarUsuarioXId(long usuarioId);
}
