package com.tallerwebi.dominio.servicio;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.entidad.Banco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class ServicioScoreImpl implements ServicioScore {

    private RepositorioBanco repositorioBanco;

    @Autowired
    public ServicioScoreImpl(RepositorioBanco repositorioBanco) {
        this.repositorioBanco = repositorioBanco;
    }


    @Override
    public void incrementarScore(int idBanco) {
        Long idBancoLong1 = (long) idBanco;
        Banco banco = repositorioBanco.buscarPorId(idBancoLong1);
        if(banco != null){
            banco.setPuntos(banco.getPuntos() + 1);
            repositorioBanco.actualizarBanco(banco);
        } else{
            throw new RuntimeException("No se puede incrementar el score del banco");
        }
    }

    @Override
    public void decrementarScore(int idBanco) throws Exception {
        Long idBancoLong = (long) idBanco;
        Banco banco = repositorioBanco.buscarPorId(idBancoLong);
        if(banco.getPuntos() <= 0){
            throw new Exception("No se puede decrementar el score");
        }
        if(banco.getPuntos() > 0){
            banco.setPuntos(banco.getPuntos() - 1);
            repositorioBanco.actualizarBanco(banco);
        }
    }

    @Override
    public ArrayList<Banco> obtenerScoring(String sangre) {
        if(sangre != null && !sangre.equals("")){
            ArrayList<Banco> scoring = repositorioBanco.searchBankByScoreAndBlood(sangre);
            scoring.sort((o1, o2) -> o2.getPuntos().compareTo(o1.getPuntos()));
            return scoring;
        } else {
            ArrayList<Banco> scoring = repositorioBanco.searchBankByScore();
            scoring.sort((o1, o2) -> o2.getPuntos().compareTo(o1.getPuntos()));
            return scoring;
        }
    }
}
