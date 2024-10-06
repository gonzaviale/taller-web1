package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.servicio.ServicioImagenes;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorAnalisisMascotaTest {
    ServicioMascota servicioMascotaMock = mock(ServicioMascota.class);
    ServicioImagenes servicioImagenesMock = mock(ServicioImagenes.class);
    ControladorAnalisisMascota controladorAnalisisMascota = new ControladorAnalisisMascota(servicioImagenesMock, servicioMascotaMock);

    @Test
    public void queSePuedanObtenerLasMascotasEnRevision(){
        Mascota mascota1 = mock(Mascota.class);
        Mascota mascota2 = mock(Mascota.class);
        when(mascota1.isEnRevision()).thenReturn(true);
        when(mascota2.isEnRevision()).thenReturn(false);

        servicioMascotaMock.registrarMascota(mascota1);
        servicioMascotaMock.registrarMascota(mascota2);

        ModelAndView mav = controladorAnalisisMascota.verSolicitudesDonantes();

        assertEquals(mav.getViewName(), "ver-solicitudes-donantes");
    }
}