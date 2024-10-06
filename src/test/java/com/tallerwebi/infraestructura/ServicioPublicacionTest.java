package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.RepositorioPublicacion;
import com.tallerwebi.dominio.servicio.ServicioPublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionNoValida;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDePublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDeSangre;
import com.tallerwebi.dominio.excepcion.PublicacionSinTitulo;
import com.tallerwebi.dominio.servicio.ServicioPublicacionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ServicioPublicacionTest {

    private ServicioPublicacion servicioPublicacion;
    private RepositorioPublicacion repositorioPublicacion;

    @BeforeEach
    public void init() {
        repositorioPublicacion = mock(RepositorioPublicacion.class);
        servicioPublicacion = new ServicioPublicacionImpl(repositorioPublicacion);
    }

    @Test
    public void cuandoElTipoDeSangreSeaInvalidoMeTiraraUnaExeptionDeTipoPublicacionSinTipoDeSangre() {
        Publicacion nuevaPublicacion = whenCreoUnaPublicacionSinTipoDeSangre();
        assertThrows(PublicacionSinTipoDeSangre.class, () -> servicioPublicacion.guardarPublicacion(nuevaPublicacion));
    }

    @Test
    public void cuandoElTipoDePublicacionSeaInvalidoMeTiraraUnaExeptionDeTipoPublicacionSinTipoDePublicacion() {
        Publicacion nuevaPublicacion = whenCreoUnaPublicacionSinTipoDePublicacion();
        assertThrows(PublicacionSinTipoDePublicacion.class, () -> servicioPublicacion.guardarPublicacion(nuevaPublicacion));
    }
    @Test
    public void cuandoElTipoDePublicacionSeaInvalidaMeTiraraUnaExeptionDeTipoPublicacionNoValida() {
        Publicacion nuevaPublicacion = whenCreoUnaPublicacionNoValida();
        assertThrows(PublicacionNoValida.class, () -> servicioPublicacion.guardarPublicacion(nuevaPublicacion));
    }
    @Test
    public void siNoIngresaUnTituloEnLaPublicacionMeLanzaraUnaExeptionDeTipoSinTituloDeLaPublicacion(){
        Publicacion nuevaPublicacion = whenCreoUnaPublicacionSinTitulo();
        assertThrows(PublicacionSinTitulo.class, () -> servicioPublicacion.guardarPublicacion(nuevaPublicacion));
    }

    private Publicacion whenCreoUnaPublicacionSinTitulo() {
        Publicacion nuevaPublicacion= new Publicacion();
        nuevaPublicacion.setTipoDePublicacion("Venta");
        nuevaPublicacion.setTipoDeSangre("DEA-1.1");
        nuevaPublicacion.setTitulo("");
        return nuevaPublicacion;
    }

    private Publicacion whenCreoUnaPublicacionNoValida() {
        Publicacion nuevaPublicacion= new Publicacion();
        nuevaPublicacion.setTipoDePublicacion("");
        nuevaPublicacion.setTipoDeSangre("");
        nuevaPublicacion.setTitulo("");
        return nuevaPublicacion;
    }


    private Publicacion whenCreoUnaPublicacionSinTipoDePublicacion() {
        Publicacion nuevaPublicacion= new Publicacion();
        nuevaPublicacion.setTipoDePublicacion("");
        nuevaPublicacion.setTipoDeSangre("DEA-1.1");
        nuevaPublicacion.setTitulo("busco DEA-1.1");
        return nuevaPublicacion;
    }


    private static Publicacion whenCreoUnaPublicacionSinTipoDeSangre() {
        Publicacion nuevaPublicacion= new Publicacion();
        nuevaPublicacion.setTipoDePublicacion("busqueda");
        nuevaPublicacion.setTipoDeSangre("");
        nuevaPublicacion.setTitulo("busco sangre");
        return nuevaPublicacion;
    }

    @Test
    public void cuandoLaPublicacionSeaValidaMeDejaraPublicar() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo {
        //given
        Publicacion miPublicacion = givenCreoUnaPublicaionValida();
        //when
        whenElRegistroDeLaPublicacionEsExitoso(miPublicacion);
        //then
        thenMeDejaGuardarLaPublicacionValida(miPublicacion);
    }

    private static Publicacion givenCreoUnaPublicaionValida() {
        Publicacion miPublicacion = new Publicacion();
        miPublicacion.setTipoDeSangre("DEA-1.1");
        miPublicacion.setTipoDePublicacion("busqueda");
        miPublicacion.setTitulo("busco sangre DEA-1.1");
        return miPublicacion;
    }

    private void whenElRegistroDeLaPublicacionEsExitoso(Publicacion miPublicacion) throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo {
        servicioPublicacion.guardarPublicacion(miPublicacion);
    }

    private void thenMeDejaGuardarLaPublicacionValida(Publicacion miPublicacion) {
        verify(repositorioPublicacion, times(1)).guardarPublicacion(miPublicacion);
    }
    @Test
    public void cuandoSeReigstraUnaPublicacionCambioElEstadoAActivoLaPublicacion() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida, PublicacionSinTitulo {
        //given
        Publicacion miPublicacion = givenCreoUnaPublicaionValida();
        //when
        whenElRegistroDeLaPublicacionEsExitoso(miPublicacion);
        //then
        thenLaPublicacionValidaCambiaDeEstadoAActiva(miPublicacion);
    }

    private void thenLaPublicacionValidaCambiaDeEstadoAActiva(Publicacion miPublicacion) {
        assertThat(miPublicacion.getEstaActiva(), equalTo(Boolean.TRUE));
    }

    @Test
    public void cuandoObtengoTodasLasPublicacionesPeroNoHayPublicacionesGuardadasObtengoUnaColeccionVacia() {
        //given
        when(servicioPublicacion.obtenerTodasLasPublicaciones()).thenReturn(Collections.emptyList());
        //when
        List<Publicacion> publicaciones = whenObtengoLasPublicacionesGuardadas();
        //then
        thenNoHayPublicacionesGuardadas(publicaciones);
    }

    @Test
    public void cuandoObtengoTodasLasPublicacionesPeroHayPublicacionesGuardadasObtengoLasPublicaciones(){
        //given
        List<Publicacion> publicacionesEsperadas= givenObtengoPublicacionesEsperadas();
        when(repositorioPublicacion.obtenerTodasLasPublicaciones()).thenReturn(publicacionesEsperadas);
        //when
        List<Publicacion> publicaciones = whenObtengoLasPublicacionesGuardadas();
        //then
        thenHayPublicacionesGuardadas(publicaciones,publicacionesEsperadas);
    }

    private void thenHayPublicacionesGuardadas(List<Publicacion> publicaciones,List<Publicacion> publicacionesEsperadas) {
        assertThat(publicaciones,(equalTo(publicacionesEsperadas)));
    }

    private List<Publicacion> givenObtengoPublicacionesEsperadas() {
        Publicacion publicacion=givenCreoUnaPublicaionValida();
        List <Publicacion> publicaciones= new ArrayList<>();
        publicaciones.add(publicacion);
        publicaciones.add(publicacion);
        return publicaciones;
    }


    private void thenNoHayPublicacionesGuardadas(List<Publicacion> publicaciones) {
        assertThat(publicaciones,equalTo(Collections.emptyList()));
    }

    private List<Publicacion> whenObtengoLasPublicacionesGuardadas() {
        return servicioPublicacion.obtenerTodasLasPublicaciones();
    }


}