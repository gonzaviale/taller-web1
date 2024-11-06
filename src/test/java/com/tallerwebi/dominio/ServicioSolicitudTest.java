package com.tallerwebi.dominio;


import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Entrega;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;
import com.tallerwebi.dominio.servicio.ServicioSolicitudImpl;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import static org.mockito.Mockito.any;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})

public class ServicioSolicitudTest {

    private final RepositorioSolicitud repositorioSolicitudMock = mock(RepositorioSolicitud.class);
    private final RepositorioBanco repositorioBancoMock = mock(RepositorioBanco.class);

    private final ServicioSolicitudImpl servicioSolicitud = new ServicioSolicitudImpl( repositorioSolicitudMock , repositorioBancoMock );

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


    @Rollback
    @Transactional
    @Test
    public void asignarPaqueteASolicitudDeberiaLlamarSolicitudAprobar() {
        int solicitudId = 1;
        long paqueteId = 100L;

        // Crear objeto mock de la solicitud
        Solicitud solicitudMock = mock(Solicitud.class);
        when(solicitudMock.getBancoId()).thenReturn(1L);
        when(repositorioSolicitudMock.buscarSolicitudPorId(solicitudId)).thenReturn(solicitudMock);

        // Crear objeto mock del banco
        Banco bancoMock = mock(Banco.class);
        PaqueteDeSangre paqueteMock = mock(PaqueteDeSangre.class);
        when(bancoMock.getPaquete(paqueteId)).thenReturn(paqueteMock);
        when(repositorioBancoMock.buscarPorId(1L)).thenReturn(bancoMock);

        // Llamar al método
        servicioSolicitud.asignarPaqueteASolicitud(solicitudId, paqueteId);

        // Verificar que solicitudAprobar fue llamada
        verify(repositorioSolicitudMock).solicitudAprobar(solicitudId);
    }

    @Rollback
    @Transactional
    @Test
    public void asignarPaqueteASolicitudDeberiaObtenerBancoPorId() {
        int solicitudId = 1;
        long paqueteId = 100L;

        // Crear objetos mock
        Solicitud solicitudMock = mock(Solicitud.class);
        when(solicitudMock.getBancoId()).thenReturn(1L);
        when(repositorioSolicitudMock.buscarSolicitudPorId(solicitudId)).thenReturn(solicitudMock);

        Banco bancoMock = mock(Banco.class);
        PaqueteDeSangre paqueteMock = mock(PaqueteDeSangre.class);
        when(bancoMock.getPaquete(paqueteId)).thenReturn(paqueteMock);
        when(repositorioBancoMock.buscarPorId(1L)).thenReturn(bancoMock);

        // Llamar al método
        servicioSolicitud.asignarPaqueteASolicitud(solicitudId, paqueteId);

        // Verificar que repositorioBanco.buscarPorId fue llamado
        verify(repositorioBancoMock).buscarPorId(1L);
    }

    @Rollback
    @Transactional
    @Test
    public void asignarPaqueteASolicitudDeberiaEliminarPaqueteDelBanco() {
        int solicitudId = 1;
        long paqueteId = 100L;

        // Crear objetos mock
        Solicitud solicitudMock = mock(Solicitud.class);
        when(solicitudMock.getBancoId()).thenReturn(1L);
        when(repositorioSolicitudMock.buscarSolicitudPorId(solicitudId)).thenReturn(solicitudMock);

        Banco bancoMock = mock(Banco.class);
        PaqueteDeSangre paqueteMock = mock(PaqueteDeSangre.class);
        when(bancoMock.getPaquete(paqueteId)).thenReturn(paqueteMock);
        when(repositorioBancoMock.buscarPorId(1L)).thenReturn(bancoMock);


        servicioSolicitud.asignarPaqueteASolicitud(solicitudId, paqueteId);


        verify(bancoMock).eliminarPaquete(paqueteId);
    }

    @Rollback
    @Transactional
    @Test
    public void asignarPaqueteASolicitudDeberiaActualizarBanco() {
        int solicitudId = 1;
        long paqueteId = 100L;

        // Crear objetos mock
        Solicitud solicitudMock = mock(Solicitud.class);
        when(solicitudMock.getBancoId()).thenReturn(1L);
        when(repositorioSolicitudMock.buscarSolicitudPorId(solicitudId)).thenReturn(solicitudMock);

        Banco bancoMock = mock(Banco.class);
        PaqueteDeSangre paqueteMock = mock(PaqueteDeSangre.class);
        when(bancoMock.getPaquete(paqueteId)).thenReturn(paqueteMock);
        when(repositorioBancoMock.buscarPorId(1L)).thenReturn(bancoMock);

        // Llamar al método
        servicioSolicitud.asignarPaqueteASolicitud(solicitudId, paqueteId);

        // Verificar que el banco fue actualizado
        verify(repositorioBancoMock).actualizarBanco(bancoMock);
    }

    @Rollback
    @Transactional
    @Test
    public void asignarPaqueteASolicitudDeberiaGuardarEntrega() {
        int solicitudId = 1;
        long paqueteId = 100L;

        // Crear objetos mock
        Solicitud solicitudMock = mock(Solicitud.class);
        when(solicitudMock.getBancoId()).thenReturn(1L);
        when(repositorioSolicitudMock.buscarSolicitudPorId(solicitudId)).thenReturn(solicitudMock);

        Banco bancoMock = mock(Banco.class);
        PaqueteDeSangre paqueteMock = mock(PaqueteDeSangre.class);
        when(bancoMock.getPaquete(paqueteId)).thenReturn(paqueteMock);
        when(repositorioBancoMock.buscarPorId(1L)).thenReturn(bancoMock);

        // Llamar al método
        servicioSolicitud.asignarPaqueteASolicitud(solicitudId, paqueteId);


        verify(repositorioSolicitudMock).guardarEntrega(any(Entrega.class));
    }

}