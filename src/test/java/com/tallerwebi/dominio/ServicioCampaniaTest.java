package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Campana;
import com.tallerwebi.dominio.servicioImpl.ServicioCampaniaImpl;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ServicioCampaniaTest {

    private final RepositorioCampania repositorioCampaniaMock = mock(RepositorioCampania.class);
    private final ServicioCampaniaImpl servicioCampania = new ServicioCampaniaImpl(repositorioCampaniaMock);

    @Test
    public void testObtenerCampanasActualesYProximas() {

        Campana campanaMock1 = new Campana();
        campanaMock1.setId(1L);
        campanaMock1.setFechaInicio(LocalDate.now().minusDays(1)); // Por ejemplo, comenzó ayer
        campanaMock1.setFechaFin(LocalDate.now().plusDays(1)); // Termina mañana

        Campana campanaMock2 = new Campana();
        campanaMock2.setId(2L);
        campanaMock2.setFechaInicio(LocalDate.now().plusDays(1)); // Comienza mañana
        campanaMock2.setFechaFin(LocalDate.now().plusDays(5)); // Termina en 5 días

        when(repositorioCampaniaMock.obtenerCampanasActualesYproximas(LocalDate.now())).thenReturn(List.of(campanaMock1, campanaMock2));

        List<Campana> campanas = servicioCampania.obtenerCampanasActualesYproximas();

        verify(repositorioCampaniaMock).obtenerCampanasActualesYproximas(LocalDate.now());

        assertEquals(2, campanas.size());
        assertTrue(campanas.contains(campanaMock1));
        assertTrue(campanas.contains(campanaMock2));
    }

    @Rollback
    @Transactional
    @Test
    public void guardarCampaniaDeberiaGuardarCampania() {
        Banco bancoMock = mock(Banco.class);
        Campana campanaMock = mock(Campana.class);


        servicioCampania.guardarCampania(campanaMock, bancoMock);

        // Assert
        verify(repositorioCampaniaMock).guardarCampania(campanaMock, bancoMock);
    }


}
