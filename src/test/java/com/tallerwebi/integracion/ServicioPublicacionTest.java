package com.tallerwebi.integracion;

import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.RepositorioPublicacion;
import com.tallerwebi.dominio.ServicioPublicacion;
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
        assertThrows(PublicacionSinTipoDeSangre.class, () -> servicioPublicacion.guardarPublicacion(new Publicacion()));
    }

    @Test
    public void cuandoElTipoDeSangreSeaValidaMeDejaraGuardarLaPublicacionValida() throws PublicacionSinTipoDeSangre {
        //given
        Publicacion miPublicacion = new Publicacion();
        miPublicacion.setTipoDeSangre("DEA-1.1");
        //when
        whenElRegistroDeLaPublicacionEsExitoso(miPublicacion);
        //then
        thenMeDejaGuardarLaPublicacionValida(miPublicacion);
    }

    private void whenElRegistroDeLaPublicacionEsExitoso(Publicacion miPublicacion) throws PublicacionSinTipoDeSangre {
        servicioPublicacion.guardarPublicacion(miPublicacion);
    }

    private void thenMeDejaGuardarLaPublicacionValida(Publicacion miPublicacion) {
        verify(repositorioPublicacion, times(1)).guardarPublicacion(miPublicacion);
    }

}