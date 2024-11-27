package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.TurnoTransfusion;

import java.util.List;

public interface RepositorioTurnoTransfusion {
    void guardarTurno(TurnoTransfusion turno);

    List<TurnoTransfusion> traerTodosLosTurnos();
}
