package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioSolicitud;
import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Entrega;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("repositorioSolicitud")
public class RepositorioSolicitudImpl implements RepositorioSolicitud {

    private final SessionFactory sessionFactory;

    public RepositorioSolicitudImpl(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }


    @Override
    public Solicitud guardarSolicitud(Solicitud solicitud1) {
        sessionFactory.getCurrentSession().save(solicitud1);
        return solicitud1;
    }

    @Override
    public Entrega guardarEntrega(Entrega entrega) {
        sessionFactory.getCurrentSession().save(entrega);
        return entrega;
    }

    @Override
    public Entrega buscarEntregaPorSolicitudId(int solicitudId) {
        return (Entrega) sessionFactory.getCurrentSession()
                .createCriteria(Entrega.class)
                .add(Restrictions.eq("solicitudId", solicitudId))
                .uniqueResult();
    }

    @Override
    public Solicitud buscarSolicitudPorId(int id) {
        return (Solicitud) sessionFactory.getCurrentSession()
                .createCriteria(Solicitud.class)
                .add(Restrictions.eq("id", id)).uniqueResult();
    }

    @Override
    public List<Solicitud> solicitudesPorBanco(Long idBanco) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Solicitud.class)
                .add(Restrictions.eq("bancoId", idBanco))
                .add(Restrictions.eq("estado", "pendiente"))
                .list();
    }

    @Override
    public List<PaqueteDeSangre> obtenerPaquetesDeSangreCompatible(Solicitud solicitud) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<PaqueteDeSangre> cq = cb.createQuery(PaqueteDeSangre.class);
        Root<PaqueteDeSangre> root = cq.from(PaqueteDeSangre.class);

        Predicate tipoProductoPredicate = cb.equal(root.get("tipoProducto"), solicitud.getTipoProducto());
        Predicate tipoSangrePredicate = cb.equal(root.get("tipoSangre"), solicitud.getTipoSangre());
        Predicate cantidadPredicate = cb.greaterThanOrEqualTo(root.get("cantidad"), solicitud.getCantidad());
        Predicate bancoIdPredicate = cb.equal(root.get("banco").get("id"), solicitud.getBancoId()); // Modificado aquí

        cq.select(root).where(cb.and(tipoProductoPredicate, tipoSangrePredicate, cantidadPredicate, bancoIdPredicate));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public void rechazarSolicitud(int solicitudId) {
        Session session = sessionFactory.getCurrentSession();


        Solicitud solicitud = this.buscarSolicitudPorId(solicitudId);
        if (solicitud != null) {

            solicitud.setEstado("Rechazada");

            session.update(solicitud);
        }
    }

    @Override
    public void solicitudAprobar(int solicitudId) {

        Session session = sessionFactory.getCurrentSession();
        Solicitud solicitud = this.buscarSolicitudPorId(solicitudId);
        if (solicitud != null) {
            solicitud.setEstado("aprobada");
            session.update(solicitud);
        }

    }

    @Override
    public Banco buscarPorId(Long bancoId) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Banco> cq = cb.createQuery(Banco.class);
        Root<Banco> root = cq.from(Banco.class);
        cq.select(root).where(cb.equal(root.get("id"), bancoId));
        return session.createQuery(cq).uniqueResult();
    }



}
