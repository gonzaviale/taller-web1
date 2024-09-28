package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Bot;
import com.tallerwebi.dominio.RepositorioBot;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioBotTest {
    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioBot repositorio;

    @Test
    @Transactional
    @Rollback
    public void queSeGuardeBot() {
        Bot bot = new Bot();
        bot.setEntrada("Hola");
        bot.setRespuesta("Como estas?");

        this.repositorio.guardarBot(bot);

        assertThat(bot.getEntrada(), notNullValue());
        assertThat(bot.getRespuesta(), notNullValue());
    }

    @Test
    @Transactional
    @Rollback
    public void queRetorneRespuesta() {
        Bot bot = new Bot();
        bot.setEntrada("Hola");
        bot.setRespuesta("Como estas?");
        Bot bot2 = new Bot();
        bot2.setEntrada("Bien y vos?");
        bot2.setRespuesta("De lujo");

        this.repositorio.guardarBot(bot);
        this.repositorio.guardarBot(bot2);
        Bot botRecibido = this.repositorio.obtenerRespuesta("Hola");

        assertThat(botRecibido.getRespuesta(), botRecibido.getRespuesta().equals("Como estas?"));
    }

}
