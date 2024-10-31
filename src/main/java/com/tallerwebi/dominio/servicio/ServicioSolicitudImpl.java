package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioSolicitud;
import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service("servicioSolicitud")
public class ServicioSolicitudImpl implements ServicioSolicitud {


    private final RepositorioSolicitud repositorio;

    @Autowired
    public ServicioSolicitudImpl(RepositorioSolicitud repositorio) {

        this.repositorio = repositorio;
    }


    @Override
    public Solicitud agregarSolicitud(Solicitud solicitud1) {

        return repositorio.guardarSolicitud(solicitud1);
    }

    @Override
    public Solicitud buscarSolicitud(int id) {
        return this.repositorio.buscarSolicitudPorId(id);
    }

    @Override
    public List<Solicitud> obtenerSolicitudesXBanco(Long idBanco) {
        return repositorio.solicitudesPorBanco(idBanco);
    }

    @Override
    public List<PaqueteDeSangre> obtenerPaquetesDeSangreCompatibles(Solicitud solicitud) {
        return repositorio.obtenerPaquetesDeSangreCompatible(solicitud);
    }

    @Override
    public void rechazarSolicitud(int solicitudId) {

        this.repositorio.rechazarSolicitud( solicitudId);
    }


    @Override
    public void asignarPaqueteASolicitud(int solicitudId, int paqueteId) {
        //TODO
        // Entrega entrega = new Entrega(solicitudId,paqueteId);
        // this.guardarEntrega(entrega) ;
        this.repositorio.solicitudAprobar(solicitudId);
    }

    @Override
    public Banco obtenerBancoXId(Long bancoId) {

        return this.repositorio.buscarPorId(bancoId);
    }


}
