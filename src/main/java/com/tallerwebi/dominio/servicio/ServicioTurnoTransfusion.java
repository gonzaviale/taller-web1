package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.TurnoTransfusion;

import java.util.List;

public interface ServicioTurnoTransfusion {
    public  List<TurnoTransfusion> traerTurnosVigentesVet(Long id);

    void guardarTurno(TurnoTransfusion turno);

    List<TurnoTransfusion> traerTurnosVigentesReceptor(Long id);

    List<TurnoTransfusion> traerTurnosVigentesDonante(Long id);
}
