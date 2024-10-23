package com.tallerwebi.dominio;


import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Campana;

import java.time.LocalDate;
import java.util.List;


public interface RepositorioCampania {

    public void actualizarBanco(Banco banco);

    void guardarCampania(Campana campana, Banco banco);

    Campana buscarCampaniaPorId(Long id);

    List<Campana> buscarCampaniasPorBanco(Long idBanco);

    List<Campana> obtenerCampanasActualesYproximas(LocalDate fechaActual);

}
