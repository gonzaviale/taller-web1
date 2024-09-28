package com.tallerwebi.infraestructura;
import com.tallerwebi.dominio.Banco;
import com.tallerwebi.dominio.PaqueteDeSangre;
import com.tallerwebi.dominio.RepositorioBanco;
import com.tallerwebi.dominio.ServicioBanco;
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
    public void agregarBanco(Banco banco) {
        this.repositorio.guardar(banco);
    }
}