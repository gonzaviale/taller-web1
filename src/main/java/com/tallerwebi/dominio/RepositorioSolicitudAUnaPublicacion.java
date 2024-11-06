package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.List;

public interface RepositorioSolicitudAUnaPublicacion {
    public void guardarSolicitud(SolicitudAUnaPublicacion solicitud);

    void aceptarSolicitud(Long solictud);

    void rechzarSolicitud(Long solicitud);

    List<SolicitudAUnaPublicacion> traerTodasLasSolicitudes();

    SolicitudAUnaPublicacion traerSolicitudPorId(Long solicitudId);

    void marcarComoVista(Long solicitud);

    Usuario traerVeterinario();

    void asignarVeterinario(Usuario vet, SolicitudAUnaPublicacion solicitud);
}

