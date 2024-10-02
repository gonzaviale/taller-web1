package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class ServicioScoreImpl implements ServicioScore {

    private RepositorioScore repositorioScore;
    private RepositorioBanco repositorioBanco;

    @Autowired
    public ServicioScoreImpl(RepositorioScore repositorioScore, RepositorioBanco repositorioBanco) {
        this.repositorioScore = repositorioScore;
        this.repositorioBanco = repositorioBanco;
    }

    @Override
    public void guardarScore(Score score) {
        repositorioScore.guardarScoring(score);
    }

    @Override
    public void incrementarScore(int idBanco) {
        Long idBancoLong1 = (long) idBanco;
        Score score = repositorioScore.obtenerScore(idBancoLong1);
        if(score != null){
            score.setScore(score.getScore() + 1);
            repositorioScore.updateScore(score);
        } else{
            Score newScore = new Score();
            newScore.setScore(1);
            Long idBancoLong = (long) idBanco;
            Banco banco = repositorioBanco.buscarPorId(idBancoLong);
            newScore.setBanco(banco);
            repositorioScore.guardarScoring(newScore);
        }
    }

    @Override
    public void decrementarScore(int idBanco) throws Exception {
        Long idBancoLong = (long) idBanco;
        Score score = repositorioScore.obtenerScore(idBancoLong);
        if(score == null){
            throw new Exception("No se puede decrementar el score");
        }
        if(score.getScore() > 0){
            score.setScore(score.getScore() - 1);
            repositorioScore.updateScore(score);
        }
    }

    @Override
    public ArrayList<Score> obtenerScoring() {
        ArrayList<Score> scoring = repositorioScore.obtenerScoring();
        scoring.sort((o1, o2) -> o2.getScore().compareTo(o1.getScore()));
        return scoring;
    }
}
