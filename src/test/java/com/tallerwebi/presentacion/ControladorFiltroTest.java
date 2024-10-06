package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.servicio.ServicioFiltro;
import com.tallerwebi.dominio.entidad.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ControladorFiltroTest {
    private ControladorFiltro controladorFiltro;
    private Usuario usuarioMock;
    private Mascota mascotaMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    private ServicioFiltro servicioFiltroMock;

    @BeforeEach
    public void init(){
        servicioFiltroMock=mock(ServicioFiltro.class);
        usuarioMock=mock(Usuario.class);
        mascotaMock=mock(Mascota.class);
        requestMock=mock(HttpServletRequest.class);
        sessionMock=mock(HttpSession.class);
        controladorFiltro=new ControladorFiltro(servicioFiltroMock);
    }

    @Test
    public void deberiaEnviarALaVistaFiltro() {
        ModelAndView modelAndView = controladorFiltro.filtrarMascotas("nombre", "a", "Donante");
        assertThat(modelAndView.getViewName(), equalToIgnoringCase("filtrar"));
    }


    @Test
    public void deberiaRetornarArrayDeMascotas() {
        // Preparación
        ArrayList<Mascota> listaMock = new ArrayList<>();
        when(servicioFiltroMock.consultarMascota("nombre", "a", "Donante")).thenReturn(listaMock);

        // Ejecución
        ModelAndView modelAndView = controladorFiltro.filtrarMascotas("nombre", "a", "Donante");

        // Validación
        assertEquals(listaMock, modelAndView.getModel().get("listaMascotas"));

        // Verificar que el servicio fue llamado correctamente
        verify(servicioFiltroMock).consultarMascota("nombre", "a", "Donante");
    }
}
