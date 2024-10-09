package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Campana;

import java.util.List;

public interface ServicioHome {
    List<Campana> obtenerCampanasActualesYproximas();
}
