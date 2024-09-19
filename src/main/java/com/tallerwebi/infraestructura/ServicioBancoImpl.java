package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.BancoNoEncontrado;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service("ServicioBanco")
public class ServicioBancoImpl implements ServicioBanco {

    private final RepositorioBanco repositorio;

    // Inyección de dependencias a través del constructor
    public ServicioBancoImpl(RepositorioBanco repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public Banco agregarPaqueteDeSangre(Long idBanco, PaqueteDeSangre paquete) throws BancoNoEncontrado {
        Banco banco = (Banco) repositorio.buscarPorId(idBanco);

        if (banco == null) {
            throw new BancoNoEncontrado("gfdg");
        }

        banco.agregarPaqueteDeSangre( paquete);
        return  repositorio.actualizar(banco,paquete);

    }

    @Override
    public Banco BuscarBancoId(Long idBanco) {

        return this.repositorio.buscarPorId(idBanco);
    }




}