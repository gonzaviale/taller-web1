package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioBanco {
    Banco guardar(Banco banco);
    PaqueteDeSangre guardarSangre(PaqueteDeSangre paquete, Banco banco);
    Banco buscarBanco(String email, String password);
    Banco buscarPorId(Long idBanco);
    PaqueteDeSangre buscarSangre(String s);
    List<PaqueteDeSangre> obtenerPaquetesDeSangrePorBanco(Long idBanco);
    List<Solicitud> solicitudesPorBanco(Long idBanco);
    Solicitud guardarSolicitud(Solicitud solicitud1);
}
