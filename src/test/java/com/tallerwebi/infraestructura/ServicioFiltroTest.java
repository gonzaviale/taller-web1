package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class ServicioFiltroTest {

    @Autowired
    RepositorioMascota repositorioMascota;
    @Autowired
    RepositorioPublicacion repositorioPublicacion;
    @Autowired
    ServicioFiltro servicioFiltro = new ServicioFiltroImpl(repositorioMascota,repositorioPublicacion);

    @Test
    @Transactional
    @Rollback
    public void filtrarMascota() {
        Mascota mascota = new Mascota();
        mascota.setNombre("Mascota");
        mascota.setSangre("0+");
        mascota.setTipo("Donante");

        repositorioMascota.agregarMascota(mascota);
        ArrayList<Mascota> mascotas = servicioFiltro.consultarMascota("Mascota", "0+", "Donante");
        Mascota recibida = mascotas.get(0);

        assertThat(recibida, equalTo(mascota));
    }

    @Test
    @Transactional
    @Rollback
    public void filtrarVariasMascotas() {
        Mascota mascota = new Mascota();
        mascota.setNombre("Mascota");
        mascota.setSangre("0+");
        mascota.setTipo("Donante");

        Mascota mascota1 = new Mascota();
        mascota1.setNombre("Mascota1");
        mascota1.setSangre("0+");
        mascota1.setTipo("Donante");

        Mascota mascota2 = new Mascota();
        mascota2.setNombre("Mascota2");
        mascota2.setSangre("0+");
        mascota2.setTipo("Recibe");

        repositorioMascota.agregarMascota(mascota);
        repositorioMascota.agregarMascota(mascota1);
        repositorioMascota.agregarMascota(mascota2);

        ArrayList<Mascota> mascotas = servicioFiltro.consultarMascota("", "", "Donante");
        ArrayList<Mascota> esperadas = new ArrayList<>();
        esperadas.add(mascota);
        esperadas.add(mascota1);

        assertThat(mascotas, equalTo(esperadas));
    }

    @Test
    @Transactional
    @Rollback
    public void filtrarUnaPublicacion() {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo("Mascota");
        publicacion.setTipoDeSangre("0+");
        publicacion.setTipoDePublicacion("Donante");
        Publicacion publicacion1 = new Publicacion();
        publicacion.setTitulo("Mascota");
        publicacion.setTipoDeSangre("A+");
        publicacion.setTipoDePublicacion("Donante");


        repositorioPublicacion.guardarPublicacion(publicacion);
        repositorioPublicacion.guardarPublicacion(publicacion1);
        ArrayList<Publicacion> publicaciones = servicioFiltro.consultarPublicaciones("Mascota","A+","","Donante");

        assertThat(publicaciones.size(), equalTo(1));
        assertThat(publicaciones, hasItem(hasProperty("titulo",is("Mascota"))));
    }

    @Test
    @Transactional
    @Rollback
    public void filtrarVariasPublicaciones() {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo("Mascota");
        publicacion.setTipoDeSangre("0+");
        publicacion.setTipoDePublicacion("Donante");

        Publicacion publicacion1 = new Publicacion();
        publicacion1.setTitulo("Mascota");
        publicacion1.setTipoDeSangre("A+");
        publicacion1.setTipoDePublicacion("Donante");

        Publicacion publicacion2 = new Publicacion();
        publicacion2.setTitulo("Mascota");
        publicacion2.setTipoDeSangre("A+");
        publicacion2.setTipoDePublicacion("Donante");

        repositorioPublicacion.guardarPublicacion(publicacion);
        repositorioPublicacion.guardarPublicacion(publicacion2);
        repositorioPublicacion.guardarPublicacion(publicacion1);

        ArrayList<Publicacion> publicaciones = servicioFiltro.consultarPublicaciones("Mascota", "", "","Donante");

        assertThat(publicaciones.size(), equalTo(3));
    }

    @Test
    @Transactional
    @Rollback
    public void siNoHayParametrosElResultadoEsUnaListaConTodasLasPublicaciones() {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo("Mascota");
        publicacion.setTipoDeSangre("0+");
        publicacion.setTipoDePublicacion("Donante");

        Publicacion publicacion1 = new Publicacion();
        publicacion1.setTitulo("Mascota");
        publicacion1.setTipoDeSangre("A+");
        publicacion1.setTipoDePublicacion("Donante");

        Publicacion publicacion2 = new Publicacion();
        publicacion2.setTitulo("Mascota");
        publicacion2.setTipoDeSangre("A+");
        publicacion2.setTipoDePublicacion("Donante");

        repositorioPublicacion.guardarPublicacion(publicacion);
        repositorioPublicacion.guardarPublicacion(publicacion2);
        repositorioPublicacion.guardarPublicacion(publicacion1);

        ArrayList<Publicacion> publicaciones = servicioFiltro.consultarPublicaciones("", "", "","");

        assertThat(publicaciones.size(), equalTo(3));
    }

    @Test
    @Transactional
    @Rollback
    public void siNoHayCoincidenciasDevuelveUnaListaVacia() {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo("Mascota");
        publicacion.setTipoDeSangre("0+");
        publicacion.setTipoDePublicacion("Donante");

        Publicacion publicacion1 = new Publicacion();
        publicacion1.setTitulo("Mascota");
        publicacion1.setTipoDeSangre("A+");
        publicacion1.setTipoDePublicacion("Donante");

        Publicacion publicacion2 = new Publicacion();
        publicacion2.setTitulo("Mascota");
        publicacion2.setTipoDeSangre("A+");
        publicacion2.setTipoDePublicacion("Donante");

        repositorioPublicacion.guardarPublicacion(publicacion);
        repositorioPublicacion.guardarPublicacion(publicacion2);
        repositorioPublicacion.guardarPublicacion(publicacion1);

        ArrayList<Publicacion> publicaciones = servicioFiltro.consultarPublicaciones("busco vendedor", "A++", "Chile","venta");

        assertThat(publicaciones.size(), equalTo(0));
    }


}
