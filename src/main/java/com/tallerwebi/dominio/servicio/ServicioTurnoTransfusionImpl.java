package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioTurnoTransfusion;
import com.tallerwebi.dominio.entidad.TurnoTransfusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("ServicioTurnoTransfusion")
@Transactional
public class ServicioTurnoTransfusionImpl implements ServicioTurnoTransfusion{
    private final RepositorioTurnoTransfusion repositorioTurnoTransfusion;

    @Autowired
    public ServicioTurnoTransfusionImpl(RepositorioTurnoTransfusion repositorioTurnoTransfusion) {
        this.repositorioTurnoTransfusion = repositorioTurnoTransfusion;
    }

    @Override
    public void guardarTurno(TurnoTransfusion turno) {
        repositorioTurnoTransfusion.guardarTurno(turno);
    }
}
