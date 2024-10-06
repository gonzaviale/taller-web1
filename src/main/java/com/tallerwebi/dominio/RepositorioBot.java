package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Bot;

public interface RepositorioBot {
    public Bot obtenerRespuesta(String entrada);
    public void guardarBot(Bot bot);
}
