package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.excepcion.*;

import java.util.List;

public interface ServicioPublicacion {

    void guardarPublicacion(Publicacion publicacion) throws PublicacionNoValida, PublicacionSinTitulo;
    Publicacion busquedaDeUna(Publicacion publicacion) throws PublicacionNoExistente;
    List<Publicacion> obtenerTodasLasPublicaciones();
    Publicacion busquedaPorId(Long publicacionId) throws PublicacionNoExistente;
    void desactivarPublicacion(Long publicacionId);
    void activarPublicacion(Long publicacionId);
}
