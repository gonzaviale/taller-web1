package com.tallerwebi.presentacion;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;
import com.tallerwebi.dominio.servicio.ServicioBanco;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


public class ControladorBancoTest {

    private final ServicioBanco servicioBancoMock = mock(ServicioBanco.class);
    private final ControladorBanco controladorBanco = new ControladorBanco(servicioBancoMock);
    private final HttpSession sessionMock = mock(HttpSession.class);

    @Test
    public void deberiaRedirigirALoginSiNoHaySesion() {
        when(sessionMock.getAttribute("idBanco")).thenReturn(null);

        ModelAndView modelAndView = controladorBanco.BancoHome(sessionMock, null);

        assertThat(modelAndView.getViewName(), is(equalTo("redirect:/login")));
    }

    @Test
    public void deberiaRedirigirALogout() {
        String result = controladorBanco.logout(sessionMock);

        assertThat(result, is(equalTo("redirect:/login")));
        verify(sessionMock).invalidate();
    }

    @Test
    public void deberiaMostrarBancoHomeConBancoEncontrado() {
        // Preparación
        Long idBanco = 1L;
        Banco bancoMock = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        when(sessionMock.getAttribute("idBanco")).thenReturn(idBanco);
        when(servicioBancoMock.BuscarBancoId(idBanco)).thenReturn(bancoMock);

        // Ejecución
        ModelAndView modelAndView = controladorBanco.BancoHome(sessionMock, null);

        // Validación
        assertThat(modelAndView.getModel().get("nombreBanco"), is(equalTo("Banco Test")));
        assertThat(modelAndView.getModel().get("idBanco"), is(equalTo(idBanco)));
    }

    @Test
    public void deberiaAgregarPaqueteDeSangreYRedirigirAHome() {
        // Preparación
        Long idBanco = 1L;
        when(sessionMock.getAttribute("idBanco")).thenReturn(idBanco);
        PaqueteDeSangre paqueteMock = new PaqueteDeSangre("Tipo A", 10, "plaquetas", new Banco());
        when(servicioBancoMock.BuscarBancoId(idBanco)).thenReturn(new Banco());

        // Ejecución
        String result = controladorBanco.agregarPaqueteDeSangre(sessionMock, "Tipo A", 10, "plaquetas");

        // Validación
        assertThat(result, is(equalTo("redirect:/bancoHome?success=Paquete de sangre agregado con exito")));
        verify(servicioBancoMock).agregarPaqueteDeSangre(paqueteMock, new Banco());
    }

    @Test
    public void deberiaVerStockConPaquetesDeSangre() {
        // Preparación
        Long idBanco = 1L;
        when(sessionMock.getAttribute("idBanco")).thenReturn(idBanco);
        List<PaqueteDeSangre> paquetesMock = new ArrayList<>();
        when(servicioBancoMock.obtenerPaquetesDeSangrePorBanco(idBanco)).thenReturn(paquetesMock);

        // Ejecución
        ModelAndView modelAndView = controladorBanco.VerStock(sessionMock);

        // Validación
        assertEquals(paquetesMock, modelAndView.getModel().get("paquetes"));
    }
    @Test
    public void deberiaMostrarPeticionesConSolicitudes() {

        Long idBanco = 1L;
        when(sessionMock.getAttribute("idBanco")).thenReturn(idBanco);
        List<Solicitud> solicitudesMock = new ArrayList<>();
        solicitudesMock.add(new Solicitud(idBanco, 1L, "Plasma", "DEA 1.1+", 300));
        when(servicioBancoMock.obtenerSolicitudesXBanco(idBanco)).thenReturn(solicitudesMock);

        ModelAndView modelAndView = controladorBanco.BancoVerPeticiones(sessionMock);
        assertEquals(solicitudesMock, modelAndView.getModel().get("solicitudes"));
        assertNotNull(modelAndView.getModel().get("datosBanco"));
    }
    @Test
    public void deberiaMostrarSolicitudConPaquetesCompatibles() {
        // Preparación
        Long idBanco = 1L;
        int solicitudId = 1;
        when(sessionMock.getAttribute("idBanco")).thenReturn(idBanco);

        Solicitud solicitudMock = new Solicitud(idBanco, 1L, "Plasma", "DEA 1.1+", 300);
        List<PaqueteDeSangre> paquetesMock = new ArrayList<>();
        paquetesMock.add(new PaqueteDeSangre("Tipo A", 5, "plaquetas", new Banco()));

        when(servicioBancoMock.buscarSolicitud(solicitudId)).thenReturn(solicitudMock);
        when(servicioBancoMock.obtenerPaquetesDeSangreCompatibles(solicitudMock)).thenReturn(paquetesMock);

        ModelAndView modelAndView = controladorBanco.BancoVerPeticion(solicitudId, sessionMock);

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
        String result = controladorBanco.rechazarSolicitud(solicitudId, sessionMock);

        // Validación
        assertThat(result, is(equalTo("redirect:/verPeticiones")));
        verify(servicioBancoMock).rechazarSolicitud(solicitudId);
    }

    @Test
    public void deberiaAsignarPaqueteYRedirigirAPeticiones() {
        // Preparación
        int solicitudId = 1;
        int paqueteId = 1;
        when(sessionMock.getAttribute("idBanco")).thenReturn(1L);

        // Ejecución
        String result = controladorBanco.asignarPaquete(solicitudId, paqueteId, sessionMock);

        // Validación
        assertThat(result, is(equalTo("redirect:/verPeticiones")));
        verify(servicioBancoMock).asignarPaqueteASolicitud(solicitudId, paqueteId);
    }

}
