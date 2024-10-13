package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.servicio.ServicioPerfil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

        ModelAndView result = controladorPerfil.irAMiPerfil("",request,"");

        thenObtengoLaVistaPerfilYEncuentroAMiUsuarioBuscado(result, usuarioBuscado);
    }

    @Test
    public void cuandoIntentoAccederAMiPerfilYNoEstoyLogeadoMeRedirigueAHome() {
        givenMockeoElBuscarPorIdParaNoEncontrarUsuario();

        ModelAndView result = controladorPerfil.irAMiPerfil("",request,"");

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
        Usuario usuarioBuscado = givenMockeoElServicioYlaRequestParaQueMeDevuelvaUnUsuarioDiferenteAlDeLaRequest();

        ModelAndView result = controladorPerfil.mostrarPerfil(1L,request,"");

        thenMeRedirigueALaVistaPerfilYEncuentroAMiUsuarioBuscado(result, usuarioBuscado);
    }

    private Usuario givenMockeoElServicioYlaRequestParaQueMeDevuelvaUnUsuarioDiferenteAlDeLaRequest() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(request.getSession().getAttribute("usuarioEnSesion")).thenReturn(new Usuario());
        when(servicioPerfil.buscarUsuarioPorId(1L)).thenReturn(usuario);

        return usuario;

    }

    private void thenMeRedirigueALaVistaPerfilYEncuentroAMiUsuarioBuscado(ModelAndView result, Usuario usuarioBuscado) {
        assertThat(result.getViewName(), equalTo("perfil"));
        assertThat(result.getModel().get("user"), is(equalTo(usuarioBuscado)));
    }

    @Test
    public void siIntentoAccederAUnPerfilInexistenteMeDevuelveAlHome() {
        givenMockeoElBuscarPorIdParaNoEncontrarUsuario();

        ModelAndView result = controladorPerfil.mostrarPerfil(1L,request,"");

        thenMeRedirigueAHome(result);
    }

    private void thenMeRedirigueAHome(ModelAndView result) {
        assertThat(result.getViewName(), equalTo("redirect:/home"));
    }

    @Test
    public void irAMiPerfil_deberiaListarMascotasCuandoSePideListarMascotas() {
        Usuario usuarioEnSesion = new Usuario();
        usuarioEnSesion.setId(1L);
        usuarioEnSesion.setNombre("Juan");

        when(session.getAttribute("usuarioEnSesion")).thenReturn(usuarioEnSesion);
        when(servicioPerfil.buscarUsuarioPorId(usuarioEnSesion.getId())).thenReturn(usuarioEnSesion);
        List<Mascota> mascotas = Arrays.asList(new Mascota(), new Mascota());
        when(servicioPerfil.obtenerMascotasDelUsuario(usuarioEnSesion.getId())).thenReturn(mascotas);

        ModelAndView result = controladorPerfil.irAMiPerfil(null, request, "mascotas");

        // Verificar que las mascotas se añaden correctamente al modelo
        assertEquals("perfil", result.getViewName());
        assertThat((result.getModel().get("miperfil")), is(Boolean.TRUE));
        assertThat(result.getModel().get("usuarioBuscado"),is(usuarioEnSesion));
        assertThat(result.getModelMap(),hasEntry("listaMascotas", mascotas));
    }

    @Test
    public void irAMiPerfil_deberiaListarPublicacionesCuandoSePideListarPublicaciones() {
        // Preparar: Simular un usuario en sesión
        Usuario usuarioEnSesion = new Usuario();
        usuarioEnSesion.setId(1L);
        usuarioEnSesion.setNombre("Juan");

        when(session.getAttribute("usuarioEnSesion")).thenReturn(usuarioEnSesion);
        when(servicioPerfil.buscarUsuarioPorId(usuarioEnSesion.getId())).thenReturn(usuarioEnSesion);

        List<Publicacion> publicacions=Arrays.asList(new Publicacion(), new Publicacion());
        // Simular el servicio para obtener las publicaciones del usuario
        when(servicioPerfil.obtenerPublicacionesDelUsuario(usuarioEnSesion.getId())).thenReturn((publicacions));

        // Ejecutar el método a probar
        ModelAndView result = controladorPerfil.irAMiPerfil(null, request, "publicaciones");

        assertEquals("perfil", result.getViewName());
        assertThat((result.getModel().get("miperfil")), is(Boolean.TRUE));
        assertThat( result.getModel().get("usuarioBuscado"),is(usuarioEnSesion));
        assertThat(result.getModelMap(), hasEntry("publicaciones",publicacions));
    }

    @Test
    public void irAMiPerfil_deberiaRedirigirAlHomeSiNoHayUsuarioEnSesion() {
        // Preparar: Simular que no hay usuario en sesión
        when(session.getAttribute("usuarioEnSesion")).thenReturn(null);

        // Ejecutar el método a probar
        ModelAndView result = controladorPerfil.irAMiPerfil(null, request, null);

        // Verificar que se redirige a la página de inicio
        assertEquals("redirect:/home", result.getViewName());
    }

    @Test
    public void mostrarPerfil_deberiaRedirigirAMiPerfilSiElIdCoincideConUsuarioEnSesion() {
        // Preparar: Simular un usuario en sesión
        Usuario usuarioEnSesion = new Usuario();
        usuarioEnSesion.setId(1L);

        when(session.getAttribute("usuarioEnSesion")).thenReturn(usuarioEnSesion);

        // Ejecutar el método a probar
        ModelAndView result = controladorPerfil.mostrarPerfil(1L, request, null);

        // Verificar que se redirige al perfil del usuario en sesión
        assertEquals("redirect:/miPerfil", result.getViewName());
    }

    @Test
    public void mostrarPerfil_deberiaMostrarPerfilDeOtroUsuarioSiElIdNoCoincide() {
        // Preparar: Simular un usuario en sesión y otro usuario buscado
        Usuario usuarioEnSesion = new Usuario();
        usuarioEnSesion.setId(1L);

        Usuario usuarioBuscado = new Usuario();
        usuarioBuscado.setId(2L);

        when(session.getAttribute("usuarioEnSesion")).thenReturn(usuarioEnSesion);
        when(servicioPerfil.buscarUsuarioPorId(2L)).thenReturn(usuarioBuscado);
        List<Mascota> mascotaList=Arrays.asList(new Felino(), new Canino());

        when(servicioPerfil.obtenerMascotasDelUsuario(usuarioBuscado.getId())).thenReturn(mascotaList);

        // Ejecutar el método a probar
        ModelAndView result = controladorPerfil.mostrarPerfil(2L, request, "mascotas");

        // Verificar que se muestra el perfil del otro usuario
        assertEquals("perfil", result.getViewName());
        assertThat((result.getModel().get("miperfil")), is(Boolean.FALSE));
        assertThat( result.getModel().get("user"),is(usuarioBuscado));
        assertThat(result.getModel().get("listaMascotas"),is(mascotaList));

    }

    @Test
    public void mostrarPerfil_deberiaRedirigirAlHomeSiNoSeEncuentraElUsuarioBuscado() {
        // Preparar: Simular que el usuario buscado no existe
        when(servicioPerfil.buscarUsuarioPorId(2L)).thenReturn(null);

        // Ejecutar el método a probar
        ModelAndView result = controladorPerfil.mostrarPerfil(2L, request, null);

        // Verificar que se redirige a la página de inicio
        assertEquals("redirect:/home", result.getViewName());
    }

    @Test
    public void actualizarUsuario_deberiaActualizarUsuarioYRedirigirAPerfil() {
        // Preparar: Simular un usuario en sesión
        Usuario usuarioEnSesion = new Usuario();
        usuarioEnSesion.setEmail("juan@example.com");
        usuarioEnSesion.setNombre("Juan");
        usuarioEnSesion.setApellido("Pérez");
        usuarioEnSesion.setPassword("1234");

        when(request.getSession().getAttribute("usuarioEnSesion")).thenReturn(usuarioEnSesion);

        String nuevoEmail = "nuevoEmail@example.com";
        String nuevoNombre = "NuevoJuan";
        String nuevoApellido = "NuevoPérez";
        String nuevaPassword = "5678";

        doNothing().when(servicioPerfil).actualizarUsuario( any());

        ModelAndView result = controladorPerfil.actualizarUsuario(request, nuevoEmail, nuevoNombre, nuevoApellido, nuevaPassword);

        assertThat(nuevoEmail, is(usuarioEnSesion.getEmail()));
        assertThat(nuevoNombre, is(usuarioEnSesion.getNombre()));
        assertThat(nuevoApellido, is(usuarioEnSesion.getApellido()));
        assertThat(nuevaPassword, is(usuarioEnSesion.getPassword()));

        // Verificar el resultado de la redirección
        assertEquals("redirect:/miPerfil", result.getViewName());
        assertEquals("usuario actualizado", result.getModel().get("mensaje"));
    }
    @Test
    public void actualizarUsuario_noDeberiaActualizarYRedirigirALoginSiUsuarioEsNulo() {
        when(request.getSession().getAttribute("usuarioEnSesion")).thenReturn(null);

        ModelAndView result = controladorPerfil.actualizarUsuario(request, "nuevoEmail@example.com", "NuevoJuan", "NuevoPérez", "5678");

        verify(servicioPerfil, never()).actualizarUsuario(any());

        assertEquals("redirect:/login", result.getViewName());
    }

}
