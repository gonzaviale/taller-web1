package com.tallerwebi.dominio;

public interface RepositorioBot {
    public Bot obtenerRespuesta(String entrada);
    public void guardarBot(Bot bot);
}
