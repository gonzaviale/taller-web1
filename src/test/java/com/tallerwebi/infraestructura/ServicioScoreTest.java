/*package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Score;
import com.tallerwebi.dominio.servicio.ServicioScore;
import com.tallerwebi.dominio.servicio.ServicioScoreImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

public class ServicioScoreTest {
    private RepositorioScore repositorioScore;
    private RepositorioBanco repositorioBanco;
    private ServicioScore servicioScore;

    @BeforeEach
    public void setUp() {
        repositorioBanco = mock(RepositorioBanco.class);
        repositorioScore = mock(RepositorioScore.class);
        servicioScore = new ServicioScoreImpl(repositorioBanco);
    }

    @Test
    public void incrementarScore() {
        // Crear los objetos Score y Banco
        Banco banco = new Banco();
        banco.setNombreBanco("Banco 1");
        banco.setCiudad("Merlo");
        banco.setDireccion("aa123");
        banco.setEmail("aa@aaaa.com");
        banco.setPais("Argentina");
        banco.setPassword("aaaa");
        banco.setHorario("12.00");
        banco.setTelefono("12345678");

        Score score = new Score();
        score.setBanco(banco);
        score.setScore(10);

        // Configuración del mock para repositorioScore y repositorioBanco
        when(repositorioScore.obtenerScore(1L)).thenReturn(score);

        // Guardar banco y score
        servicioScore.guardarScore(score);

        // Incrementar el score
        servicioScore.incrementarScore(1);

        // Verificar que el score incrementó
        Score scoreObtenido = repositorioScore.obtenerScore(1L);
        assertThat(scoreObtenido.getScore(), equalTo(11));

        // Verificar que el método guardarScore fue llamado con el score actualizado
        verify(repositorioScore).guardarScoring(score);
    }

    @Test
    public void decrementarScore() throws Exception {
        // Crear los objetos Score y Banco
        Banco banco = new Banco();
        banco.setNombreBanco("Banco 1");
        banco.setCiudad("Merlo");
        banco.setDireccion("aa123");
        banco.setEmail("aa@aaaa.com");
        banco.setPais("Argentina");
        banco.setPassword("aaaa");
        banco.setHorario("12.00");
        banco.setTelefono("12345678");

        Score score = new Score();
        score.setBanco(banco);
        score.setScore(10);

        // Configuración del mock para repositorioScore y repositorioBanco
        when(repositorioScore.obtenerScore(1L)).thenReturn(score);

        // Guardar banco y score
        servicioScore.guardarScore(score);

        // Incrementar el score
        servicioScore.decrementarScore(1);

        // Verificar que el score incrementó
        Score scoreObtenido = repositorioScore.obtenerScore(1L);
        assertThat(scoreObtenido.getScore(), equalTo(9));

        // Verificar que el método guardarScore fue llamado con el score actualizado
        verify(repositorioScore).guardarScoring(score);
    }

    @Test
    public void obtenerScoringList() {
        ArrayList<Score> scoreList = new ArrayList<>();
        Score score = new Score();
        Score score2 = new Score();
        Banco banco = new Banco();
        Banco banco2 = new Banco();
        banco.setNombreBanco("Banco 1");
        banco.setCiudad("Merlo");
        banco.setDireccion("aa123");
        banco.setEmail("aa@aaaa.com");
        banco.setPais("Argentina");
        banco.setPassword("aaaa");
        banco.setHorario("12.00");
        banco.setTelefono("12345678");
        banco2.setNombreBanco("Banco 2");
        banco2.setCiudad("Merlo");
        banco2.setDireccion("aa123");
        banco2.setEmail("aa@aaaa.com");
        banco2.setPais("Argentina");
        banco2.setPassword("aaaa");
        banco2.setHorario("12.00");
        banco2.setTelefono("12345678");
        score.setBanco(banco);
        score.setScore(10);
        score2.setBanco(banco2);
        score2.setScore(20);
        scoreList.add(score);
        scoreList.add(score2);

        when(repositorioScore.obtenerScoring()).thenReturn(scoreList);

        servicioScore.guardarScore(score);
        servicioScore.guardarScore(score2);
        ArrayList<Score> scoreListObtenido = repositorioScore.obtenerScoring();

        assertThat(scoreListObtenido.size(), equalTo(2));
    }
}
*/