package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioSolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("repositorioSolicitudAUnaPublicacion")
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

    @Override
    public List<SolicitudAUnaPublicacion> solicitudesPendientes() {
        List<SolicitudAUnaPublicacion> solicitudes;
        solicitudes = sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAUnaPublicacion.class)
                .add(Restrictions.eq("pendiente", true))
                .list();
        return solicitudes;
    }
}
