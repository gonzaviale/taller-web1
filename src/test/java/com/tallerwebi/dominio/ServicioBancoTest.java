package com.tallerwebi.dominio;
import com.tallerwebi.dominio.excepcion.BancoNoEncontrado;
import com.tallerwebi.infraestructura.ServicioBancoImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioBancoTest {

    private final RepositorioBanco repositorioBancoMock = mock(RepositorioBanco.class);
    private final ServicioBancoImpl servicioBanco = new ServicioBancoImpl(repositorioBancoMock);



    @Test
    public void agregarPaqueteDeSangreSiBancoExisteDeberiaAgregarPaquete() throws BancoNoEncontrado {
        // Preparar datos de prueba
        Banco bancoMock = mock(Banco.class);
        PaqueteDeSangre paquete = new PaqueteDeSangre("Tipo A", 10,"plaquetas" ,bancoMock);

        // Simula que el banco existe al buscar por id
        when(repositorioBancoMock.buscarPorId(1L)).thenReturn(bancoMock);

        // Ejecutar el método
        servicioBanco.agregarPaqueteDeSangre(paquete, bancoMock);

        // Verificar que el repositorio guarda el paquete de sangre
        verify(repositorioBancoMock).guardarSangre(paquete, bancoMock);
    }

    }
