package com.tallerwebi.dominio;
import com.tallerwebi.dominio.excepcion.BancoNoEncontrado;
import com.tallerwebi.infraestructura.ServicioBancoImp;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioBancoTest {

    private final RepositorioBanco repositorioBancoMock = mock(RepositorioBanco.class);
    private final ServicioBancoImp servicioBanco = new ServicioBancoImp(repositorioBancoMock);



    @Test
    public void agregarPaqueteDeSangreSiBancoExisteDeberiaAgregarPaquete() throws BancoNoEncontrado {

        Banco bancoMock = mock(Banco.class);
        PaqueteDeSangre paquete = new PaqueteDeSangre();

        when(repositorioBancoMock.buscarPorId(1L)).thenReturn(bancoMock);

        servicioBanco.agregarPaqueteDeSangre(1L, paquete);


        verify(repositorioBancoMock).guardar(bancoMock);
    }

    @Test
    public void agregarPaqueteDeSangreSiBancoNoExisteDeberiaLanzarExcepcion() {
        // Preparar datos de prueba
        PaqueteDeSangre paquete = new PaqueteDeSangre();

        // Simula que el banco no existe al buscar por id
        when(repositorioBancoMock.buscarPorId(1L)).thenReturn(null);

        // Ejecutar y verificar que se lanza la excepción
        assertThrows(BancoNoEncontrado.class, () -> {
            servicioBanco.agregarPaqueteDeSangre(1L, paquete);
        });
    }


    }
