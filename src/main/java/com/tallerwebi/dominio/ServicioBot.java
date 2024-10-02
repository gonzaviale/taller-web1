package com.tallerwebi.dominio;

public interface ServicioBot {
    public Bot solicitarRespuesta(String respuesta);
    public  void guardarBot(Bot bot);
}
