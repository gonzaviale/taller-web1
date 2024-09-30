package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorAgregarMascotaTest {

    ServicioMascota servicioMascotaMock = mock(ServicioMascota.class);
    ControladorAgregarMascota agregarMascota = new ControladorAgregarMascota(servicioMascotaMock);
    Mascota mascotaMock = mock(Felino.class);
    Usuario usuarioMock = mock(DuenoMascota.class);
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void init() {
        when(mascotaMock.getNombre()).thenReturn("Manchas");
        when(mascotaMock.getAnios()).thenReturn(5);
        when(mascotaMock.getPeso()).thenReturn(6f);

        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);

        // Mock para devolver la sesión cuando se llame a request.getSession()
        when(requestMock.getSession()).thenReturn(sessionMock);
        // Mock para devolver el usuario cuando se llame a session.getAttribute("usuarioEnSesion")
        when(sessionMock.getAttribute("usuarioEnSesion")).thenReturn(usuarioMock);
    }

    @Test
    public void queUnDuenoPuedaAgregarUnaMascotaDonante() {
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), mascotaMock.getPeso(), "Felino", requestMock);

        thenRegistroExitoso(mav, "home");
        assertThat(mascotaMock.getDuenio().getId(), equalTo(usuarioMock.getId()));
    }

    private void thenRegistroExitoso(ModelAndView mav, String vista) {
        assertThat(mav.getViewName(), equalToIgnoringCase(vista));
    }
}