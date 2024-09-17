package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.ServicioPublicacion;

import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDeSangre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ControladorPublicacionTest {

    private ControladorPublicacionBusqueda controladorPublicacionBusqueda;
    private ServicioPublicacion servicioPublicacion;
    //Como donante puedo publicar sangre para donar ya sea paga o gratuita

    @BeforeEach
    public void init(){
        servicioPublicacion = mock(ServicioPublicacion.class);
        controladorPublicacionBusqueda = new ControladorPublicacionBusqueda(servicioPublicacion);
    }

    @Test
    public void siNoIngresaCamposSangreOLoIngresaVacioNoSePodraRealizarLaPublicacion(){

        //given
        String campoDeSangre="";
        //when
        ModelAndView vistaError= whenRealizoUnaPublicacion(campoDeSangre);
        //then
        thenLaPubliacionNoEsRegistradaCuandoElCampoSangreEsVacio(vistaError);
    }

    private ModelAndView whenRealizoUnaPublicacion(String campoDeSangre) {
       return controladorPublicacionBusqueda.publicarPublicacion(campoDeSangre);
    }

    private void thenLaPubliacionNoEsRegistradaCuandoElCampoSangreEsVacio(ModelAndView ventanaError) {
        assertThat(ventanaError.getViewName(), is (equalToIgnoringCase("error")));
        assertThat(ventanaError.getModel().get("error").toString(), is (equalToIgnoringCase("el campo sangre no puede estar vacio")));
    }

    @Test
    public void siIngresaCampoSangreSePodraRealizarLaPublicacion() throws PublicacionSinTipoDeSangre {

        //given
        String campoDeSangre="DEA-1.1";
        //when
        ModelAndView publicacionExitosa= whenRealizoUnaPublicacion(campoDeSangre);
        //then
        thenLaPubliacionEsRegistrada(publicacionExitosa);
    }

    private void thenLaPubliacionEsRegistrada(ModelAndView miPublicacion) throws PublicacionSinTipoDeSangre {
        assertThat(miPublicacion.getViewName(),is(equalTo("redirect:/registro exitoso")));
        assertThat(miPublicacion.getModel().get("registro exitoso").toString(), is("la publicacion fue registrada correctamente"));
        verify(servicioPublicacion, times(1)).guardarPublicacion(ArgumentMatchers.any());
    }
    @Test
    public void queCuandoElRegistroSeaExitosoMeDeAltaUnaNuevaPublicacion() throws PublicacionSinTipoDeSangre {
        //given
        String campoDeSangre="DEA-1.1";
        //when
        ModelAndView miPublicacion= whenRealizoUnaPublicacion(campoDeSangre);
        //then
        thenLaPubliacionEsRegistrada(miPublicacion);
    }

    @Test
    public void queNoMePermitaDarDeAltaUnaPublicacionConUnaSangreSinTipo() throws PublicacionSinTipoDeSangre {
        //given
        String campoDeSangreNoValido="DEA-1.1";
        doThrow(new PublicacionSinTipoDeSangre()).when(servicioPublicacion).guardarPublicacion(ArgumentMatchers.any(Publicacion.class));
        //when
        ModelAndView mav= whenRealizoUnaPublicacion(campoDeSangreNoValido);
        //then
        thenLaPubliacionNoEsRegistradaCuandoElCampoSangreEsVacio(mav);
    }
}