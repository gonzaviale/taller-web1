package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.BancoNoEncontrado;
import org.springframework.stereotype.Service;


@Service("ServicioBanco")
public class ServicioBancoImp implements ServicioBanco {

    private final RepositorioBanco repositorio;

    // Inyección de dependencias a través del constructor
    public ServicioBancoImp(RepositorioBanco repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public PaqueteDeSangre agregarPaqueteDeSangre(int idBanco, PaqueteDeSangre paquete) throws BancoNoEncontrado {
        Banco banco = (Banco) repositorio.buscarPorId(idBanco);

        if (banco == null) {
            throw new BancoNoEncontrado("El banco con ID " + idBanco + " no fue encontrado.");
        }

        String tipoSangre = paquete.getTipoSangre();
        int cantidad = paquete.getCantidad();

        banco.agregarPaqueteDeSangre(tipoSangre, cantidad);

        banco.agregarPaqueteDeSangre(paquete);
        repositorio.guardar(banco);

        return paquete;
    }
}