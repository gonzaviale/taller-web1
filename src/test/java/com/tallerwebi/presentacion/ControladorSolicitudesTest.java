package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;
import com.tallerwebi.dominio.servicio.ServicioBanco;
import com.tallerwebi.dominio.servicio.ServicioSolicitud;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ControladorSolicitudesTest {

    private final ServicioSolicitud servicioSolicitudesMock = mock(ServicioSolicitud.class);
    private final ControladorSolicitudes controladorSolicitudes= new ControladorSolicitudes(servicioSolicitudesMock);
    private final HttpSession sessionMock = mock(HttpSession.class);

    @Test
    public void deberiaMostrarPeticionesConSolicitudes() {

        Long idBanco = 1L;
        when(sessionMock.getAttribute("idBanco")).thenReturn(idBanco);
        List<Solicitud> solicitudesMock = new ArrayList<>();
        solicitudesMock.add(new Solicitud(idBanco, 1L, "Plasma", "DEA 1.1+", 300));
        when(servicioSolicitudesMock.obtenerSolicitudesXBanco(idBanco)).thenReturn(solicitudesMock);

        ModelAndView modelAndView = controladorSolicitudes.BancoVerPeticiones(sessionMock);
        assertEquals(solicitudesMock, modelAndView.getModel().get("solicitudes"));
        assertNotNull(modelAndView.getModel().get("datosBanco"));
    }

    @Test
    public void deberiaMostrarPeticionesSinSolicitudes() {
        Long idBanco = 1L;
        when(sessionMock.getAttribute("idBanco")).thenReturn(idBanco);

        List<Solicitud> solicitudesVacias = new ArrayList<>();
        when(servicioSolicitudesMock.obtenerSolicitudesXBanco(idBanco)).thenReturn(solicitudesVacias);

        ModelAndView modelAndView = controladorSolicitudes.BancoVerPeticiones(sessionMock);

        assertEquals(solicitudesVacias, modelAndView.getModel().get("solicitudes"));
        assertNotNull(modelAndView.getModel().get("datosBanco"));
    }


    @Test
    public void deberiaMostrarSolicitudConPaquetesCompatibles() {
        Long idBanco = 1L;
        int solicitudId = 1;
        when(sessionMock.getAttribute("idBanco")).thenReturn(idBanco);

        Solicitud solicitudMock = new Solicitud(idBanco, 1L, "Plasma", "DEA 1.1+", 300);
        List<PaqueteDeSangre> paquetesMock = new ArrayList<>();
        paquetesMock.add(new PaqueteDeSangre("Tipo A", 5, "plaquetas", new Banco()));

        when(servicioSolicitudesMock.buscarSolicitud(solicitudId)).thenReturn(solicitudMock);
        when(servicioSolicitudesMock.obtenerPaquetesDeSangreCompatibles(solicitudMock)).thenReturn(paquetesMock);

        ModelAndView modelAndView = controladorSolicitudes.BancoVerPeticion(solicitudId, sessionMock);

        assertEquals(solicitudMock, modelAndView.getModel().get("solicitud"));
        assertEquals(paquetesMock, modelAndView.getModel().get("paquetes"));
        assertNotNull(modelAndView.getModel().get("datosBanco"));
    }

    @Test
    public void deberiaRechazarSolicitudYRedirigirAPeticiones() {
        // Preparación
        int solicitudId = 1;
        when(sessionMock.getAttribute("idBanco")).thenReturn(1L);

        // Ejecución
        String result = controladorSolicitudes.rechazarSolicitud(solicitudId, sessionMock);

        // Validación
        assertThat(result, is(equalTo("redirect:/verPeticiones")));
        verify(servicioSolicitudesMock).rechazarSolicitud(solicitudId);
    }

    @Test
    public void deberiaAsignarPaqueteYRedirigirAPeticiones() {
        // Preparación
        int solicitudId = 1;
        int paqueteId = 1;
        when(sessionMock.getAttribute("idBanco")).thenReturn(1L);

        // Ejecución
        String result = controladorSolicitudes.asignarPaquete(solicitudId, paqueteId, sessionMock);

        // Validación
        assertThat(result, is(equalTo("redirect:/verPeticiones")));
        verify(servicioSolicitudesMock).asignarPaqueteASolicitud(solicitudId, paqueteId);
    }



}
