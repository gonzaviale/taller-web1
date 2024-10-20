package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioSolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServicioSolicitudAUnaPublicacionImpl implements ServicioSolicitudAUnaPublicacion {
    final private RepositorioSolicitudAUnaPublicacion repositorioSolicitud;

    @Autowired
    public ServicioSolicitudAUnaPublicacionImpl(RepositorioSolicitudAUnaPublicacion repositorioSolicitud) {
        this.repositorioSolicitud = repositorioSolicitud;
    }

    @Override
    public void guardarSolicitud(SolicitudAUnaPublicacion solicitud) {
        repositorioSolicitud.guardarSolicitud(solicitud);
    }

    @Override
    public List<SolicitudAUnaPublicacion> traerSolicitudesPendientesDelUsuario(Mascota mascotaDonante, Usuario dueno) {
        List<SolicitudAUnaPublicacion> solicitudes = repositorioSolicitud.solicitudesPendientes();

        List<SolicitudAUnaPublicacion> solicitudesPorUsuario = solicitudes.stream()
                .filter(solicitud -> solicitud.getMascotaDonante().getDuenio().equals(dueno))
                .collect(Collectors.toList());

        return solicitudesPorUsuario;
    }
}