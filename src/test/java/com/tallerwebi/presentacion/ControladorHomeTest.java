package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioPublicacion;
import com.tallerwebi.infraestructura.ServicioPublicacionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class ControladorHomeTest {

    ServicioPublicacion servicioPublicacion;
    ControladorHome controladorHome;

    @BeforeEach
    public void init(){
        servicioPublicacion= mock(ServicioPublicacionImpl.class);
        controladorHome= new ControladorHome(servicioPublicacion);
    }

    @Test
    public void queMePermitaListarUnaPublicacion(){
        //given
        givenElServicioMeEntregaUnaPublicacion();
        //when
        whenIngresoYObtengoUnaPublicacion();
        //obtengo todas las publicaciones y las imprimo por pantalla dependiendo de sus parametros
        //el controlador solo se encarga de pasar los datos a la vista entonces debo simular que el servicio
        //deberia mockear el servicio de publicacion para que me entregue una lista falsa de cosas y
        //validar que se impriman bien

    }

    private void whenIngresoYObtengoUnaPublicacion() {

    }

    private void givenElServicioMeEntregaUnaPublicacion() {
        //mock(servicioPublicacion.obtenerPublicaciones());
    }
}
