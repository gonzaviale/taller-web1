package com.tallerwebi.dominio.servicio;

import java.util.List;

public interface ServicioImagenes {
    List<String> obtenerImagenesPorUsuario(Long idMascota);
}
