package com.tallerwebi.dominio;

import java.util.ArrayList;

public interface RepositorioMascota {
    public ArrayList<Mascota> buscarMascota(String nombre);
    public ArrayList<Mascota> buscarPorTipo(String tipo);
    public ArrayList<Mascota> buscarPorSangre(String sangre);
    public void agregarMascota(Mascota mascota);
    public void eliminarMascota(Mascota mascota);
    public void actualizarMascota(Mascota mascota);
}
