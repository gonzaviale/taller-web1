package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.List;

public interface ServicioSolicitudAUnaPublicacion {
    void guardarSolicitud(SolicitudAUnaPublicacion solicitud);
    SolicitudAUnaPublicacion traerSolicitudPorId(Long solicitudId);
    List<SolicitudAUnaPublicacion> traerSolicitudesPendientesDelUsuario(Usuario dueno);
    void aceptarSolicitud(Long solicitud);
    void rechazarSolicitud(Long solicitud);
    List<SolicitudAUnaPublicacion> traerSolicitudesAceptadasDelUsuario(Usuario dueno);
    List<SolicitudAUnaPublicacion> traerSolicitudesRechazadasDelUsuario(Usuario dueno);
}
