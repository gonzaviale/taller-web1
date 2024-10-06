package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Score;

import java.util.ArrayList;

public interface ServicioScore {
    void guardarScore(Score score);
    void incrementarScore(int idBanco);
    void decrementarScore(int idBanco) throws Exception;
    ArrayList<Score> obtenerScoring();
}
