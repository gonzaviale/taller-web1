package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.RepositorioBanco;
import com.tallerwebi.dominio.RepositorioSolicitud;
import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Entrega;
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

    private final RepositorioBanco repositorioBanco;

    @Autowired
    public ServicioSolicitudImpl(RepositorioSolicitud repositorio , RepositorioBanco repositorioBanco) {

        this.repositorio = repositorio;
        this.repositorioBanco = repositorioBanco;
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
    public void asignarPaqueteASolicitud(int solicitudId, long paqueteId) {
        this.repositorio.solicitudAprobar(solicitudId);
        Banco banco = this.repositorioBanco.buscarPorId(buscarSolicitud(solicitudId).getBancoId());
        PaqueteDeSangre paquete = banco.getPaquete(paqueteId);
        banco.eliminarPaquete(paqueteId);
        this.repositorioBanco.actualizarBanco(banco);
        Entrega entrega = new Entrega(solicitudId, paquete.getId(),banco.getDireccion());
        this.repositorio.guardarEntrega(entrega);

        //enviar mensaje al usuario notificando el id de la entrega y el lugar de retiro

    }

    @Override
    public Banco obtenerBancoXId(Long bancoId) {

        return this.repositorio.buscarPorId(bancoId);
    }




}
