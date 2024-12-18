package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Entrega;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.RepositorioPublicacion;
import com.tallerwebi.dominio.entidad.Solicitud;
import com.tallerwebi.dominio.excepcion.PublicacionNoExistente;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository("repositorioPublicacion")
public class RepositorioPublicacionImpl implements RepositorioPublicacion {

    final private SessionFactory sessionFactory;

    @Autowired
    public RepositorioPublicacionImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void guardarPublicacion(Publicacion publicacion) {
        publicacion.setLocalDateTime(LocalDateTime.now());
        sessionFactory.getCurrentSession().save(publicacion);
    }

    @Override
    public Publicacion obtenerPorId(Long id) throws PublicacionNoExistente {
        Publicacion buscada = sessionFactory.getCurrentSession().get(Publicacion.class, id);
        //|| !(buscada.getEstaActiva())
        if (buscada == null) {
            throw new PublicacionNoExistente();
        }

        return buscada;
    }

    @Override
    public List<Publicacion> obtenerTodasLasPublicaciones() {
        return sessionFactory.getCurrentSession().createCriteria(Publicacion.class)
                .add(Restrictions.eq("estaActiva", true))
                .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
                .list();
    }

    @Override
    public ArrayList<Publicacion> buscarPublicaciones(String titulo, String tipoDeSangre, String zonaDeResidencia, String tipoDePublicacion) {


        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(Publicacion.class)
                .add(Restrictions.eq("estaActiva", true));

        if (titulo != null && !titulo.isEmpty()) {
            criteria.add(Restrictions.like("titulo", "%" + titulo + "%", MatchMode.ANYWHERE));
        }

        if (tipoDeSangre != null && !tipoDeSangre.isEmpty()) {
            criteria.add(Restrictions.like("tipoDeSangre", "%" + tipoDeSangre + "%", MatchMode.ANYWHERE));
        }

        if (zonaDeResidencia != null && !zonaDeResidencia.isEmpty()) {
            criteria.add(Restrictions.like("zonaDeResidencia", "%" + zonaDeResidencia + "%", MatchMode.ANYWHERE));
        }
        if (tipoDePublicacion != null && !tipoDePublicacion.isEmpty()) {
            criteria.add(Restrictions.like("tipoDePublicacion", "%" + tipoDePublicacion + "%", MatchMode.ANYWHERE));
        }

        List<Publicacion> publicaciones = criteria.list();
        return new ArrayList<>(publicaciones);
    }

    @Override
    public void desactivarPublicacion(Long publicacionId) {
        Publicacion publicacion = (Publicacion) sessionFactory.getCurrentSession()
                .createCriteria(Publicacion.class)
                .add(Restrictions.eq("id", publicacionId))
                .uniqueResult();
        publicacion.setEstaActiva(false);
        sessionFactory.getCurrentSession().saveOrUpdate(publicacion);
    }

    @Override
    public void activarPublicacion(Long publicacionId) {
        Publicacion publicacion = (Publicacion) sessionFactory.getCurrentSession()
                .createCriteria(Publicacion.class)
                .add(Restrictions.eq("id", publicacionId))
                .uniqueResult();
        publicacion.setEstaActiva(true);
        sessionFactory.getCurrentSession().saveOrUpdate(publicacion);
    }

    @Override
    public void editarPublicacion(Long id, Publicacion publicacionActualizada) throws PublicacionNoExistente {
        Publicacion publicacion = this.obtenerPorId(id);

        actualizarCamposDePublicacion(publicacion, publicacionActualizada);

        sessionFactory.getCurrentSession().update(publicacion);
    }


    //TODO
    @Override
    public List<Entrega> obtenerEntregasParaUsuario(Long id) {
        CriteriaBuilder cb = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Entrega> cq = cb.createQuery(Entrega.class);

        Root<Entrega> entregaRoot = cq.from(Entrega.class);

        Predicate usuarioPredicate = cb.equal(entregaRoot.get("usuarioId"), id);
        cq.select(entregaRoot).where(usuarioPredicate);
        return sessionFactory.getCurrentSession().createQuery(cq).getResultList();

    }

    private void actualizarCamposDePublicacion(Publicacion publicacion, Publicacion publicacionActualizada) {

        publicacion.setTitulo(publicacionActualizada.getTitulo());
        publicacion.setDescripcion(publicacionActualizada.getDescripcion());
        publicacion.setPuedeMovilizarse(publicacionActualizada.getPuedeMovilizarse());
        publicacion.setZonaDeResidencia(publicacionActualizada.getZonaDeResidencia());
        publicacion.setMascotaDonante(publicacionActualizada.getMascotaDonante());
        publicacion.setTipoDeSangre(publicacionActualizada.getTipoDeSangre());
    }

}
