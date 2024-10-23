package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;

import java.util.List;

public interface RepositorioSolicitudAUnaPublicacion {
    public void guardarSolicitud(SolicitudAUnaPublicacion solicitud);

    void aceptarSolicitud(Long solictud);

    void rechzarSolicitud(Long solicitud);

    List<SolicitudAUnaPublicacion> traerTodasLasSolicitudes();

    SolicitudAUnaPublicacion traerSolicitudPorId(Long solicitudId);
}

