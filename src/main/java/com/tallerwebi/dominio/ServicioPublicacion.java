package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PublicacionNoExistente;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDeSangre;

public interface ServicioPublicacion {

    void guardarPublicacion(Publicacion publicacion) throws PublicacionSinTipoDeSangre;
    Publicacion busquedaDeUna(Publicacion publicacion) throws PublicacionNoExistente;

}
