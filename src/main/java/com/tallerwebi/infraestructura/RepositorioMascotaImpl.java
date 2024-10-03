package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mascota;
import com.tallerwebi.dominio.RepositorioMascota;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("repositorioMascota")
public class RepositorioMascotaImpl implements RepositorioMascota {

    private final SessionFactory sessionFactory;

    @Override
    public List<Mascota> buscarMascotaEnRevision() {
        Session session = sessionFactory.openSession();
        List<Mascota> mascotasEnRevision = null;
        mascotasEnRevision = session.createCriteria(Mascota.class)
                .add(Restrictions.eq("enRevision", true))
                .list();
        return mascotasEnRevision;
    }
    @Override
    public void aprobarMascotaDonante(Long mascotaId) {
        Session session = sessionFactory.openSession();
        Mascota mascota = (Mascota) session.createCriteria(Mascota.class)
                .add(Restrictions.eq("id", mascotaId))
                .uniqueResult();
        mascota.setRevision(false);
        mascota.setAprobado(true);
        // Actualizamos la mascota en la sesión
        session.saveOrUpdate(mascota);
    }
    @Override
    public void rechazarMascotaDonante(Long mascotaId) {
        Session session = sessionFactory.openSession();
        Mascota mascota = (Mascota) session.createCriteria(Mascota.class)
                .add(Restrictions.eq("id", mascotaId))
                .uniqueResult();
        mascota.setRevision(false);
        mascota.setRechazado(true);
        session.saveOrUpdate(mascota);
    }

    public RepositorioMascotaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ArrayList<Mascota> buscarMascota(String nombre, String sangre, String tipo) {
        final Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria(Mascota.class);

        if (nombre != null && !nombre.isEmpty()) {
            criteria.add(Restrictions.like("nombre","%" +nombre + "%",MatchMode.ANYWHERE));
        }

        if (sangre != null && !sangre.isEmpty()) {
            criteria.add(Restrictions.like("sangre", "%" +sangre + "%", MatchMode.ANYWHERE));
        }

        if (tipo != null && !tipo.isEmpty()) {
            criteria.add(Restrictions.like("tipo", "%" +tipo + "%",MatchMode.ANYWHERE));
        }

        List<Mascota> mascotas = criteria.list();
        return new ArrayList<>(mascotas);
    }


    @Override
    public void agregarMascota(Mascota mascota) {
        final Session session = sessionFactory.getCurrentSession();
        session.save(mascota);
    }

    @Override
    public void eliminarMascota(Mascota mascota) {
        final Session session = sessionFactory.getCurrentSession();
        session.delete(mascota);
    }

    @Override
    public void actualizarMascota(Mascota mascota) {
        final Session session = sessionFactory.getCurrentSession();
        session.update(mascota);
    }
}
