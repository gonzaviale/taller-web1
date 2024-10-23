package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.excepcion.PublicacionNoExistente;

import java.util.ArrayList;
import java.util.List;

public interface RepositorioPublicacion {
    void guardarPublicacion(Publicacion publicacion);
    Publicacion obtenerPorId(Long id) throws PublicacionNoExistente;

    List<Publicacion> obtenerTodasLasPublicaciones();

    ArrayList<Publicacion> buscarPublicaciones(String titulo, String tipoDeSangre, String zonaDeResidencia, String tipoDePublicacion);
    void desactivarPublicacion(Long publicacionId);
    void activarPublicacion(Long publicacionId);
}
