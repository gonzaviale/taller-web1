package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.TurnoTransfusion;

import java.util.List;

public interface ServicioTurnoTransfusion {
    public  List<TurnoTransfusion> traerTurnosVigentesVet();

    void guardarTurno(TurnoTransfusion turno);
}
