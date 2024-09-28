package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.*;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorAgregarMascotaTest {
    ControladorAgregarMascota agregarMascota = new ControladorAgregarMascota();
    Mascota mascotaMock = mock(Felino.class);
    Usuario usuarioMock = mock(DuenoMascota.class);

    @Test
    public void queUnDuenoPuedaAgregarUnaMascotaDonante(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock, (DuenoMascota) usuarioMock);

        assertThat(mav.getViewName(), equalToIgnoringCase("home"));
        assertThat(mascotaMock.getDuenio().getId(), equalTo(usuarioMock.getId()));
    }
}