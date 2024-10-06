package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.excepcion.*;

import java.util.List;

public interface ServicioPublicacion {

    void guardarPublicacion(Publicacion publicacion) throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo;
    Publicacion busquedaDeUna(Publicacion publicacion) throws PublicacionNoExistente;
    List<Publicacion> obtenerTodasLasPublicaciones();
}
