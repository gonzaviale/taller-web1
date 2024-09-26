package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.*;

import java.util.List;

public interface ServicioPublicacion {

    void guardarPublicacion(Publicacion publicacion) throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo;
    Publicacion busquedaDeUna(Publicacion publicacion) throws PublicacionNoExistente;
    List<Publicacion> obtenerTodasLasPublicaciones();
}
