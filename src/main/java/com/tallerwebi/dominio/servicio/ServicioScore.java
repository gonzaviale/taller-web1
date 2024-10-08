package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Banco;

import java.util.ArrayList;

public interface ServicioScore {
    void incrementarScore(int idBanco);
    void decrementarScore(int idBanco) throws Exception;
    ArrayList<Banco> obtenerScoring(String sangre);
}
