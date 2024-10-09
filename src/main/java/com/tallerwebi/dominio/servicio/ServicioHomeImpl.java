package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioHome;
import com.tallerwebi.dominio.entidad.Campana;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ServicioHomeImpl implements ServicioHome{


    private final RepositorioHome repositorio;

    @Autowired
    public ServicioHomeImpl( RepositorioHome repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public List<Campana> obtenerCampanasActualesYproximas() {
        LocalDate fechaActual = LocalDate.now();
        return  this.repositorio.obtenerCampanasActualesYproximas(fechaActual);
    }
}
