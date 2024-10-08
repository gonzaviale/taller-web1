package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.servicio.ServicioScore;
import com.tallerwebi.dominio.servicio.ServicioScoreImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioScoreTest {
    private RepositorioBanco repositorioBanco;
    private ServicioScore servicioScore;

    @BeforeEach
    public void setUp() {
        repositorioBanco = mock(RepositorioBanco.class);
        servicioScore = new ServicioScoreImpl(repositorioBanco);
    }

    @Test
    public void incrementarScore() {
        Banco banco = new Banco();
        banco.setId(1L);
        banco.setNombreBanco("Banco 1");
        banco.setCiudad("Merlo");
        banco.setDireccion("aa123");
        banco.setEmail("aa@aaaa.com");
        banco.setPais("Argentina");
        banco.setPassword("aaaa");
        banco.setHorario("12.00");
        banco.setTelefono("12345678");

        when(repositorioBanco.guardar(banco)).thenReturn(banco);
        when(repositorioBanco.buscarPorId(1L)).thenReturn(banco);
        ArrayList<Banco> bancoList = new ArrayList<>();
        bancoList.add(banco);
        when(repositorioBanco.searchBankByScore()).thenReturn(bancoList);

        repositorioBanco.guardar(banco);
        verify(repositorioBanco).guardar(banco);

        servicioScore.incrementarScore(1);
        verify(repositorioBanco).actualizarBanco(banco);

        ArrayList<Banco> bancoArrayList = servicioScore.obtenerScoring("");
        verify(repositorioBanco).searchBankByScore();

        assertThat(bancoArrayList, notNullValue());
        assertThat(bancoArrayList.size(), equalTo(1));
        assertThat(bancoArrayList.get(0).getPuntos(), equalTo(1));
    }

    @Test
    public void decrementarScore() throws Exception {
        Banco banco = new Banco();
        banco.setId(1L);
        banco.setNombreBanco("Banco 1");
        banco.setCiudad("Merlo");
        banco.setDireccion("aa123");
        banco.setEmail("aa@aaaa.com");
        banco.setPais("Argentina");
        banco.setPassword("aaaa");
        banco.setHorario("12.00");
        banco.setTelefono("12345678");

        when(repositorioBanco.guardar(banco)).thenReturn(banco);
        when(repositorioBanco.buscarPorId(1L)).thenReturn(banco);
        ArrayList<Banco> bancoList = new ArrayList<>();
        bancoList.add(banco);
        when(repositorioBanco.searchBankByScore()).thenReturn(bancoList);

        repositorioBanco.guardar(banco);
        verify(repositorioBanco).guardar(banco);

        servicioScore.incrementarScore(1);
        servicioScore.decrementarScore(1);
        verify(repositorioBanco, times(2)).actualizarBanco(banco);

        ArrayList<Banco> bancoArrayList = servicioScore.obtenerScoring("");
        verify(repositorioBanco).searchBankByScore();

        assertThat(bancoArrayList, notNullValue());
        assertThat(bancoArrayList.size(), equalTo(1));
        assertThat(bancoArrayList.get(0).getPuntos(), equalTo(0));
    }

    @Test
    public void obtenerExceptionAlIncrementarBancoInexistente() throws RuntimeException {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicioScore.incrementarScore(1);
        });

        assertThat(exception.getMessage(), equalTo("No se puede incrementar el score del banco"));
    }

    @Test
    public void obtenerExceptionAlDecrementarBancoInexistente(){
        Exception exception = assertThrows(Exception.class, () -> {
            servicioScore.decrementarScore(1);
        });

        assertThat(exception.getMessage(), equalTo("No se puede decrementar el score de un banco inexistente"));
    }

    @Test
    public void obtenerExceptionAlDecrementarBancoQueTienePuntosIgualACero(){

        Exception exception = assertThrows(Exception.class, () -> {
            Banco banco = new Banco();
            banco.setId(1L);
            banco.setNombreBanco("Banco 1");
            banco.setCiudad("Merlo");
            banco.setDireccion("aa123");
            banco.setEmail("aa@aaaa.com");
            banco.setPais("Argentina");
            banco.setPassword("aaaa");
            banco.setHorario("12.00");
            banco.setTelefono("12345678");

            when(repositorioBanco.guardar(banco)).thenReturn(banco);
            when(repositorioBanco.buscarPorId(1L)).thenReturn(banco);

            repositorioBanco.guardar(banco);
            verify(repositorioBanco).guardar(banco);

            servicioScore.decrementarScore(1);
            verify(repositorioBanco).actualizarBanco(banco);
        });
        assertThat(exception.getMessage(), equalTo("No se puede decrementar el score"));
    }
}
