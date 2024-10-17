package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioSolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioMascota")
public class RepositorioSolicitudAUnaPublicacionImpl implements RepositorioSolicitudAUnaPublicacion {
    private final SessionFactory sessionFactory;

    @Autowired
    public RepositorioSolicitudAUnaPublicacionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void guardarSolicitud(SolicitudAUnaPublicacion solicitud) {
        final Session session = sessionFactory.getCurrentSession();
        session.save(solicitud);
    }
}
