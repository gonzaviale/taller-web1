package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioSolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

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
    public List<SolicitudAUnaPublicacion> traerTodasLasSolicitudes() {
        List<SolicitudAUnaPublicacion> solicitudes;
        solicitudes = sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAUnaPublicacion.class)
                .list();
        return solicitudes;
    }

    @Override
    public SolicitudAUnaPublicacion traerSolicitudPorId(Long solicitudId) {
        SolicitudAUnaPublicacion solicitudAUnaPublicacion = (SolicitudAUnaPublicacion) sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAUnaPublicacion.class)
                .add(Restrictions.eq("id", solicitudId))
                .uniqueResult();
        return solicitudAUnaPublicacion;
    }

    @Override
    public void marcarComoVista(Long solicitud) {
        SolicitudAUnaPublicacion solicitudAUnaPublicacion = (SolicitudAUnaPublicacion) sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAUnaPublicacion.class)
                .add(Restrictions.eq("id", solicitud))
                .uniqueResult();
        solicitudAUnaPublicacion.setVista(true);
        sessionFactory.getCurrentSession().saveOrUpdate(solicitudAUnaPublicacion);
    }

    @Override
    public Usuario traerVeterinario() {
        Usuario vet = traerVeterinarioSinSolicitudes();

        if (vet == null) {
            vet = traerVeterinarioConMenosSolicitudes();
        }

        if (vet == null) {
            vet = traerVeterinarioConMenorId();
        }
        return vet;
    }

    private Usuario traerVeterinarioSinSolicitudes() {
        DetachedCriteria subquery = DetachedCriteria.forClass(SolicitudAUnaPublicacion.class, "solicitud")
                .add(Property.forName("solicitud.veterinario.id").eqProperty("vet.id"))
                .setProjection(Projections.id());

        Usuario vet = (Usuario) sessionFactory.getCurrentSession()
                .createCriteria(Usuario.class, "vet")
                .add(Restrictions.eq("vet.rol", "Veterinario"))
                .add(Subqueries.notExists(subquery))
                .addOrder(Order.asc("vet.id"))
                .setMaxResults(1)
                .uniqueResult();
        return vet;
    }

    private Usuario traerVeterinarioConMenosSolicitudes() {
        List<Object[]> resultado = sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAUnaPublicacion.class, "solicitud")
                .setProjection(Projections.projectionList()
                        .add(Projections.groupProperty("solicitud.veterinario.id"), "veterinarioId")
                        .add(Projections.rowCount(), "numSolicitudes"))
                .add(Restrictions.isNotNull("solicitud.veterinario"))
                .addOrder(Order.asc("numSolicitudes"))
                .list();

        if (resultado.isEmpty()) {
            return null;
        }

        Long veterinarioId = (Long) resultado.get(0)[0];

        return (Usuario) sessionFactory.getCurrentSession()
                .get(Usuario.class, veterinarioId);
    }

    private Usuario traerVeterinarioConMenorId() {
        Usuario vet = (Usuario) sessionFactory.getCurrentSession()
                .createCriteria(Usuario.class, "vet")
                .add(Restrictions.eq("rol", "Veterinario"))
                .addOrder(Order.asc("id"))
                .setMaxResults(1)
                .uniqueResult();
        return vet;
    }

    @Override
    public void asignarVeterinario(Usuario vet, SolicitudAUnaPublicacion solicitud) {
        solicitud.setVeterinario(vet);
        sessionFactory.getCurrentSession().saveOrUpdate(solicitud);
    }

    @Override
    public void aceptarSolicitud(Long solictud) {
        SolicitudAUnaPublicacion solicitudAUnaPublicacion = (SolicitudAUnaPublicacion) sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAUnaPublicacion.class)
                .add(Restrictions.eq("id", solictud))
                .uniqueResult();
        solicitudAUnaPublicacion.setPendiente(false);
        solicitudAUnaPublicacion.setAprobada(true);
        sessionFactory.getCurrentSession().saveOrUpdate(solicitudAUnaPublicacion);
    }

    @Override
    public void rechzarSolicitud(Long solicitud) {
        SolicitudAUnaPublicacion solicitudAUnaPublicacion = (SolicitudAUnaPublicacion) sessionFactory.getCurrentSession()
                .createCriteria(SolicitudAUnaPublicacion.class)
                .add(Restrictions.eq("id", solicitud))
                .uniqueResult();
        solicitudAUnaPublicacion.setPendiente(false);
        solicitudAUnaPublicacion.setRechazada(true);
        sessionFactory.getCurrentSession().saveOrUpdate(solicitudAUnaPublicacion);
    }
}