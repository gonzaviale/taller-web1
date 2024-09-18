package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.ServicioPublicacion;

import com.tallerwebi.dominio.excepcion.PublicacionNoValida;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDePublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDeSangre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ControladorPublicacionTest {

    private ControladorPublicacion controladorPublicacion;
    private ServicioPublicacion servicioPublicacion;
    //Como donante puedo publicar sangre para donar ya sea paga o gratuita

    @BeforeEach
    public void init() {
        servicioPublicacion = mock(ServicioPublicacion.class);
        controladorPublicacion = new ControladorPublicacion(servicioPublicacion);
    }

    @Test
    public void siNoIngresaCamposSangreOLoIngresaVacioNoSePodraRealizarLaPublicacion() throws PublicacionSinTipoDeSangre, PublicacionNoValida, PublicacionSinTipoDePublicacion {

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
        return controladorPublicacion.publicarPublicacion(nuevaPublicacion);
    }

    private void thenLaPubliacionNoEsRegistradaCuandoElCampoSangreEsVacio(ModelAndView ventanaError) {
        assertThat(ventanaError.getViewName(), is(equalToIgnoringCase("error")));
        assertThat(ventanaError.getModel().get("error").toString(), is(equalToIgnoringCase("el campo sangre no puede estar vacio")));
    }

    @Test
    public void siIngresaCampoSangreSePodraRealizarLaPublicacion() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida {

        //given
        Publicacion nuevaPublicacionValida = givenCreoUnaPublicacionValida();
        //when
        ModelAndView publicacionExitosa = whenRealizoUnaPublicacion(nuevaPublicacionValida);
        //then
        thenLaPubliacionEsRegistrada(publicacionExitosa);
    }

    private void thenLaPubliacionEsRegistrada(ModelAndView miPublicacion) throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida {
        assertThat(miPublicacion.getViewName(), is(equalTo("redirect:/registro-exitoso")));
        assertThat(miPublicacion.getModel().get("registroExitoso").toString(), is("la publicacion fue registrada correctamente"));
        verify(servicioPublicacion, times(1)).guardarPublicacion(ArgumentMatchers.any());
    }

    @Test
    public void queCuandoElRegistroSeaExitosoMeDeAltaUnaNuevaPublicacion() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida {
        //given
        Publicacion nuevaPublicacionValida = givenCreoUnaPublicacionValida();
        //when
        ModelAndView miPublicacion = whenRealizoUnaPublicacion(nuevaPublicacionValida);
        //then
        thenLaPubliacionEsRegistrada(miPublicacion);
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
    public void queNoMePermitaDarDeAltaUnaPublicacionConUnaSangreSinTipo() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida {
        //given
        Publicacion nuevaPublicacionNoValida = givenCreoUnaPublicacionNoValida();
        doThrow(new PublicacionSinTipoDeSangre()).when(servicioPublicacion).guardarPublicacion(ArgumentMatchers.any(Publicacion.class));
        //when
        ModelAndView mav = whenRealizoUnaPublicacion(nuevaPublicacionNoValida);
        //then
        thenLaPubliacionNoEsRegistradaCuandoElCampoSangreEsVacio(mav);
    }

    @Test
    public void queNoMePermitaRealizaUnaPublicacionSinTipoDepublicacionYConSuCampoDeSangreVacio() throws PublicacionSinTipoDeSangre, PublicacionNoValida, PublicacionSinTipoDePublicacion {
        //given
        Publicacion nuevaPublicacionNoValida = givenCreoUnaPublicacionNoValida();
        doThrow(new PublicacionNoValida()).when(servicioPublicacion).guardarPublicacion(ArgumentMatchers.any(Publicacion.class));
        //when
        ModelAndView vistaError = whenRealizoUnaPublicacion(nuevaPublicacionNoValida);
        //then
        thenLaPubliacionNoEsRegistradaCuandoElCampoSangreEsVacioYElTipoDePublicacionNoEsValida(vistaError);

    }

    private void thenLaPubliacionNoEsRegistradaCuandoElCampoSangreEsVacioYElTipoDePublicacionNoEsValida(ModelAndView vistaError) {
        assertThat(vistaError.getViewName(), is(equalToIgnoringCase("error")));
        assertThat(vistaError.getModel().get("error").toString(), is(equalToIgnoringCase("el campo tipo de publicacion y el campo de sangre no puede estar vacio")));
    }

    @Test
    public void queMeLanzeUnaExepcionDeSinTipoDePublicacionCuandoNoIngresoElTipoDePublicacion() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida {
        //given
        Publicacion nuevaPublicacionNoValida = givenCreoUnaPublicacionSinTipoDePublicacion();
        doThrow(new PublicacionSinTipoDePublicacion()).when(servicioPublicacion).guardarPublicacion(ArgumentMatchers.any(Publicacion.class));
        //when
        ModelAndView mav = whenRealizoUnaPublicacion(nuevaPublicacionNoValida);
        //then
        thenLaPubliacionNoEsRegistradaCuandoElCampoTipoDePublicacionEstaVacio(mav);
    }

    private void thenLaPubliacionNoEsRegistradaCuandoElCampoTipoDePublicacionEstaVacio(ModelAndView mav) {
        assertThat(mav.getViewName(), is(equalToIgnoringCase("error")));
        assertThat(mav.getModel().get("error").toString(), is(equalToIgnoringCase("el campo tipo de publicacion no puede estar vacio")));
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
}