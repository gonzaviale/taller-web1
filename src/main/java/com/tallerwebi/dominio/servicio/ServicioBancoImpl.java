package com.tallerwebi.dominio.servicio;
import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service("ServicioBanco")
public class ServicioBancoImpl implements ServicioBanco {

    private final RepositorioBanco repositorio;

    @Autowired
    public ServicioBancoImpl(RepositorioBanco repositorio) {
        this.repositorio = repositorio;
    }
    @Override
    public void agregarPaqueteDeSangre(PaqueteDeSangre paquete, Banco banco) {
        repositorio.guardarSangre(paquete,banco);
    }
    @Override
    public Banco BuscarBancoId(Long idBanco) {

        return this.repositorio.buscarPorId(idBanco);
    }

    @Override
    public List<PaqueteDeSangre>  obtenerPaquetesDeSangrePorBanco(Long idBanco) {
        return repositorio.obtenerPaquetesDeSangrePorBanco(idBanco);
    }

    @Override
    public List<Solicitud> obtenerSolicitudesXBanco(Long idBanco) {
        return repositorio.solicitudesPorBanco(idBanco);
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
    public List<PaqueteDeSangre> obtenerPaquetesDeSangreCompatibles(Solicitud solicitud) {
        return repositorio.obtenerPaquetesDeSangreCompatible(solicitud);
    }

    @Override
    public void rechazarSolicitud(int solicitudId) {
       this.repositorio.rechazarSolicitud( solicitudId);
    }

    @Override
    public void asignarPaqueteASolicitud(int solicitudId, int paqueteId) {

       // Entrega entrega = new Entrega(solicitudId,paqueteId);
       // this.guardarEntrega(entrega) ;
        this.repositorio.solicitudAprobar(solicitudId);
    }

    @Override
    public PaqueteDeSangre BuscarPaqueteSangreXId(int paqueteId) {
        return this.repositorio.buscarSangreXId(paqueteId);
    }

    @Override
    public void agregarBanco(Banco banco) {

        this.repositorio.guardar(banco);
    }
}