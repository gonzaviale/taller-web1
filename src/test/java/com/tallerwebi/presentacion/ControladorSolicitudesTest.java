package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;
import com.tallerwebi.dominio.servicio.ServicioBanco;
import com.tallerwebi.dominio.servicio.ServicioSolicitud;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ControladorSolicitudesTest {

    private final ServicioSolicitud servicioSolicitudesMock = mock(ServicioSolicitud.class);
    private final ControladorSolicitudes controladorSolicitudes= new ControladorSolicitudes(servicioSolicitudesMock);
    private final HttpSession sessionMock = mock(HttpSession.class);
    private final Model modelMock = mock(Model.class);
    private final RedirectAttributes redirectAttributesMock = mock(RedirectAttributes.class);


    @Test
    public void deberiaMostrarPeticionesConSolicitudes() {
        Long idBanco = 1L;
        when(sessionMock.getAttribute("idBanco")).thenReturn(idBanco);

        List<Solicitud> solicitudesMock = new ArrayList<>();
        solicitudesMock.add(new Solicitud(idBanco, 1L, "Plasma", "DEA 1.1+", 300));

        when(servicioSolicitudesMock.obtenerSolicitudesXBanco(idBanco)).thenReturn(solicitudesMock);

        RedirectAttributes redirectAttributesMock = mock(RedirectAttributes.class);

        ModelAndView modelAndView = controladorSolicitudes.BancoVerPeticiones(sessionMock, "Solicitud aprobada exitosamente");

        assertEquals("Solicitud aprobada exitosamente", modelAndView.getModel().get("mensaje"));

        assertEquals(solicitudesMock, modelAndView.getModel().get("solicitudes"));

        assertNotNull(modelAndView.getModel().get("datosBanco"));
    }

    @Test
    public void deberiaMostrarPeticionesSinSolicitudes() {
        Long idBanco = 1L;
        when(sessionMock.getAttribute("idBanco")).thenReturn(idBanco);


        List<Solicitud> solicitudesVacias = new ArrayList<>();


        when(servicioSolicitudesMock.obtenerSolicitudesXBanco(idBanco)).thenReturn(solicitudesVacias);


        RedirectAttributes redirectAttributesMock = mock(RedirectAttributes.class);

        ModelAndView modelAndView = controladorSolicitudes.BancoVerPeticiones(sessionMock, "");

        assertEquals(solicitudesVacias, modelAndView.getModel().get("solicitudes"));

        assertNotNull(modelAndView.getModel().get("datosBanco"));

        assertNull(modelAndView.getModel().get("mensaje"));
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
        String result = controladorSolicitudes.asignarPaquete(solicitudId, paqueteId, sessionMock, redirectAttributesMock);

        // Validación
        assertThat(result, is(equalTo("redirect:/verPeticiones")));
        verify(servicioSolicitudesMock).asignarPaqueteASolicitud(solicitudId, paqueteId);
    }


    @Test
    public void deberiaMostrarFormularioSolicitudConDatosCorrectos() {
        // Preparación
        Long bancoId = 1L;
        String tipoProducto = "Sangre total";
        String tipoSangre = "DEA 1.1+";
        int cantidad = 200;
        Long usuarioId = 2L;

        when(sessionMock.getAttribute("usuarioId")).thenReturn(usuarioId);

        // Ejecución
        String vista = controladorSolicitudes.mostrarFormularioSolicitud(bancoId, tipoProducto, tipoSangre, cantidad, modelMock, sessionMock);

        // Validación
        assertEquals("crearSolicitud", vista);
        verify(modelMock).addAttribute("usuarioId", usuarioId);
        verify(modelMock).addAttribute("bancoId", bancoId);
        verify(modelMock).addAttribute("tipoProducto", tipoProducto);
        verify(modelMock).addAttribute("tipoSangre", tipoSangre);
        verify(modelMock).addAttribute("cantidad", cantidad);
    }

    @Test
    public void deberiaProcesarSolicitudYRedirigirAHome() {
        // Preparación
        Long bancoId = 1L;
        Long usuarioId = 2L;
        String tipoProducto = "Plasma";
        String tipoSangre = "DEA 1.2-";
        int cantidad = 150;

        // Ejecución
        String vista = controladorSolicitudes.procesarSolicitud(bancoId, usuarioId, tipoProducto, tipoSangre, cantidad, redirectAttributesMock);

        // Validación
        assertEquals("redirect:/home", vista);

        // Verificar que se crea una solicitud con los datos correctos y que el método agregarSolicitud fue llamado
        verify(servicioSolicitudesMock).agregarSolicitud(argThat(solicitud ->
                Objects.equals(solicitud.getBancoId(), bancoId) &&
                        Objects.equals(solicitud.getUsuarioId(), usuarioId) &&
                        solicitud.getTipoProducto().equals(tipoProducto) &&
                        solicitud.getTipoSangre().equals(tipoSangre) &&
                        solicitud.getCantidad() == cantidad
        ));
    }

}
