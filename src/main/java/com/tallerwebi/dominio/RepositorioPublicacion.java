package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PublicacionNoExistente;

import java.util.List;

public interface RepositorioPublicacion {
    void guardarPublicacion(Publicacion publicacion);
    Publicacion obtenerPorId(Long id) throws PublicacionNoExistente;

    List<Publicacion> obtenerTodasLasPublicaciones();
}
