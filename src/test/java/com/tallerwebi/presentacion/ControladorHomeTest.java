package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.servicio.ServicioPublicacion;
import com.tallerwebi.dominio.servicio.ServicioPublicacionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorHomeTest {

    ServicioPublicacion servicioPublicacion;
    ControladorHome controladorHome;

    @BeforeEach
    public void init(){
        servicioPublicacion= mock(ServicioPublicacionImpl.class);
        controladorHome= new ControladorHome(servicioPublicacion);
    }

    /*@Test
    public void queMePermitaListarUnaPublicacion(){
        //given
        givenElServicioMeEntregaUnaPublicacion();
        //when
        List<Publicacion> publicaciones= whenIngresoYObtengoTodasLasPublicaciones();
        //then
        thenObtengoUnaPublicacionParaListar(publicaciones);
    } */

    @Test
    public void queMePermitaListarVariasPublicaciones(){
        //given
        givenElServicioMeEntregaTresPublicaciones();
        //when
        List<Publicacion> publicaciones= whenIngresoYObtengoTodasLasPublicaciones();
        //then
        thenObtengoTresPublicacionesParaListar(publicaciones);
    }

    @Test
    public void siNoHayPublicacionesGuardadasNoListaPublicaciones(){
        //given
        givenElServicioMeEntregaUnaListaVacia();
        //when
        List<Publicacion> publicaciones= whenIngresoYObtengoTodasLasPublicaciones();
        //then
        thenNoObtengoPublicaciones(publicaciones);
    }

    private void thenNoObtengoPublicaciones(List<Publicacion> publicaciones) {
        assertThat(publicaciones, is(Collections.emptyList()));
        assertThat(publicaciones.size(),is(0));
    }

    private void givenElServicioMeEntregaUnaListaVacia() {
        when(servicioPublicacion.obtenerTodasLasPublicaciones()).thenReturn(new ArrayList<>());
    }


    private void thenObtengoTresPublicacionesParaListar(List<Publicacion> publicaciones) {
        assertThat(publicaciones.size(), is(3));
        assertThat(publicaciones, hasItem(hasProperty("titulo",is("dono sangre"))));
        assertThat(publicaciones, hasItem(hasProperty("titulo",is("vendo sangre"))));
        assertThat(publicaciones, hasItem(hasProperty("titulo",is("busco sangre"))));

    }

    private void givenElServicioMeEntregaTresPublicaciones() {

        List<Publicacion> publicaciones= new ArrayList<>();
        Publicacion nuevaPublicacion = getPublicacionConTipoDePublicacionYTitulo("donacion", "dono sangre");

        Publicacion nuevaPublicacion1= getPublicacionConTipoDePublicacionYTitulo("venta", "vendo sangre");

        Publicacion nuevaPublicacion2= getPublicacionConTipoDePublicacionYTitulo("busqueda","busco sangre");

        publicaciones.add(nuevaPublicacion);
        publicaciones.add(nuevaPublicacion1);
        publicaciones.add(nuevaPublicacion2);

        when(servicioPublicacion.obtenerTodasLasPublicaciones()).thenReturn(publicaciones);
    }

    private static Publicacion getPublicacionConTipoDePublicacionYTitulo(String tipoDePublicacion,String titulo) {
        Publicacion nuevaPublicacion= new Publicacion();
        nuevaPublicacion.setTipoDePublicacion(tipoDePublicacion);
        nuevaPublicacion.setTitulo(titulo);
        return nuevaPublicacion;
    }


    /*private void thenObtengoUnaPublicacionParaListar(List<Publicacion> publicaciones) {
        ModelAndView modelAndView=controladorHome.irAHome("",new HttpServletRequest(), );
        assertThat(modelAndView.getModel().get("publicaciones"), is(equalTo(publicaciones)));
        assertThat(servicioPublicacion.obtenerTodasLasPublicaciones().size(), is(1));
    }*/

    private List <Publicacion> whenIngresoYObtengoTodasLasPublicaciones() {
        return servicioPublicacion.obtenerTodasLasPublicaciones();

    }

    private void givenElServicioMeEntregaUnaPublicacion() {
        List<Publicacion> publicaciones= new ArrayList<>();
        publicaciones.add(new Publicacion());
        when(servicioPublicacion.obtenerTodasLasPublicaciones()).thenReturn(publicaciones);
    }
}
