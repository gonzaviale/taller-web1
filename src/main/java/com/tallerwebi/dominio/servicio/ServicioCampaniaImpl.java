package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioCampania;
import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Campana;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Transactional
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
    @Override
    public void guardarCampania(Campana campana, Banco banco) {
        this.repositorio.guardarCampania(campana,banco);
    }

    @Override
    public List<Campana> obtenerCampaniasPorBanco(Long idBanco) {
        return this.repositorio.buscarCampaniasPorBanco(idBanco);
    }


}
