package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioPerfil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

public class ControladorPerfilTest {
    @InjectMocks
    private ControladorPerfil controladorPerfil;
    @Mock
    private ServicioPerfil servicioPerfil;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;


    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        when(request.getSession()).thenReturn(session);
    }
    @Test
    public void cuandoVoyAMiPerfilYTengoUnUsuarioLogeadoObtengoElUsuarioLogeado() {
        Usuario usuarioBuscado = givenPreparoLaRespuestaDeLaSesionYDelServicioParaQueMeDevuelvaElUsuaioConIdUno();

        ModelAndView result = controladorPerfil.irAMiPerfil(request);

        thenObtengoLaVistaPerfilYEncuentroAMiUsuarioBuscado(result, usuarioBuscado);
    }

    @Test
    public void cuandoIntentoAccederAMiPerfilYNoEstoyLogeadoMeRedirigueAHome() {
        givenMockeoElBuscarPorIdParaNoEncontrarUsuario();

        ModelAndView result = controladorPerfil.irAMiPerfil(request);

        thenMeRedirigueAHome(result);
    }


    private static void thenObtengoLaVistaPerfilYEncuentroAMiUsuarioBuscado(ModelAndView result, Usuario usuarioBuscado) {
        assertThat(result.getViewName(), equalTo("perfil"));
        assertThat(result.getModel().get("usuarioBuscado"), is(equalTo(usuarioBuscado)));
    }

    private Usuario givenPreparoLaRespuestaDeLaSesionYDelServicioParaQueMeDevuelvaElUsuaioConIdUno() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(request.getSession().getAttribute("usuarioEnSesion")).thenReturn(usuario);
        when(servicioPerfil.buscarUsuarioPorId(1L)).thenReturn(usuario);

        return usuario;

    }
    private void givenMockeoElBuscarPorIdParaNoEncontrarUsuario() {

        when(request.getSession().getAttribute("usuarioEnSesion")).thenReturn(null);
        when(servicioPerfil.buscarUsuarioPorId(1L)).thenReturn(null);
    }


    @Test
    public void muestroUnPerfilExistenteYaQueExisteEseUsuarioConEseId() {
        Usuario usuarioBuscado = givenPreparoLaRespuestaDeLaSesionYDelServicioParaQueMeDevuelvaElUsuaioConIdUno();

        ModelAndView result = controladorPerfil.mostrarPerfil(1L);

        thenObtengoLaVistaPerfilYEncuentroAMiUsuarioBuscado(result, usuarioBuscado);
    }

    @Test
    public void siIntentoAccederAUnPerfilInexistenteMeDevuelveAlHome() {
        givenMockeoElBuscarPorIdParaNoEncontrarUsuario();

        ModelAndView result = controladorPerfil.mostrarPerfil(1L);

        thenMeRedirigueAHome(result);
    }

    private void thenMeRedirigueAHome(ModelAndView result) {
        assertThat(result.getViewName(), equalTo("redirect:home"));
    }

}
