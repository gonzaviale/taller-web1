package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.RepositorioPublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionNoExistente;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("repositorioPublicacion")
public class RepositorioPublicacionImpl implements RepositorioPublicacion {

    final private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPublicacionImpl(SessionFactory sessionFactory){
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void guardarPublicacion(Publicacion publicacion) {
        sessionFactory.getCurrentSession().save(publicacion);
    }

    @Override
    public Publicacion obtenerPorId(Long id) throws PublicacionNoExistente {
        Publicacion buscada= sessionFactory.getCurrentSession().get(Publicacion.class,id);
        if(buscada==null){
            throw new PublicacionNoExistente();
        }
        return buscada;
    }

    @Override
    public List<Publicacion> obtenerTodasLasPublicaciones() {
        String hql = "FROM Publicacion"; // Traer todos los registros de la entidad Publicacion
        Query<Publicacion> query = sessionFactory.getCurrentSession().createQuery(hql, Publicacion.class);
        return query.getResultList();
    }

    @Override
    public ArrayList<Publicacion> buscarPublicaciones(String titulo, String tipoDeSangre, String zonaDeResidencia, String tipoDePublicacion) {


        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Publicacion.class);

        if (titulo != null && !titulo.isEmpty()) {
            criteria.add(Restrictions.like("titulo","%" +titulo + "%", MatchMode.ANYWHERE));
        }

        if (tipoDeSangre != null && !tipoDeSangre.isEmpty()) {
            criteria.add(Restrictions.like("tipoDeSangre", "%" +tipoDeSangre + "%", MatchMode.ANYWHERE));
        }

        if (zonaDeResidencia != null && !zonaDeResidencia.isEmpty()) {
            criteria.add(Restrictions.like("zonaDeResidencia", "%" +zonaDeResidencia + "%",MatchMode.ANYWHERE));
        }
        if (tipoDePublicacion != null && !tipoDePublicacion.isEmpty()) {
            criteria.add(Restrictions.like("tipoDePublicacion", "%" +tipoDePublicacion + "%",MatchMode.ANYWHERE));
        }

        List<Publicacion> publicaciones = criteria.list();
        return new ArrayList<>(publicaciones);
    }
}
