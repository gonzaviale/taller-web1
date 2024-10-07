package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Bot;
import com.tallerwebi.dominio.RepositorioBot;
import com.tallerwebi.dominio.servicio.ServicioBot;
import com.tallerwebi.dominio.servicio.ServicioBotImpl;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class ServicioBotTest {

    private ServicioBot servicioBot;
    private RepositorioBot repositorioBot;

    @BeforeEach
    public void setUp() {
        repositorioBot = mock(RepositorioBot.class);
        servicioBot = new ServicioBotImpl(repositorioBot);
    }

    @Test
    public void queDevuelvaMensajeBot() {
        Bot bot = new Bot();
        bot.setEntrada("Hola");
        bot.setRespuesta("¿Como estas?");
        servicioBot.guardarBot(bot);

        when(repositorioBot.obtenerRespuesta("Hola")).thenReturn(bot);
        Bot botRespuesta = servicioBot.solicitarRespuesta("Hola");

        verify(repositorioBot, times(1)).guardarBot(bot);
        verify(repositorioBot, times(1)).obtenerRespuesta("Hola");
        assertEquals("¿Como estas?", botRespuesta.getRespuesta());
    }



}
