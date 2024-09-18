package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.RepositorioPublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionNoExistente;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("repositorioPublicacionBusqueda")
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
}
