package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mascota;
import com.tallerwebi.dominio.RepositorioMascota;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("repositorioMascota")
public class RepositorioMascotaImpl implements RepositorioMascota {

    private SessionFactory sessionFactory;

    public RepositorioMascotaImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public ArrayList<Mascota> buscarMascota(String nombre, String sangre, String tipo) {
        final Session session = sessionFactory.getCurrentSession();

        Criteria criteria = session.createCriteria(Mascota.class);

        if (nombre != null && !nombre.isEmpty()) {
            criteria.add(Restrictions.eq("nombre", nombre));
        }

        if (sangre != null && !sangre.isEmpty()) {
            criteria.add(Restrictions.eq("sangre", sangre));
        }

        if (tipo != null && !tipo.isEmpty()) {
            criteria.add(Restrictions.eq("tipo", tipo));
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