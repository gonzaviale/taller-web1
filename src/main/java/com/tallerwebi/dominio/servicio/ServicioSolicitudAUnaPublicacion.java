package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.Usuario;

import java.util.List;

public interface ServicioSolicitudAUnaPublicacion {
    void guardarSolicitud(SolicitudAUnaPublicacion solicitud);
    List<SolicitudAUnaPublicacion> traerSolicitudesPendientesDelUsuario(Mascota mascotaDonante, Usuario dueno);
}
