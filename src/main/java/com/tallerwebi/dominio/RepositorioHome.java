package com.tallerwebi.dominio;


import com.tallerwebi.dominio.entidad.Campana;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


public interface RepositorioHome {


    List<Campana> obtenerCampanasActualesYproximas(LocalDate fechaActual);
}
