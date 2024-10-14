package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.entidad.Felino;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioImagenes;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
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
    ServicioImagenes servicioImagenesMock = mock(ServicioImagenes.class);
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

    @Rollback
    @Test
    public void queUnDuenoPuedaAgregarUnaMascotaDonante() {
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), mascotaMock.getPeso(), mascotaMock.getTipo(), "No", imagenesMock, requestMock);

        thenRegistroExitoso(mav, "redirect:/home");
        assertThat(mascotaMock.getDuenio().getId(), equalTo(usuarioMock.getId()));
    }

    @Test
    public void queUnDuenoNoPuedaAgregarUnaMascotaDonanteSiFueTransfundida() {
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), mascotaMock.getPeso(), mascotaMock.getTipo(), "Si", imagenesMock, requestMock);

        thenRegistroFalla(mav, "agregar-mascota-donante", "errorTransfusion", "Un animal que ya recibió una transfusión no puede ser donante");
    }

    @Rollback
    @Test
    public void queUnDuenoPuedaAgregarUnaMascotaReceptora() {
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarReceptora(mascotaMock.getNombre(), mascotaMock.getAnios(), mascotaMock.getPeso(), mascotaMock.getTipo(), imagenesMock, requestMock);

        thenRegistroExitoso(mav, "redirect:/home");
        assertThat(mascotaMock.getDuenio().getId(), equalTo(usuarioMock.getId()));
    }

    @Test
    public void queNoSePuedaRegistrarMascotaSiNoSeEnvianLasImagenesDeLosEstudios(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);
        MultipartFile[] imagenesVacias = new MultipartFile[0];

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), mascotaMock.getPeso(), mascotaMock.getTipo(), "No", imagenesVacias, requestMock);

        thenRegistroFalla(mav, "agregar-mascota-donante", "errorImagenes", "Una mascota no se puede registrar sin imágenes de sus estudios");
    }

    @Test
    public void queNoSePuedaRegistrarMascotaSiElNombreEstaVacio(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante("", mascotaMock.getAnios(), mascotaMock.getPeso(), mascotaMock.getTipo(), "No", imagenesMock, requestMock);

        thenRegistroFalla(mav, "agregar-mascota-donante", "errorNombre", "El nombre de la mascota es obligatorio");
    }

    @Test
    public void queNoSePuedaRegistrarMascotaSiLaEdadEstaVacia(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), 0, mascotaMock.getPeso(), mascotaMock.getTipo(), "No", imagenesMock, requestMock);

        thenRegistroFalla(mav, "agregar-mascota-donante", "errorEdad", "La edad de la mascota es obligatoria");
    }

    @Test
    public void queNoSePuedaRegistrarMascotaSiElPesoEstaVacio(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), 0f, mascotaMock.getTipo(), "No", imagenesMock, requestMock);

        thenRegistroFalla(mav, "agregar-mascota-donante", "errorPeso", "El peso de la mascota es obligatorio");
    }

    private void thenRegistroExitoso(ModelAndView mav, String vista) {
        assertThat(mav.getViewName(), equalToIgnoringCase(vista));
    }

    private void thenRegistroFalla(ModelAndView mav, String vista, String errorKey, String error) {
        assertThat(mav.getViewName(), equalToIgnoringCase(vista));
        assertThat(mav.getModel().get(errorKey).toString(), equalToIgnoringCase(error));
    }
}