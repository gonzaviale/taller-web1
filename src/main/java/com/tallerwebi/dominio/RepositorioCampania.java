package com.tallerwebi.dominio;


import com.tallerwebi.dominio.entidad.Campana;

import java.time.LocalDate;
import java.util.List;


public interface RepositorioCampania {


    List<Campana> obtenerCampanasActualesYproximas(LocalDate fechaActual);
}
