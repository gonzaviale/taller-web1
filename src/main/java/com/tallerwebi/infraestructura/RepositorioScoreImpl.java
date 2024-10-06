package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioScore;
import com.tallerwebi.dominio.entidad.Score;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository("repositorioScore")
public class RepositorioScoreImpl implements RepositorioScore {
    private SessionFactory sessionFactory;
    public RepositorioScoreImpl (SessionFactory sessionFactory) { this.sessionFactory = sessionFactory; }

    @Override
    public void guardarScoring(Score score) {
        final Session session = sessionFactory.getCurrentSession();
        session.save(score);
    }

    @Override
    public void updateScore(Score score) {
        final Session session = sessionFactory.getCurrentSession();
        session.update(score);
    }

    @Override
    public Score obtenerScore(Long idBanco) {
        final Session session = sessionFactory.getCurrentSession();
        return (Score) session.createCriteria(Score.class)
                .createAlias("banco", "b")
                .add(Restrictions.eq("b.id", idBanco))
                .uniqueResult();
    }

    @Override
    public ArrayList<Score> obtenerScoring() {
        final Session session = sessionFactory.getCurrentSession();
        return (ArrayList<Score>) session.createCriteria(Score.class).list();
    }
}
