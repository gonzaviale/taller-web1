package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PublicacionNoExistente;
import com.tallerwebi.dominio.excepcion.PublicacionNoValida;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDePublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDeSangre;

public interface ServicioPublicacion {

    void guardarPublicacion(Publicacion publicacion) throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida;
    Publicacion busquedaDeUna(Publicacion publicacion) throws PublicacionNoExistente;

}
