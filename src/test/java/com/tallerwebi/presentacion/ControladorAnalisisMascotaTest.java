package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.servicio.ServicioImagenes;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.eclipse.jetty.util.LazyList.contains;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorAnalisisMascotaTest {
    ServicioMascota servicioMascotaMock = mock(ServicioMascota.class);
    ServicioImagenes servicioImagenesMock = mock(ServicioImagenes.class);
    ControladorAnalisisMascota controladorAnalisisMascota = new ControladorAnalisisMascota(servicioImagenesMock, servicioMascotaMock);

    @Rollback
    @Test
    public void queSePuedanObtenerLasMascotasEnRevision(){
        Mascota mascota1 = mock(Mascota.class);
        Mascota mascota2 = mock(Mascota.class);
        when(mascota1.isEnRevision()).thenReturn(true);
        when(mascota2.isEnRevision()).thenReturn(false);

        servicioMascotaMock.registrarMascota(mascota1);
        servicioMascotaMock.registrarMascota(mascota2);

        ModelAndView mav = controladorAnalisisMascota.verSolicitudes();

        assertThat(mav.getViewName().toString(), equalToIgnoringCase("ver-solicitudes"));
    }

    @Rollback
    @Test
    public void queDevuelveMascotasConImagenes() {
        Mascota mascotaMock = mock(Mascota.class);
        when(mascotaMock.getId()).thenReturn(1L);
        when(mascotaMock.getNombre()).thenReturn("Firulais");

        List<Mascota> listaDeMascotas = Collections.singletonList(mascotaMock);
        List<String> imagenesDeMascota = Arrays.asList("imagen1.jpg", "imagen2.jpg");


        when(servicioMascotaMock.obtenerMascotasEnRevision()).thenReturn(listaDeMascotas);
        when(servicioImagenesMock.obtenerImagenesPorUsuario(1L)).thenReturn(imagenesDeMascota);

        ModelAndView mav = controladorAnalisisMascota.verSolicitudes();

        assertThat(mav.getViewName(), is("ver-solicitudes"));

        Map<Mascota, List<String>> mascotasConImagenes = (Map<Mascota, List<String>>) mav.getModel().get("mascotasConImagenes");

        assertThat(mascotasConImagenes, is(notNullValue()));
        assertThat(mascotasConImagenes.keySet(), hasItem(mascotaMock));

        // Verificar que las imágenes asociadas a la mascota son correctas
        assertThat(mascotasConImagenes.get(mascotaMock).get(0), equalToIgnoringCase("imagen1.jpg"));
        assertThat(mascotasConImagenes.get(mascotaMock), hasSize(2));
    }


    @Rollback
    @Test
    public void queSePuedaAprobarUnaMascota(){
        Mascota mascota = mock(Mascota.class);
        when(mascota.getId()).thenReturn(1l);

        servicioMascotaMock.registrarMascota(mascota);

        ModelAndView mav = controladorAnalisisMascota.aprobar(mascota.getId());

        assertThat(mav.getViewName().toString(), equalToIgnoringCase("redirect:/ver-solicitudes"));
    }

    @Rollback
    @Test
    public void queSePuedaRechazarUnaMascota(){
        Mascota mascota = mock(Mascota.class);
        when(mascota.getId()).thenReturn(1l);

        servicioMascotaMock.registrarMascota(mascota);

        ModelAndView mav = controladorAnalisisMascota.rechazar(mascota.getId());

        assertThat(mav.getViewName().toString(), equalToIgnoringCase("redirect:/ver-solicitudes"));
    }
}