package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import com.tallerwebi.dominio.servicio.ServicioPublicacion;

import org.mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ControladorPublicacionTest {

    private ControladorPublicacion controladorPublicacion;
    private ServicioPublicacion servicioPublicacion;
    private ServicioMascota servicioMascota;
    private HttpServletRequest request;
    private HttpSession session;
/*
    @BeforeEach
    public void init() {
        // Crear los mocks manualmente
        servicioPublicacion = mock(ServicioPublicacion.class);
        servicioMascota = mock(ServicioMascota.class);
        request = mock(HttpServletRequest.class);
        session = mock(HttpSession.class);

        // Injectar el mock manualmente en el controlador
        controladorPublicacion = new ControladorPublicacion(servicioPublicacion, servicioMascota);

        // Configurar el comportamiento de los mocks
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void siNoIngresaCamposSangreOLoIngresaVacioNoSePodraRealizarLaPublicacion() throws PublicacionSinTipoDeSangre, PublicacionNoValida, PublicacionSinTipoDePublicacion, PublicacionSinTitulo {

        //given
        Publicacion nuevaPublicacionNoValida = givenCreoUnaPublicacionNoValidaSinCampoSangre();
        doThrow(new PublicacionSinTipoDeSangre()).when(servicioPublicacion).guardarPublicacion(ArgumentMatchers.any(Publicacion.class));
        //when
        ModelAndView vistaError = whenRealizoUnaPublicacion(nuevaPublicacionNoValida);
        //then
        thenLaPubliacionNoEsRegistradaCuandoElCampoSangreEsVacio(vistaError);
    }

    private Publicacion givenCreoUnaPublicacionNoValidaSinCampoSangre() {
        String campoDeSangre="";
        String tipoDePublicion="busqueda";
        Publicacion nuevaPublicacion= new Publicacion();
        nuevaPublicacion.setTipoDeSangre(campoDeSangre);
        nuevaPublicacion.setTipoDePublicacion(tipoDePublicion);
        return nuevaPublicacion;
    }

    private ModelAndView whenRealizoUnaPublicacion(Publicacion nuevaPublicacion) {
        return controladorPublicacion.publicarPublicacion(nuevaPublicacion, 1l, request);
    }

    private void thenLaPubliacionNoEsRegistradaCuandoElCampoSangreEsVacio(ModelAndView ventanaError) {
        assertThat(ventanaError.getViewName(), is(equalToIgnoringCase("redirect:/home")));
        assertThat(ventanaError.getModel().get("mensaje").toString(), is(equalToIgnoringCase("Publicacion no registrada: el campo sangre no puede estar vacio")));
    }

    @Test
    public void siIngresaCampoSangreSePodraRealizarLaPublicacion() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo {

        //given
        Publicacion nuevaPublicacionValida = givenCreoUnaPublicacionValida();
        //when
        ModelAndView publicacionExitosa = whenRealizoUnaPublicacion(nuevaPublicacionValida);
        //then
        thenLaPubliacionEsRegistrada(publicacionExitosa);
    }

    @Test
    public void siIngresoLosCamposRequeridosAdemasDeAgregarleUnUsuario() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo {

        //given
        Publicacion nuevaPublicacionValida = givenCreoUnaPublicacionValida();
        //when
        ModelAndView publicacionExitosa = whenRealizoUnaPublicacion(nuevaPublicacionValida);
        //then
        thenLaPubliacionEsRegistrada(publicacionExitosa);
    }

    private void thenLaPubliacionEsRegistrada(ModelAndView miPublicacion) throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo {
        assertThat(miPublicacion.getViewName(), is(equalTo("redirect:/home")));
        assertThat(miPublicacion.getModel().get("mensaje").toString(), is("la publicacion fue registrada correctamente"));
        verify(servicioPublicacion, times(1)).guardarPublicacion(ArgumentMatchers.any());
    }

    @Test
    public void queCuandoElRegistroSeaExitosoMeDeAltaUnaNuevaPublicacion() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo {
        //given
        Publicacion nuevaPublicacionValida = givenCreoUnaPublicacionValidaAdemasDeMockearRequest();
        //when
        ModelAndView miPublicacion = whenRealizoUnaPublicacion(nuevaPublicacionValida);
        //then
        thenLaPubliacionEsRegistradaContieneUsuario(miPublicacion,nuevaPublicacionValida);
    }

    @Test
    public void queCuandoRegistroUnaPublicacionElRegistroNoEsExitosoYaQueNoTieneUsuario() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo {
        //given
        Publicacion nuevaPublicacionValida = givenCreoUnaPublicacionValidaAdemasDeMockearRequestPeroNoTieneUsuario();
        //when
        ModelAndView miPublicacion = whenRealizoUnaPublicacion(nuevaPublicacionValida);
        //then
        thenLaPubliacionEsRegistradaNoContieneUsuario(miPublicacion,nuevaPublicacionValida);
    }

    private void thenLaPubliacionEsRegistradaNoContieneUsuario(ModelAndView miPublicacion, Publicacion nuevaPublicacionValida) {
        assertThat(nuevaPublicacionValida.getDuenioPublicacion(), nullValue());
    }


    private void thenLaPubliacionEsRegistradaContieneUsuario(ModelAndView miPublicacion,Publicacion publicacionValida) throws PublicacionSinTipoDeSangre, PublicacionNoValida, PublicacionSinTitulo, PublicacionSinTipoDePublicacion {
        assertThat(miPublicacion.getViewName(), is(equalTo("redirect:/home")));
        assertThat(miPublicacion.getModel().get("mensaje").toString(), is("la publicacion fue registrada correctamente"));
        verify(servicioPublicacion, times(1)).guardarPublicacion(ArgumentMatchers.any());
        assertThat(publicacionValida.getDuenioPublicacion(), notNullValue());
    }

    private Publicacion givenCreoUnaPublicacionValidaAdemasDeMockearRequestPeroNoTieneUsuario() {
        String campoDeSangre = "DEA-1.1";
        String tipoDePublicion = "busqueda";
        Publicacion nuevaPublicacion = new Publicacion();
        nuevaPublicacion.setTipoDeSangre(campoDeSangre);
        nuevaPublicacion.setTipoDePublicacion(tipoDePublicion);

        when(request.getSession().getAttribute("usuarioEnSesion")).thenReturn(null);

        return nuevaPublicacion;
    }


    private Publicacion givenCreoUnaPublicacionValidaAdemasDeMockearRequest() {
        String campoDeSangre = "DEA-1.1";
        String tipoDePublicion = "busqueda";
        Publicacion nuevaPublicacion = new Publicacion();
        nuevaPublicacion.setTipoDeSangre(campoDeSangre);
        nuevaPublicacion.setTipoDePublicacion(tipoDePublicion);
        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(request.getSession().getAttribute("usuarioEnSesion")).thenReturn(usuario);

        return nuevaPublicacion;
    }


    private Publicacion givenCreoUnaPublicacionValida() {
        String campoDeSangre = "DEA-1.1";
        String tipoDePublicion = "busqueda";
        Publicacion nuevaPublicacion = new Publicacion();
        nuevaPublicacion.setTipoDeSangre(campoDeSangre);
        nuevaPublicacion.setTipoDePublicacion(tipoDePublicion);
        return nuevaPublicacion;
    }

    @Test
    public void queNoMePermitaDarDeAltaUnaPublicacionConUnaSangreSinTipo() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo {
        //given
        Publicacion nuevaPublicacionNoValida = givenCreoUnaPublicacionNoValida();
        doThrow(new PublicacionSinTipoDeSangre()).when(servicioPublicacion).guardarPublicacion(ArgumentMatchers.any(Publicacion.class));
        //when
        ModelAndView mav = whenRealizoUnaPublicacion(nuevaPublicacionNoValida);
        //then
        thenLaPubliacionNoEsRegistradaCuandoElCampoSangreEsVacio(mav);
    }

    @Test
    public void queNoMePermitaRealizaUnaPublicacionSinTipoDepublicacionYConSuCampoDeSangreVacio() throws PublicacionSinTipoDeSangre, PublicacionNoValida, PublicacionSinTipoDePublicacion, PublicacionSinTitulo {
        //given
        Publicacion nuevaPublicacionNoValida = givenCreoUnaPublicacionNoValida();
        doThrow(new PublicacionNoValida()).when(servicioPublicacion).guardarPublicacion(ArgumentMatchers.any(Publicacion.class));
        //when
        ModelAndView vistaError = whenRealizoUnaPublicacion(nuevaPublicacionNoValida);
        //then
        thenLaPubliacionNoEsRegistradaCuandoElCampoSangreEsVacioYElTipoDePublicacionNoEsValida(vistaError);

    }

    private void thenLaPubliacionNoEsRegistradaCuandoElCampoSangreEsVacioYElTipoDePublicacionNoEsValida(ModelAndView vistaError) {
        assertThat(vistaError.getViewName(), is(equalToIgnoringCase("redirect:/home")));
        assertThat(vistaError.getModel().get("mensaje").toString(), is(equalToIgnoringCase("Publicacion no registrada: el campo tipo de publicacion y el campo de sangre no puede estar vacio")));
    }

    @Test
    public void queMeLanzeUnaExepcionDeSinTipoDePublicacionCuandoNoIngresoElTipoDePublicacion() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo {
        //given
        Publicacion nuevaPublicacionNoValida = givenCreoUnaPublicacionSinTipoDePublicacion();
        doThrow(new PublicacionSinTipoDePublicacion()).when(servicioPublicacion).guardarPublicacion(ArgumentMatchers.any(Publicacion.class));
        //when
        ModelAndView mav = whenRealizoUnaPublicacion(nuevaPublicacionNoValida);
        //then
        thenLaPubliacionNoEsRegistradaCuandoElCampoTipoDePublicacionEstaVacio(mav);
    }

    private void thenLaPubliacionNoEsRegistradaCuandoElCampoTipoDePublicacionEstaVacio(ModelAndView mav) {
        assertThat(mav.getViewName(), is(equalToIgnoringCase("redirect:/home")));
        assertThat(mav.getModel().get("mensaje").toString(), is(equalToIgnoringCase("Publicacion no registrada: el campo tipo de publicacion no puede estar vacio")));
    }

    private Publicacion givenCreoUnaPublicacionSinTipoDePublicacion() {
        String campoDeSangre="DEA-1.1";
        String tipoDePublicion="";
        Publicacion nuevaPublicacion= new Publicacion();
        nuevaPublicacion.setTipoDeSangre(campoDeSangre);
        nuevaPublicacion.setTipoDePublicacion(tipoDePublicion);
        return nuevaPublicacion;
    }

    private static Publicacion givenCreoUnaPublicacionNoValida() {
        String campoDeSangre="";
        String tipoDePublicion="";
        Publicacion nuevaPublicacion= new Publicacion();
        nuevaPublicacion.setTipoDeSangre(campoDeSangre);
        nuevaPublicacion.setTipoDePublicacion(tipoDePublicion);
        return nuevaPublicacion;
    }

    //TODO: validar que sea una sangre valida la ingresada
    @Test
    public void siNoIngresaUnTituloEnLaPublicacionLaPublicacionNoSePublicara() throws PublicacionSinTipoDeSangre, PublicacionNoValida, PublicacionSinTipoDePublicacion, PublicacionSinTitulo {
        //given
        Publicacion publicacionSinTitulo= givenCreoUnaPublicacionSinTitutlo();
        doThrow(new PublicacionSinTitulo()).when(servicioPublicacion).guardarPublicacion(ArgumentMatchers.any(Publicacion.class));
        //when
        ModelAndView mav= controladorPublicacion.publicarPublicacion(publicacionSinTitulo, 1l, request);
        //
        thenLaPublicacionSinTituloNoEsPublicada(mav);
    }

    private Publicacion givenCreoUnaPublicacionSinTitutlo() {
        String campoDeSangre="DEA-1.1.";
        String tipoDePublicion="busqueda";
        String titulo="";
        Publicacion nuevaPublicacion= new Publicacion();
        nuevaPublicacion.setTipoDeSangre(campoDeSangre);
        nuevaPublicacion.setTipoDePublicacion(tipoDePublicion);
        nuevaPublicacion.setTitulo(titulo);
        return nuevaPublicacion;
    }

    private void thenLaPublicacionSinTituloNoEsPublicada(ModelAndView mav) {
        assertThat(mav.getViewName(),is(equalToIgnoringCase("redirect:/home")));
        assertThat(mav.getModel().get("mensaje").toString(),is(equalToIgnoringCase("Publicacion no registrada: el campo titulo de la publicacion no puede estar vacio")));
    }

*/
}