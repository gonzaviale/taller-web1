package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository("RepositorioBanco")
public class RepositorioBancoImpl implements RepositorioBanco {

    private SessionFactory sessionFactory;

    public RepositorioBancoImpl(org.hibernate.SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    @Override
    public Banco guardar(Banco banco) {
        sessionFactory.getCurrentSession().save(banco);
        return banco;
    }

    @Override
    public PaqueteDeSangre guardarSangre(PaqueteDeSangre paquete, Banco banco) {
        banco.agregarPaqueteDeSangre(paquete);

        sessionFactory.getCurrentSession().save(paquete);
        sessionFactory.getCurrentSession().saveOrUpdate(banco);
        return paquete;
    }

    @Override
    public Banco buscarBanco(String email, String password) {
        final Session session = sessionFactory.getCurrentSession();
        return (Banco) session.createCriteria(Usuario.class)
                .add(Restrictions.eq("email", email))
                .add(Restrictions.eq("password", password))
                .uniqueResult();
    }

    @Override
    public Banco buscarPorId(Long idBanco) {
        final Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Banco> cq = cb.createQuery(Banco.class);
        Root<Banco> root = cq.from(Banco.class);
        cq.select(root).where(cb.equal(root.get("id"), idBanco));
        return session.createQuery(cq).uniqueResult();
    }


    @Override
    public PaqueteDeSangre buscarSangre(String tipoSangre) {

        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<PaqueteDeSangre> cq = cb.createQuery(PaqueteDeSangre.class);
        Root<PaqueteDeSangre> root = cq.from(PaqueteDeSangre.class);
        cq.select(root).where(cb.equal(root.get("tipoSangre"), tipoSangre));
        return session.createQuery(cq).uniqueResult();
    }


    public List<PaqueteDeSangre> obtenerPaquetesDeSangrePorBanco(Long idBanco) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<PaqueteDeSangre> cq = cb.createQuery(PaqueteDeSangre.class);
        Root<PaqueteDeSangre> root = cq.from(PaqueteDeSangre.class);


        cq.select(root);
        cq.where(cb.equal(root.get("banco").get("id"), idBanco));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public List<Solicitud> solicitudesPorBanco(Long idBanco) {
        final Session session = sessionFactory.getCurrentSession();
        return session.createCriteria(Solicitud.class)
                .add(Restrictions.eq("bancoId", idBanco))
                .list();
    }

    @Override
    public Solicitud guardarSolicitud(Solicitud solicitud1) {
        sessionFactory.getCurrentSession().save(solicitud1);
        return solicitud1;
    }

    @Override
    public Solicitud buscarSolicitudPorId(int id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Solicitud> cq = cb.createQuery(Solicitud.class);
        Root<Solicitud> root = cq.from(Solicitud.class);
        cq.select(root).where(cb.equal(root.get("id"), id));
        return session.createQuery(cq).uniqueResult();
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
}