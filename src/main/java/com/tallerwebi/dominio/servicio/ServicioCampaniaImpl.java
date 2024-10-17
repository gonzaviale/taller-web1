package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioCampania;
import com.tallerwebi.dominio.entidad.Campana;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServicioCampaniaImpl implements ServicioCampania {


    private final RepositorioCampania repositorio;

    @Autowired
    public ServicioCampaniaImpl(RepositorioCampania repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Campana> obtenerCampanasActualesYproximas() {
        LocalDate fechaActual = LocalDate.now();
        return  this.repositorio.obtenerCampanasActualesYproximas(fechaActual);
    }
}
