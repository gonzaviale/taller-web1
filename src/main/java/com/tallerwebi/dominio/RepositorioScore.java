package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Score;

import java.util.ArrayList;

public interface RepositorioScore {
    public void guardarScoring(Score score);
    public void updateScore(Score score);
    public Score obtenerScore(Long idBanco);
    public ArrayList<Score> obtenerScoring();
}
