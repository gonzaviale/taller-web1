package com.tallerwebi.dominio;

import java.util.List;

public interface ServicioImagenes {
    List<String> obtenerImagenesPorUsuario(Long idMascota);
}
