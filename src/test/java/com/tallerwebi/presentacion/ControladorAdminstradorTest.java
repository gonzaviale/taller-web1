package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.entidad.Veterinario;
import com.tallerwebi.dominio.servicio.ServicioFiltro;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

public class ControladorAdminstradorTest {

    @Mock
    private ServicioFiltro servicioFiltro;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private ControladorAdministrador controladorAdministrador;

    private Usuario admin;

    @BeforeEach
    void setUp() {
        // Configuración de un usuario administrador de prueba
        admin = new Usuario();
        admin.setRol("administrador");
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void irAHomeAdministradorMeRedirigirSiNoHayAdminEnSesion() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("administrador")).thenReturn(null);

        ModelAndView modelAndView = controladorAdministrador.irAHomeAdministrador("", request);

        assertThat(modelAndView.getViewName(),equalToIgnoringCase("redirect:/home"));
    }

    @Test
    void irAHomeAdministrador_debeMostrarVistaAdminSiHayAdminEnSesion() {
        List<Usuario> veterinariosNoVerificados = new ArrayList<>();
        veterinariosNoVerificados.add(new Veterinario());
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("administrador")).thenReturn(admin);
        when(servicioFiltro.obtenerTodosLosVeterinariosNoVerificados()).thenReturn(veterinariosNoVerificados);

        ModelAndView modelAndView = controladorAdministrador.irAHomeAdministrador("", request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("administrador"));
        assertThat(modelAndView.getModel(), hasKey("usuarios")) ;
    }

    @Test
    void aceptarUsuario_debeRedirigirSiNoHayAdminEnSesion() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("administrador")).thenReturn(null);

        ModelAndView modelAndView = controladorAdministrador.aceptarUsuario(1L, request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
    }


    @Test
    void aceptarUsuario_debeRedirigirConMensajeSiActivacionEsExitosa() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("administrador")).thenReturn(admin);
        when(servicioFiltro.activarUsuarioBuscadoPor(1L)).thenReturn(true);

        ModelAndView modelAndView = controladorAdministrador.aceptarUsuario(1L, request);

        assertThat(modelAndView.getViewName(),equalToIgnoringCase("redirect:/administrador?mensaje=usuario-activado"));
    }

    @Test
    void aceptarUsuario_debeRedirigirConMensajeDeErrorSiActivacionFalla() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("administrador")).thenReturn(admin);
        when(servicioFiltro.activarUsuarioBuscadoPor(1L)).thenReturn(false);

        ModelAndView modelAndView = controladorAdministrador.aceptarUsuario(1L, request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/administrador?mensaje=error"));
    }

    @Test
    void rechazarUsuario_debeRedirigirSiNoHayAdminEnSesion() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("administrador")).thenReturn(null);

        ModelAndView modelAndView = controladorAdministrador.rechazarUsuario(1L, request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
    }

    @Test
    void rechazarUsuario_debeRedirigirConMensajeSiDesactivacionEsExitosa() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("administrador")).thenReturn(admin);
        when(servicioFiltro.desactivarUsuarioBuscadoPor(1L)).thenReturn(true);

        ModelAndView modelAndView = controladorAdministrador.rechazarUsuario(1L, request);

        assertThat(modelAndView.getViewName(),equalToIgnoringCase("redirect:/administrador?mensaje=usuario-inactivo"));
    }

    @Test
    void rechazarUsuario_debeRedirigirConMensajeDeErrorSiDesactivacionFalla() {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("administrador")).thenReturn(admin);
        when(servicioFiltro.desactivarUsuarioBuscadoPor(1L)).thenReturn(false);

        ModelAndView modelAndView = controladorAdministrador.rechazarUsuario(1L, request);

        assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/administrador?mensaje=error"));
    }




}
