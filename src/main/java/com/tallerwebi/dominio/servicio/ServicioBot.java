package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Bot;

public interface ServicioBot {
    public Bot solicitarRespuesta(String respuesta);
    public  void guardarBot(Bot bot);
}
