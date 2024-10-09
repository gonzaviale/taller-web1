package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.entidad.Felino;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioImagenes;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorAgregarMascotaTest {

    ServicioMascota servicioMascotaMock = mock(ServicioMascota.class);
    ServicioImagenes servicioImagenesMock =  mock(ServicioImagenes.class);
    ControladorAgregarMascota agregarMascota = new ControladorAgregarMascota(servicioMascotaMock, servicioImagenesMock);
    Mascota mascotaMock = mock(Felino.class);
    Usuario usuarioMock = mock(Usuario.class);
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    MultipartFile[] imagenesMock;

    @BeforeEach
    public void init() throws IOException {
        when(mascotaMock.getNombre()).thenReturn("Manchas");
        when(mascotaMock.getAnios()).thenReturn(5);
        when(mascotaMock.getPeso()).thenReturn(6f);
        when(mascotaMock.getTipo()).thenReturn("Felino");

        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);

        // Mock para devolver la sesión cuando se llame a request.getSession()
        when(requestMock.getSession()).thenReturn(sessionMock);
        // Mock para devolver el usuario cuando se llame a session.getAttribute("usuarioEnSesion")
        when(sessionMock.getAttribute("usuarioEnSesion")).thenReturn(usuarioMock);

        MultipartFile imagen1 = mock(MultipartFile.class);
        MultipartFile imagen2 = mock(MultipartFile.class);

        // Configura el comportamiento del mock
        when(imagen1.getOriginalFilename()).thenReturn("imagen1.jpg");
        when(imagen1.isEmpty()).thenReturn(false);
        when(imagen1.getBytes()).thenReturn(new byte[]{1, 2, 3}); // Simulación de bytes de la imagen

        when(imagen2.getOriginalFilename()).thenReturn("imagen2.png");
        when(imagen2.isEmpty()).thenReturn(false);
        when(imagen2.getBytes()).thenReturn(new byte[]{4, 5, 6}); // Simulación de bytes de la imagen

        imagenesMock = new MultipartFile[]{imagen1, imagen2};
    }

    @Test
    public void queUnDuenoPuedaAgregarUnaMascotaDonante() {
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), mascotaMock.getPeso(), mascotaMock.getTipo(), "No", requestMock);

        thenRegistroExitoso(mav, "redirect:/home");
        assertThat(mascotaMock.getDuenio().getId(), equalTo(usuarioMock.getId()));
    }

    @Test
    public void queUnDuenoNoPuedaAgregarUnaMascotaDonanteSiFueTransfundida() {
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), mascotaMock.getPeso(), mascotaMock.getTipo(), "Si", requestMock);

        thenRegistroFalla(mav, "agregar-mascota-donante", "errorTransfusion", "Un animal que ya recibió una transfusión no puede ser donante");
    }

    private void thenRegistroExitoso(ModelAndView mav, String vista) {
        assertThat(mav.getViewName(), equalToIgnoringCase(vista));
    }

    private void thenRegistroFalla(ModelAndView mav, String vista, String errorKey, String error) {
        assertThat(mav.getViewName(), equalToIgnoringCase(vista));
        assertThat(mav.getModel().get(errorKey).toString(), equalToIgnoringCase(error));
    }
}