package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioSolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("ServicioSolicitudAUnaPublicacion")
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
    public SolicitudAUnaPublicacion traerSolicitudPorId(Long solicitudId) {
        return repositorioSolicitud.traerSolicitudPorId(solicitudId);
    }

    @Override
    public List<SolicitudAUnaPublicacion> traerSolicitudesPendientesDelUsuario(Usuario dueno) {
        List<SolicitudAUnaPublicacion> solicitudes = repositorioSolicitud.traerTodasLasSolicitudes();

        List<SolicitudAUnaPublicacion> solicitudesPorUsuario = solicitudes.stream()
                .filter(solicitud -> solicitud.getMascotaDonante().getDuenio().getId() == dueno.getId())
                .filter(SolicitudAUnaPublicacion::getPendiente)
                .collect(Collectors.toList());

        return solicitudesPorUsuario;
    }

    @Override
    public List<SolicitudAUnaPublicacion> traerSolicitudesAceptadasDelUsuario(Usuario dueno) {
        List<SolicitudAUnaPublicacion> solicitudes = repositorioSolicitud.traerTodasLasSolicitudes();

        List<SolicitudAUnaPublicacion> solicitudesPorUsuario = solicitudes.stream()
                .filter(solicitud -> solicitud.getMascotaDonante().getDuenio().getId() == dueno.getId())
                .filter(SolicitudAUnaPublicacion::getAprobada)
                .filter(solicitud -> !solicitud.getVista())
                .collect(Collectors.toList());

        return solicitudesPorUsuario;
    }

    @Override
    public List<SolicitudAUnaPublicacion> traerSolicitudesRechazadasDelUsuario(Usuario dueno) {
        List<SolicitudAUnaPublicacion> solicitudes = repositorioSolicitud.traerTodasLasSolicitudes();

        List<SolicitudAUnaPublicacion> solicitudesPorUsuario = solicitudes.stream()
                .filter(solicitud -> solicitud.getMascotaDonante().getDuenio().getId() == dueno.getId())
                .filter(SolicitudAUnaPublicacion::getRechazada)
                .filter(solicitud -> !solicitud.getVista())
                .collect(Collectors.toList());

        return solicitudesPorUsuario;
    }

    @Override
    public void marcarComoVista(Long solicitud) {
        repositorioSolicitud.marcarComoVista(solicitud);
    }

    @Override
    public void asignarVeterinario(Long solicitud) {
        Usuario vet = repositorioSolicitud.traerVeterinario();
        repositorioSolicitud.asignarVeterinario(vet, traerSolicitudPorId(solicitud));
    }

    @Override
    public void aceptarSolicitud(Long solicitud) {
        repositorioSolicitud.aceptarSolicitud(solicitud);
    }

    @Override
    public void rechazarSolicitud(Long solictud) {
        repositorioSolicitud.rechzarSolicitud(solictud);
    }
}