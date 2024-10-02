package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Bot;
import com.tallerwebi.dominio.RepositorioBot;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository("repositorioBot")
public class RepositorioBotImpl implements RepositorioBot {

    private SessionFactory sessionFactory;

    public RepositorioBotImpl(SessionFactory sessionFactory) {this.sessionFactory = sessionFactory;}

    @Override
    public Bot obtenerRespuesta(String entrada) {
        final Session session = sessionFactory.getCurrentSession();
        return (Bot) session.createCriteria(Bot.class)
                .add(Restrictions.eq("entrada", entrada))
                .uniqueResult();
    }

    @Override
    public void guardarBot(Bot bot) {
        final Session session = sessionFactory.getCurrentSession();
        session.save(bot);
    }
}
