package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioSolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ServicioSoicitudAUnaPublicacionImpl implements ServicioSolicitudAUnaPublicacion {
    final private RepositorioSolicitudAUnaPublicacion repositorioSolicitud;

    @Autowired
    public ServicioSoicitudAUnaPublicacionImpl(RepositorioSolicitudAUnaPublicacion repositorioSolicitud) {
        this.repositorioSolicitud = repositorioSolicitud;
    }

    @Override
    public void guardarSolicitud(SolicitudAUnaPublicacion solicitud) {
        repositorioSolicitud.guardarSolicitud(solicitud);
    }
}
