package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Publicacion;
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

    @Test
    @Transactional
    @Rollback
    public void queCuandoBusqueSinParametrosNoObtengaPublicaciones() {
        //given --> no obtengo publicaciones
        List<Publicacion> publicacionesEsperadas= new ArrayList<>();
        //when
        List<Publicacion> publicacionesObtenidas= whenObtengoLasPublicacionesPorCoincidenciasSinParametros();
        //then
        thenNoObtengoPublicaciones(publicacionesEsperadas,publicacionesObtenidas);
    }

    private void thenNoObtengoPublicaciones(List<Publicacion> publicacionesEsperadas, List<Publicacion> publicacionesObtenidas) {
        assertThat(publicacionesObtenidas.size(),equalTo(publicacionesEsperadas.size()));
        assertThat(publicacionesObtenidas.size(),is(0));
    }

    private List<Publicacion> whenObtengoLasPublicacionesPorCoincidenciasSinParametros() {
        return repositorio.buscarPublicaciones("","","","");
    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaEncontrarDosCoincidenciasCuandoCargoDosPublicacionesConCoincidenciasEnSangre() {
        //given
        givenGuardoTresPublicacionesConSoloDosCoincidenciasEnPublicaciones();
        //when
        List<Publicacion> publicacionesObtenidas= whenObtengoLasPublicaionesPorCoincidencias();
        //then
        thenEncuentroDosCoincidenciasConLaInicialDEA(publicacionesObtenidas);
    }

    private List<Publicacion> whenObtengoLasPublicaionesPorCoincidencias() {
        return repositorio.buscarPublicaciones("","DEA","","");
    }

    private void thenEncuentroDosCoincidenciasConLaInicialDEA(List<Publicacion> publicacionesObtenidas) {
        assertThat(publicacionesObtenidas.size(),is(2));
        assertThat(publicacionesObtenidas, hasItem(hasProperty("tipoDeSangre",is("DEA-1.1."))));
        assertThat(publicacionesObtenidas, hasItem(hasProperty("tipoDeSangre",is("DEA-1.2."))));
        assertThat(repositorio.obtenerTodasLasPublicaciones(),hasItem(hasProperty("tipoDeSangre",is("INVALIDA"))));
    }


    private void givenGuardoTresPublicacionesConSoloDosCoincidenciasEnPublicaciones() {

        Publicacion nuevaPublicacion=new Publicacion();
        nuevaPublicacion.setTipoDeSangre("DEA-1.1.");
        Publicacion nuevaPublicacion1= new Publicacion();
        nuevaPublicacion1.setTipoDeSangre("DEA-1.2.");
        Publicacion nuevaPublicacion2= new Publicacion();
        nuevaPublicacion2.setTipoDeSangre("INVALIDA");
        repositorio.guardarPublicacion(nuevaPublicacion2);
        repositorio.guardarPublicacion(nuevaPublicacion);
        repositorio.guardarPublicacion(nuevaPublicacion1);
    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaEncontrarDosCoincidenciasCuandoCargoDosPublicacionesConCoincidenciasEnTipoDePublicacion(){
        //given
        givenGuardoTresPublicacionesConSoloDosCoincidenciasEnPublicacionesDeTipoPublicacion();
        //when
        List<Publicacion> publicacionesObtenidas= whenObtengoLasPublicaionesPorCoincidenciasDeTipoDePublicacionDeDonacion();
        //then
        thenEncuentroDosCoincidenciasConLaInicialDonaEnElTipoPublicacion(publicacionesObtenidas);
    }

    private void thenEncuentroDosCoincidenciasConLaInicialDonaEnElTipoPublicacion(List<Publicacion> publicacionesObtenidas) {
        assertThat(publicacionesObtenidas.size(),is(2));
        assertThat(publicacionesObtenidas, hasItem(hasProperty("tipoDePublicacion",is("Donacion"))));
        assertThat(repositorio.obtenerTodasLasPublicaciones(),hasItem(hasProperty("tipoDePublicacion",is("INVALIDA"))));

    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaEncontrarDosCoincidenciasCuandoCargoDosPublicacionesConCoincidenciasEnTitulo(){
        //given
        givenGuardoTresPublicacionesConSoloDosCoincidenciasEnPublicacionesTitulo();
        //when
        List<Publicacion> publicacionesObtenidas= whenObtengoLasPublicaionesPorCoincidenciasDeTituloDeDonacion();
        //then
        thenEncuentroDosCoincidenciasConLaInicialDona(publicacionesObtenidas);
    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaEncontrarDosCoincidenciasCuandoCargoDosPublicacionesConCoincidenciasEnZonaDeResidencia(){
        //given
        givenGuardoTresPublicacionesConSoloDosCoincidenciasEnPublicacionesConZonasDeResidencia();
        //when
        List<Publicacion> publicacionesObtenidas= whenObtengoLasPublicaionesPorCoincidenciasDeZonaDeResidencia();
        //then
        thenEncuentroDosCoincidenciasConLaInicialDonaEnZonaDeResidencia(publicacionesObtenidas);
    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaEncontrarDosCoincidenciasCuandoCargoDosPublicacionesConCoincidenciasEnZonaDeResidenciaYTitulo(){
        //given
        givenGuardoTresPublicacionesConCoincidenciasEnPublicacionesConZonasDeResidenciaYTitulo();
        //when
        List<Publicacion> publicacionesObtenidas= whenObtengoLasPublicaionesPorCoincidenciasDeZonaDeResidenciaYTitulo();
        //then
        thenEncuentroDosCoincidenciasConLaInicialDonaEnZonaDeResidenciaYTitulo(publicacionesObtenidas);
    }

    @Test
    @Transactional
    @Rollback
    public void queCuandoNoTengaCoincidenciasObtengaUnaListaVacia(){
        //given
        givenGuardoTresPublicacionesConCoincidenciasEnPublicacionesConZonasDeResidenciaYTitulo();
        //when
        List<Publicacion> publicacionesObtenidas= whenObtengoLasPublicaionesPorCoincidenciasDeZonaDeResidenciaYTituloNoExistentes();
        //then
        thenObtengoUnaListaVaciaDePublicaciones(publicacionesObtenidas);
    }

    private List<Publicacion> whenObtengoLasPublicaionesPorCoincidenciasDeZonaDeResidenciaYTituloNoExistentes() {
        return repositorio.buscarPublicaciones("venta","","inglaterra","");
    }


    private void thenEncuentroDosCoincidenciasConLaInicialDonaEnZonaDeResidenciaYTitulo(List<Publicacion> publicacionesObtenidas) {
        assertThat(publicacionesObtenidas.size(),is(2));
        assertThat(publicacionesObtenidas, hasItem(hasProperty("zonaDeResidencia",is("argentina"))));
        assertThat(repositorio.obtenerTodasLasPublicaciones(),hasItem(hasProperty("zonaDeResidencia",is("Chile"))));
    }

    private List<Publicacion> whenObtengoLasPublicaionesPorCoincidenciasDeZonaDeResidenciaYTitulo() {
        return repositorio.buscarPublicaciones("busqueda","","argentina","");
    }

    private void givenGuardoTresPublicacionesConCoincidenciasEnPublicacionesConZonasDeResidenciaYTitulo() {
        Publicacion nuevaPublicacion=new Publicacion();
        nuevaPublicacion.setZonaDeResidencia("argentina");
        nuevaPublicacion.setTitulo("busqueda");
        Publicacion nuevaPublicacion1= new Publicacion();
        nuevaPublicacion1.setZonaDeResidencia("argentina");
        nuevaPublicacion1.setTitulo("busqueda");
        Publicacion nuevaPublicacion2= new Publicacion();
        nuevaPublicacion2.setZonaDeResidencia("Chile");
        nuevaPublicacion2.setTitulo("donacion");
        repositorio.guardarPublicacion(nuevaPublicacion2);
        repositorio.guardarPublicacion(nuevaPublicacion);
        repositorio.guardarPublicacion(nuevaPublicacion1);

    }

    private void thenEncuentroDosCoincidenciasConLaInicialDonaEnZonaDeResidencia(List<Publicacion> publicacionesObtenidas) {
        assertThat(publicacionesObtenidas.size(),is(2));
        assertThat(publicacionesObtenidas, hasItem(hasProperty("zonaDeResidencia",is("Donacion"))));
        assertThat(repositorio.obtenerTodasLasPublicaciones(),hasItem(hasProperty("zonaDeResidencia",is("INVALIDA"))));
    }

    private List<Publicacion> whenObtengoLasPublicaionesPorCoincidenciasDeZonaDeResidencia() {
        return repositorio.buscarPublicaciones("","","Dona","");
    }

    private void givenGuardoTresPublicacionesConSoloDosCoincidenciasEnPublicacionesConZonasDeResidencia() {
        Publicacion nuevaPublicacion=new Publicacion();
        nuevaPublicacion.setZonaDeResidencia("Donacion");
        Publicacion nuevaPublicacion1= new Publicacion();
        nuevaPublicacion1.setZonaDeResidencia("Donacion");
        Publicacion nuevaPublicacion2= new Publicacion();
        nuevaPublicacion2.setZonaDeResidencia("INVALIDA");
        repositorio.guardarPublicacion(nuevaPublicacion2);
        repositorio.guardarPublicacion(nuevaPublicacion);
        repositorio.guardarPublicacion(nuevaPublicacion1);
    }

    private List<Publicacion> whenObtengoLasPublicaionesPorCoincidenciasDeTituloDeDonacion() {
        return repositorio.buscarPublicaciones("Dona","","","");
    }

    private void givenGuardoTresPublicacionesConSoloDosCoincidenciasEnPublicacionesTitulo() {
        Publicacion nuevaPublicacion=new Publicacion();
        nuevaPublicacion.setTitulo("Donacion");
        Publicacion nuevaPublicacion1= new Publicacion();
        nuevaPublicacion1.setTitulo("Donacion");
        Publicacion nuevaPublicacion2= new Publicacion();
        nuevaPublicacion2.setTitulo("INVALIDA");
        repositorio.guardarPublicacion(nuevaPublicacion2);
        repositorio.guardarPublicacion(nuevaPublicacion);
        repositorio.guardarPublicacion(nuevaPublicacion1);
    }

    private void thenEncuentroDosCoincidenciasConLaInicialDona(List<Publicacion> publicacionesObtenidas) {
        assertThat(publicacionesObtenidas.size(),is(2));
        assertThat(publicacionesObtenidas, hasItem(hasProperty("titulo",is("Donacion"))));
        assertThat(repositorio.obtenerTodasLasPublicaciones(),hasItem(hasProperty("titulo",is("INVALIDA"))));
    }

    private List<Publicacion> whenObtengoLasPublicaionesPorCoincidenciasDeTipoDePublicacionDeDonacion() {
        return repositorio.buscarPublicaciones("","","","Dona");
    }

    private void givenGuardoTresPublicacionesConSoloDosCoincidenciasEnPublicacionesDeTipoPublicacion() {
        Publicacion nuevaPublicacion=new Publicacion();
        nuevaPublicacion.setTipoDePublicacion("Donacion");
        Publicacion nuevaPublicacion1= new Publicacion();
        nuevaPublicacion1.setTipoDePublicacion("Donacion");
        Publicacion nuevaPublicacion2= new Publicacion();
        nuevaPublicacion2.setTipoDePublicacion("INVALIDA");
        repositorio.guardarPublicacion(nuevaPublicacion2);
        repositorio.guardarPublicacion(nuevaPublicacion);
        repositorio.guardarPublicacion(nuevaPublicacion1);

    }

}