package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Campana;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;

import java.util.List;

public interface ServicioBanco {
    void agregarPaqueteDeSangre(PaqueteDeSangre paquete, Banco banco);
    void agregarBanco(Banco banco);
    Banco BuscarBancoId(Long idBanco);
    List<PaqueteDeSangre> obtenerPaquetesDeSangrePorBanco(Long idBanco);
    List<Solicitud> obtenerSolicitudesXBanco(Long idBanco);
    Solicitud agregarSolicitud(Solicitud solicitud1);
    Solicitud buscarSolicitud(int id);
    List<PaqueteDeSangre> obtenerPaquetesDeSangreCompatibles(Solicitud solicitud);
    void rechazarSolicitud(int solicitudId);
    void asignarPaqueteASolicitud(int solicitudId, int paqueteId);
    PaqueteDeSangre BuscarPaqueteSangreXId(int paqueteId);
    void guardarCampania(Campana campana, Banco banco);
    List<Campana> obtenerCampaniasPorBanco(Long idBanco);
}
