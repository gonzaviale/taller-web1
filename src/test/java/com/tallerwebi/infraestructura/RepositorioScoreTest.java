package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.RepositorioBanco;
import com.tallerwebi.dominio.RepositorioScore;
import com.tallerwebi.dominio.entidad.Score;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioScoreTest {

    @Autowired
    private RepositorioScore repositorioScore;

    @Autowired
    private RepositorioBanco repositorioBanco;

    private Banco banco;
    private Score score;

    @BeforeEach
    public void setUp() {
        // Inicializa el banco
        this.banco = new Banco();
        banco.setNombreBanco("Banco 1");
        banco.setCiudad("Merlo");
        banco.setDireccion("aa123");
        banco.setEmail("aa@aaaa.com");
        banco.setPais("Argentina");
        banco.setPassword("aaaa");
        banco.setHorario("12.00");
        banco.setTelefono("12345678");

        // Inicializa el score
        this.score = new Score();
        score.setBanco(banco);
        score.setScore(10);
    }

    @Test
    @Transactional
    @Rollback
    public void testInsertScore() {
        // Guarda el banco antes del score
        repositorioBanco.guardar(banco);

        // Guarda el score
        repositorioScore.guardarScoring(score);

        // Verifica que se haya guardado correctamente
        Score scoreGuardado = repositorioScore.obtenerScore(banco.getId());
        assertThat(scoreGuardado, notNullValue());
        assertThat(scoreGuardado.getScore(), equalTo(score.getScore()));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateScore() {
        // Guarda el banco y el score inicialmente
        repositorioBanco.guardar(banco);
        repositorioScore.guardarScoring(score);

        // Actualiza el score
        score.setScore(20);
        repositorioScore.updateScore(score);

        // Verifica que el score se haya actualizado correctamente
        Score scoreActualizado = repositorioScore.obtenerScore(banco.getId());
        assertThat(scoreActualizado.getScore(), equalTo(20));
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerScore() {
        // Guarda el banco y el score
        repositorioBanco.guardar(banco);
        repositorioScore.guardarScoring(score);

        // Obtiene el score y lo verifica
        Score scoreObtenido = repositorioScore.obtenerScore(banco.getId());
        assertThat(scoreObtenido, notNullValue());
        assertThat(scoreObtenido.getScore(), equalTo(10));
    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerScoring() {
        // Guarda el banco y el score
        repositorioBanco.guardar(banco);
        repositorioScore.guardarScoring(score);

        // Obtiene todos los scores y los verifica
        List<Score> scores = repositorioScore.obtenerScoring();
        assertThat(scores, notNullValue());
        assertThat(scores.size(), equalTo(1));
        assertThat(scores.get(0).getScore(), equalTo(10));
    }
}
