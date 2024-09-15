package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mascota;
import com.tallerwebi.dominio.RepositorioMascota;
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
    public ArrayList<Mascota> buscarMascota(String nombre) {
        final Session session = sessionFactory.getCurrentSession();
        List<Mascota> mascotas = session.createCriteria(Mascota.class)
                .add(Restrictions.eq("nombre", nombre))
                .list();
        return new ArrayList<>(mascotas);
    }

    @Override
    public ArrayList<Mascota> buscarPorTipo(String tipo) {
        final Session session = sessionFactory.getCurrentSession();
        List<Mascota> mascotas = session.createCriteria(Mascota.class)
                .add(Restrictions.eq("tipo", tipo))
                .list();
        return new ArrayList<>(mascotas);
    }

    @Override
    public ArrayList<Mascota> buscarPorSangre(String sangre) {
        final Session session = sessionFactory.getCurrentSession();
        List<Mascota> mascotas = session.createCriteria(Mascota.class)
                .add(Restrictions.eq("sangre", sangre))
                .list();
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
