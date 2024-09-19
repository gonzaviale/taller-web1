package com.tallerwebi.integracion;

import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.RepositorioPublicacion;
import com.tallerwebi.dominio.ServicioPublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionNoValida;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDePublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionSinTipoDeSangre;
import com.tallerwebi.infraestructura.ServicioPublicacionImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    private Publicacion whenCreoUnaPublicacionNoValida() {
        Publicacion nuevaPublicacion= new Publicacion();
        nuevaPublicacion.setTipoDePublicacion("");
        nuevaPublicacion.setTipoDeSangre("");
        return nuevaPublicacion;
    }


    private Publicacion whenCreoUnaPublicacionSinTipoDePublicacion() {
        Publicacion nuevaPublicacion= new Publicacion();
        nuevaPublicacion.setTipoDePublicacion("");
        nuevaPublicacion.setTipoDeSangre("DEA-1.1");
        return nuevaPublicacion;
    }


    private static Publicacion whenCreoUnaPublicacionSinTipoDeSangre() {
        Publicacion nuevaPublicacion= new Publicacion();
        nuevaPublicacion.setTipoDePublicacion("busqueda");
        nuevaPublicacion.setTipoDeSangre("");
        return nuevaPublicacion;
    }

    @Test
    public void cuandoElTipoDeSangreSeaValidaYSuTipoDePublicacionTambienMeDejaraGuardarLaPublicacionValida() throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida {
        //given
        Publicacion miPublicacion = new Publicacion();
        miPublicacion.setTipoDeSangre("DEA-1.1");
        miPublicacion.setTipoDePublicacion("busqueda");
        //when
        whenElRegistroDeLaPublicacionEsExitoso(miPublicacion);
        //then
        thenMeDejaGuardarLaPublicacionValida(miPublicacion);
    }

    private void whenElRegistroDeLaPublicacionEsExitoso(Publicacion miPublicacion) throws PublicacionSinTipoDeSangre, PublicacionSinTipoDePublicacion, PublicacionNoValida {
        servicioPublicacion.guardarPublicacion(miPublicacion);
    }

    private void thenMeDejaGuardarLaPublicacionValida(Publicacion miPublicacion) {
        verify(repositorioPublicacion, times(1)).guardarPublicacion(miPublicacion);
    }

}