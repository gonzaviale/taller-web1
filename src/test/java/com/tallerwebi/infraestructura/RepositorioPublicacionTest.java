package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Publicacion;
import com.tallerwebi.dominio.RepositorioPublicacion;
import com.tallerwebi.dominio.excepcion.PublicacionNoExistente;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioPublicacionTest {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioPublicacion repositorio;
    @Test
    @Transactional
    @Rollback
    public void guardarPublicacion() {
        //given
        Publicacion miPublicacion = givenObtengoPublicacionConTipoDeSangre();

        //when
        whenGuardoUnaPublicacionEnMiRepositorio(miPublicacion);

        //then
        thenMiPublicacionGuardadaExiste(miPublicacion);
    }

    private static void thenMiPublicacionGuardadaExiste(Publicacion miPublicacion) {
        assertThat(miPublicacion.getId(), is(notNullValue()));
    }

    @Test
    @Transactional
    @Rollback
    public void queCuandoGuardeUnaPublicacionPuedaBuscarPorSuId() throws PublicacionNoExistente {
        //given
        Publicacion miPublicacion = givenObtengoPublicacionConTipoDeSangre();

        //when
        whenGuardoUnaPublicacionEnMiRepositorio(miPublicacion);

        //then
        thenLaPublicacionEsObtenidaPorSuIdDeFormaCorrecta(miPublicacion);
    }

    private void whenGuardoUnaPublicacionEnMiRepositorio(Publicacion miPublicacion) {
        repositorio.guardarPublicacion(miPublicacion);
    }

    private void thenLaPublicacionEsObtenidaPorSuIdDeFormaCorrecta(Publicacion miPublicacion) throws PublicacionNoExistente {
        Publicacion publicacionGuardada = repositorio.obtenerPorId(miPublicacion.getId());
        assertThat(publicacionGuardada, is(notNullValue()));
        assertThat(publicacionGuardada.getId(), is(equalTo(miPublicacion.getId())));
        assertThat(publicacionGuardada.getTipoDeSangre(), is(equalToIgnoringCase("DEA-1.1")));
    }

    private static Publicacion givenObtengoPublicacionConTipoDeSangre() {
        Publicacion miPublicacion = new Publicacion();
        miPublicacion.setTipoDeSangre("DEA-1.1");
        return miPublicacion;
    }

    @Test
    @Transactional
    @Rollback
    public void queMeLanzeUnaExepcionSiNoExistePublicacionConEseId() {
        //given
        //when
        //then
        thenLaPublicacionNoEsObtenidaPorSuIdYaQueNoExiste();
    }

    private void thenLaPublicacionNoEsObtenidaPorSuIdYaQueNoExiste(){
        assertThrows(PublicacionNoExistente.class, ()->repositorio.obtenerPorId(100L));
    }

    @Test
    @Transactional
    @Rollback
    public void cuandoObtengoTodasLasPublicacionesYNoHayGuardadasMeDevuelveUnaListaVacia() {
        //given

        //when
        List<Publicacion> publicaciones= whenObtengoLasPublicacionesGuardadas();
        //then
        thenObtengoUnaListaVaciaDePublicaciones(publicaciones);
    }

    @Test
    @Transactional
    @Rollback
    public void queLasPublicacionesGuardadasSeanLasPublicacionesObtenidas() {
        //given
        List<Publicacion> publicacionesEsperadas= givenObtengoUnaListaConLasPublicacionesGuardadas();
        //when
        List<Publicacion> publicacionesObtenidas= whenObtengoLasPublicacionesGuardadas();
        //then
        thenObtengoUnaListaConLasPublicaciones(publicacionesEsperadas,publicacionesObtenidas);
    }

    private void thenObtengoUnaListaConLasPublicaciones(List<Publicacion> publicacionesEsperadas, List<Publicacion> publicacionesObtenidas) {
    assertThat(publicacionesEsperadas, is(equalTo(publicacionesObtenidas)));
    }

    private List<Publicacion> givenObtengoUnaListaConLasPublicacionesGuardadas() {
        Publicacion nuevaPublicacion=new Publicacion();
        Publicacion nuevaPublicacion1= new Publicacion();
        repositorio.guardarPublicacion(nuevaPublicacion);
        repositorio.guardarPublicacion(nuevaPublicacion1);
        List <Publicacion> publicaciones= new ArrayList<>();
        publicaciones.add(nuevaPublicacion);
        publicaciones.add(nuevaPublicacion1);
        return publicaciones;
    }

    private List<Publicacion> whenObtengoLasPublicacionesGuardadas() {
        return repositorio.obtenerTodasLasPublicaciones();
    }

    private void thenObtengoUnaListaVaciaDePublicaciones(List<Publicacion> publicaciones) {
        assertThat(publicaciones,is(equalTo(Collections.emptyList())));
    }

}