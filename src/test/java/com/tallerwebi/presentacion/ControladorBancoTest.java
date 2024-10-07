package com.tallerwebi.presentacion;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
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
        // Ejecución
        String result = controladorBanco.logout(sessionMock);

        // Validación
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

}
