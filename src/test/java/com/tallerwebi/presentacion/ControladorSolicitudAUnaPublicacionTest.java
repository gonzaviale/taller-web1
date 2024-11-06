package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.SolicitudAUnaPublicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.PublicacionNoExistente;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import com.tallerwebi.dominio.servicio.ServicioPerfil;
import com.tallerwebi.dominio.servicio.ServicioPublicacion;
import com.tallerwebi.dominio.servicio.ServicioSolicitudAUnaPublicacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ControladorSolicitudAUnaPublicacionTest {
    ServicioMascota servicioMascotaMock = mock(ServicioMascota.class);
    ServicioPublicacion servicioPublicacionMock = mock(ServicioPublicacion.class);
    ServicioSolicitudAUnaPublicacion servicioSolicitudAUnaPublicacionMock = mock(ServicioSolicitudAUnaPublicacion.class);
    ControladorSolicitudAUnaPublicacion controladorSolicitudAUnaPublicacion = new ControladorSolicitudAUnaPublicacion(servicioMascotaMock, servicioSolicitudAUnaPublicacionMock, servicioPublicacionMock);
    Mascota mascotaDonante = mock(Mascota.class);
    Mascota mascotaReceptora = mock(Mascota.class);
    Publicacion publicacionMock = mock(Publicacion.class);
    SolicitudAUnaPublicacion solicitudAUnaPublicacionMock = mock(SolicitudAUnaPublicacion.class);
    Usuario vet = mock(Usuario.class);

    @BeforeEach
    public void init() {
        when(mascotaDonante.getId()).thenReturn(1l);
        when(mascotaReceptora.getId()).thenReturn(2l);
        when(publicacionMock.getId()).thenReturn(1l);
        when(solicitudAUnaPublicacionMock.getId()).thenReturn(1l);
        when(solicitudAUnaPublicacionMock.getPublicacion()).thenReturn(publicacionMock);
        when(vet.getRol()).thenReturn("Veterinario");
        when(vet.getNombre()).thenReturn("vet");
    }

    @Test
    public void queSePuedaRealizarUnaSolicitud() throws PublicacionNoExistente {
        ModelAndView mav = whenRealizarSolicitud(mascotaDonante.getId(), mascotaReceptora.getId(), publicacionMock.getId());

        thenExito(mav, "redirect:/home");
    }

    @Test
    public void queSePuedaAceptarUnaSolicitud() {
        ModelAndView mav = controladorSolicitudAUnaPublicacion.aceptarSolicitud(solicitudAUnaPublicacionMock.getId());

        thenExito(mav, "redirect:/miPerfil");
    }

    @Test
    public void queAlAceptarUnaSolicitudSeAsigneUnVeterinario() {
        ModelAndView mav = controladorSolicitudAUnaPublicacion.aceptarSolicitud(solicitudAUnaPublicacionMock.getId());
        when(solicitudAUnaPublicacionMock.getVeterinario()).thenReturn(vet);

        thenExito(mav, "redirect:/miPerfil");
        assertThat(solicitudAUnaPublicacionMock.getVeterinario().getNombre(), equalToIgnoringCase(vet.getNombre()));
    }

    @Test
    public void queSePuedaRechazarUnaSolicitud() {
        doNothing().when(servicioSolicitudAUnaPublicacionMock).rechazarSolicitud(anyLong());
        when(servicioSolicitudAUnaPublicacionMock.traerSolicitudPorId(anyLong())).thenReturn(solicitudAUnaPublicacionMock);
        ModelAndView mav = controladorSolicitudAUnaPublicacion.rechazarSolicitud(solicitudAUnaPublicacionMock.getId());

        thenExito(mav, "redirect:/miPerfil");
    }

    @Test
    public void queSiSeRechazaUnaSolicitudLaPublicacionVuelvaAEstarActiva() {
        doNothing().when(servicioSolicitudAUnaPublicacionMock).rechazarSolicitud(anyLong());
        when(servicioSolicitudAUnaPublicacionMock.traerSolicitudPorId(anyLong())).thenReturn(solicitudAUnaPublicacionMock);
        ModelAndView mav = controladorSolicitudAUnaPublicacion.rechazarSolicitud(solicitudAUnaPublicacionMock.getId());

        when(solicitudAUnaPublicacionMock.getPublicacion().getEstaActiva()).thenReturn(true);

        thenExito(mav, "redirect:/miPerfil");
        assertThat(solicitudAUnaPublicacionMock.getPublicacion().getEstaActiva().toString(), equalToIgnoringCase("true"));
    }

    @Test
    public void queSePuedanMarcarLasSolicitudesComoVistas() throws PublicacionNoExistente {
        when(solicitudAUnaPublicacionMock.getVista()).thenReturn(false);
        ModelAndView mav = controladorSolicitudAUnaPublicacion.solicitudVista(solicitudAUnaPublicacionMock.getId());
        thenExito(mav, "redirect:/miPerfil");
    }

    private ModelAndView whenRealizarSolicitud(Long mascotaDonante, Long mascotaReceptora, Long publicacion) throws PublicacionNoExistente {
        return controladorSolicitudAUnaPublicacion.realizarSolicitud(mascotaDonante, mascotaReceptora, publicacion);
    }

    private void thenExito(ModelAndView mav, String vista) {
        assertThat(mav.getViewName(), equalToIgnoringCase(vista));
    }

    private void thenFalla(ModelAndView mav, String vista, String errorKey, String error) {
        assertThat(mav.getViewName(), equalToIgnoringCase(vista));
        assertThat(mav.getModel().get(errorKey).toString(), equalToIgnoringCase(error));
    }
}
