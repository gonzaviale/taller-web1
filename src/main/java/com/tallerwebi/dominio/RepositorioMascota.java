package com.tallerwebi.dominio;

import java.util.ArrayList;

public interface RepositorioMascota {
    public ArrayList<Mascota> buscarMascota(String nombre, String sangre, String tipo);
    public void agregarMascota(Mascota mascota);
    public void eliminarMascota(Mascota mascota);
    public void actualizarMascota(Mascota mascota);
}
