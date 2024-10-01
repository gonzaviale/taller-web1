package com.tallerwebi.infraestructura;
import com.tallerwebi.dominio.*;
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
    public void agregarBanco(Banco banco) {
        this.repositorio.guardar(banco);
    }
}