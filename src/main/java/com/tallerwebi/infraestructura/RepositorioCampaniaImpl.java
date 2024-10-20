package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioCampania;
import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Campana;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;


@Repository("RepositorioCampania")
public class RepositorioCampaniaImpl implements RepositorioCampania {

    private SessionFactory sessionFactory;

    public RepositorioCampaniaImpl(org.hibernate.SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }


    @Override
    public void actualizarBanco(Banco banco){
        sessionFactory.getCurrentSession().update(banco);
    }

    @Override
    public void guardarCampania(Campana campana, Banco banco) {
        this.actualizarBanco(banco);
        sessionFactory.getCurrentSession().save(campana);

    }

    @Override
    public Campana buscarCampaniaPorId(Long id) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Campana> query = builder.createQuery(Campana.class);
        Root<Campana> root = query.from(Campana.class);

        query.select(root)
                .where(builder.equal(root.get("id"), id));
        return session.createQuery(query).uniqueResult();
    }

    @Override
    public List<Campana> buscarCampaniasPorBanco(Long idBanco) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Campana> query = builder.createQuery(Campana.class);
        Root<Campana> root = query.from(Campana.class);


        query.select(root)
                .where(builder.equal(root.get("banco"), idBanco));


        return session.createQuery(query).getResultList();
    }



    @Override
    public List<Campana> obtenerCampanasActualesYproximas(LocalDate fechaActual) {

        Session session = sessionFactory.getCurrentSession();


        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Campana> query = builder.createQuery(Campana.class);
        Root<Campana> root = query.from(Campana.class);

        query.select(root)
                .where(builder.greaterThanOrEqualTo(root.get("fechaFin"), fechaActual));

        return session.createQuery(query).getResultList();
    }



}
