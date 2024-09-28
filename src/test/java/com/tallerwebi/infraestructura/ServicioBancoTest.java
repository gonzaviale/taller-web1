package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})

public class ServicioBancoTest {

    private final RepositorioBanco repositorioBancoMock = mock(RepositorioBanco.class);
    private final ServicioBancoImpl servicioBanco = new ServicioBancoImpl(repositorioBancoMock);

    @Rollback
    @Transactional
    @Test
    public void buscarBancoPorIdSiBancoExisteDeberiaRetornarBanco() {
        Banco bancoMock = mock(Banco.class);

        when(repositorioBancoMock.buscarPorId(1L)).thenReturn(bancoMock);

        Banco bancoEncontrado = servicioBanco.BuscarBancoId(1L);


        verify(repositorioBancoMock).buscarPorId(1L);
        assertEquals(bancoMock, bancoEncontrado);
    }

    @Rollback
    @Transactional
    @Test
    public void agregarPaqueteDeSangreSiBancoExisteDeberiaAgregarPaquete() {
        Banco bancoMock = mock(Banco.class);
        PaqueteDeSangre paquete = new PaqueteDeSangre("Tipo A", 10,"plaquetas" ,bancoMock);

        when(repositorioBancoMock.buscarPorId(1L)).thenReturn(bancoMock);

        servicioBanco.agregarPaqueteDeSangre(paquete, bancoMock);

        verify(repositorioBancoMock).guardarSangre(paquete, bancoMock);
    }
    @Rollback
    @Transactional
    @Test
    public void obtenerPaquetesDeSangrePorBancoDeberiaRetornarListaDePaquetes() {
        List<PaqueteDeSangre> paquetesMock = mock(List.class);

        when(repositorioBancoMock.obtenerPaquetesDeSangrePorBanco(1L)).thenReturn(paquetesMock);

        List<PaqueteDeSangre> paquetes = servicioBanco.obtenerPaquetesDeSangrePorBanco(1L);


        verify(repositorioBancoMock).obtenerPaquetesDeSangrePorBanco(1L);
        assertEquals(paquetesMock, paquetes);
    }



}
