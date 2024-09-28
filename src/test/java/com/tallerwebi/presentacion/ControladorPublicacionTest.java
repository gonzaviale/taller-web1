package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.ServicioPublicacion;

import com.tallerwebi.dominio.excepcion.PublicacionNoValida;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDePublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDeSangre;
import com.tallerwebi.dominio.excepcion.PublicacionSinTitulo;
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

    @BeforeEach
    public void init() {
        servicioPublicacion = mock(ServicioPublicacion.class);
        controladorPublicacion = new ControladorPublicacion(servicioPublicacion);
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
        return controladorPublicacion.publicarPublicacion(nuevaPublicacion);
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

    private void thenLaPubliacionEsRegistrada(ModelAndView miPublicacion) throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo {
        assertThat(miPublicacion.getViewName(), is(equalTo("redirect:/home")));
        assertThat(miPublicacion.getModel().get("mensaje").toString(), is("la publicacion fue registrada correctamente"));
        verify(servicioPublicacion, times(1)).guardarPublicacion(ArgumentMatchers.any());
    }

    @Test
    public void queCuandoElRegistroSeaExitosoMeDeAltaUnaNuevaPublicacion() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo {
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

    //TODO: validar que sea una sangre valida la ingresada,agregar un usuario a la publicacion pertenece y poder validar que le pertenece
    @Test
    public void siNoIngresaUnTituloEnLaPublicacionLaPublicacionNoSePublicara() throws PublicacionSinTipoDeSangre, PublicacionNoValida, PublicacionSinTipoDePublicacion, PublicacionSinTitulo {
        //given
        Publicacion publicacionSinTitulo= givenCreoUnaPublicacionSinTitutlo();
        doThrow(new PublicacionSinTitulo()).when(servicioPublicacion).guardarPublicacion(ArgumentMatchers.any(Publicacion.class));
        //when
        ModelAndView mav= controladorPublicacion.publicarPublicacion(publicacionSinTitulo);
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
}