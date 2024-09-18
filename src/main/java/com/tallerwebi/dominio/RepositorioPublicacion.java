package com.tallerwebi.dominio;

import com.tallerwebi.dominio.excepcion.PublicacionNoExistente;

public interface RepositorioPublicacion {
    void guardarPublicacion(Publicacion publicacion);
    Publicacion obtenerPorId(Long id) throws PublicacionNoExistente;
}
