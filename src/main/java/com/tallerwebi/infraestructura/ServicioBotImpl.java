package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Bot;
import com.tallerwebi.dominio.RepositorioBot;
import com.tallerwebi.dominio.ServicioBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class ServicioBotImpl implements ServicioBot {
    private RepositorioBot repositorioBot;

    @Autowired
    public ServicioBotImpl(RepositorioBot repositorioBot) { this.repositorioBot = repositorioBot; }

    @Override
    public Bot solicitarRespuesta(String respuesta) {
        String respuestaSinEspacios = respuesta.replaceAll("\\s+", "");
        return this.repositorioBot.obtenerRespuesta(respuestaSinEspacios);
    }


    @Override
    public void guardarBot(Bot bot) {
        this.repositorioBot.guardarBot(bot);
    }
}
