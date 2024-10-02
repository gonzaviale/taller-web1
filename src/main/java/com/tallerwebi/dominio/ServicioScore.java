package com.tallerwebi.dominio;

import java.util.ArrayList;

public interface ServicioScore {
    void guardarScore(Score score);
    void incrementarScore(int idBanco);
    void decrementarScore(int idBanco) throws Exception;
    ArrayList<Score> obtenerScoring();
}
