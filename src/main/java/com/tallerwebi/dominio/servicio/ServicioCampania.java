package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Campana;

import java.util.List;

public interface ServicioCampania {
    List<Campana> obtenerCampanasActualesYproximas();
    void guardarCampania(Campana campana, Banco banco);
    List<Campana> obtenerCampaniasPorBanco(Long idBanco);

}
