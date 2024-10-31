package com.tallerwebi.dominio;


import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;
import com.tallerwebi.dominio.servicioImpl.ServicioSolicitudImpl;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

public class ServicioSolicitudTest {

    private final RepositorioSolicitud repositorioSolicitudMock = mock(RepositorioSolicitud.class);
    private final ServicioSolicitudImpl servicioSolicitud = new ServicioSolicitudImpl( repositorioSolicitudMock );

    @Rollback
    @Transactional
    @Test
    public void agregarSolicitudDeberiaGuardarSolicitud() {
        Solicitud solicitud = new Solicitud(1L, 1L, "Sangre total", "DEA 1.1+", 5);
        when(repositorioSolicitudMock.guardarSolicitud(solicitud)).thenReturn(solicitud);

        Solicitud solicitudGuardada = servicioSolicitud.agregarSolicitud(solicitud);

        verify(repositorioSolicitudMock).guardarSolicitud(solicitud);
        assertEquals(solicitud, solicitudGuardada);
    }

    @Rollback
    @Transactional
    @Test
    public void buscarSolicitudSiSolicitudExisteDeberiaRetornarSolicitud() {
        Solicitud solicitudMock = mock(Solicitud.class);
        when(solicitudMock.getId()).thenReturn(1);

        when(repositorioSolicitudMock.buscarSolicitudPorId(1)).thenReturn(solicitudMock);

        Solicitud solicitudEncontrada = servicioSolicitud.buscarSolicitud(1);

        verify(repositorioSolicitudMock).buscarSolicitudPorId(1);
        assertEquals(solicitudMock, solicitudEncontrada);
    }


    @Rollback
    @Transactional
    @Test
    public void obtenerSolicitudesPorBancoDeberiaRetornarListaDeSolicitudes() {
        List<Solicitud> solicitudesMock = mock(List.class);

        when(repositorioSolicitudMock.solicitudesPorBanco(1L)).thenReturn(solicitudesMock);
        List<Solicitud> solicitudes = servicioSolicitud.obtenerSolicitudesXBanco(1L);

        verify(repositorioSolicitudMock).solicitudesPorBanco(1L);
        assertEquals(solicitudesMock, solicitudes);
    }


    @Rollback
    @Transactional
    @Test
    public void obtenerPaquetesDeSangreCompatiblesDeberiaRetornarListaDePaquetesCompatibles() {
        Solicitud solicitudMock = new Solicitud(1L, 1L, "Sangre total", "DEA 1.1+", 5);
        List<PaqueteDeSangre> paquetesMock = new ArrayList<>();
        paquetesMock.add(new PaqueteDeSangre("DEA 1.1+", 10, "Sangre total", null));
        paquetesMock.add(new PaqueteDeSangre("DEA 1.1+", 7, "Sangre total", null));

            when(repositorioSolicitudMock.obtenerPaquetesDeSangreCompatible(solicitudMock)).thenReturn(paquetesMock);

        List<PaqueteDeSangre> paquetesCompatibles = servicioSolicitud.obtenerPaquetesDeSangreCompatibles(solicitudMock);

        verify(repositorioSolicitudMock).obtenerPaquetesDeSangreCompatible(solicitudMock);
        assertEquals(paquetesMock, paquetesCompatibles);
    }


    @Rollback
    @Transactional
    @Test
    public void rechazarSolicitudSiSolicitudExisteDeberiaActualizarEstado() {

        Solicitud solicitudMock = new Solicitud(1L, 1L, "Sangre total", "DEA 1.1+", 1);
        when(repositorioSolicitudMock.buscarSolicitudPorId(1)).thenReturn(solicitudMock);

        servicioSolicitud.rechazarSolicitud(1);

            verify(repositorioSolicitudMock).rechazarSolicitud(1);
    }


}
