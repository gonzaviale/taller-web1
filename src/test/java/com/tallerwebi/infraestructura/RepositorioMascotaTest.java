package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Canino;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.RepositorioMascota;
import com.tallerwebi.dominio.entidad.Publicacion;
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

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioMascotaTest {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioMascota repositorioMascota;

    @Test
    @Transactional
    @Rollback
    public void queGuardeMascota(){
        Mascota mascota = new Canino();
        mascota.setNombre("tobi");

        repositorioMascota.agregarMascota(mascota);

        assertThat(mascota.getId(), notNullValue());
    }

    @Test
    @Transactional
    @Rollback
    public void queMeTraigaMascotasPorNombre(){
        Mascota mascota = new Canino();
        mascota.setNombre("tobi");

        repositorioMascota.agregarMascota(mascota);
        ArrayList<Mascota> mascotasPorNombre = repositorioMascota.buscarMascota("tobi", "", "");
        String nombreRecibido = mascotasPorNombre.get(0).getNombre();
        String nombreEsperado = "tobi";

        assertEquals(nombreRecibido, nombreEsperado);
    }

    @Test
    @Transactional
    @Rollback
    public void queMeTraigaMascotasPorSangre(){
        Mascota mascota = new Canino();
        mascota.setSangre("0+");

        repositorioMascota.agregarMascota(mascota);
        ArrayList<Mascota> mascotasPorSangre = repositorioMascota.buscarMascota("", "0+", "");
        String sangreRecibida = mascotasPorSangre.get(0).getSangre();

        assertEquals(sangreRecibida, "0+");
    }

    @Test
    @Transactional
    @Rollback
    public void queMeTraigaMascotasPorTipo(){
        Mascota mascota = new Mascota();
        mascota.setTipo("Canino");

        repositorioMascota.agregarMascota(mascota);
        ArrayList<Mascota> mascotasPorTipo = repositorioMascota.buscarMascota("","","Canino");
        String tipoRecibida = mascotasPorTipo.get(0).getTipo();

        assertEquals(tipoRecibida, "Canino");
    }
//el test entra en un bucle del que no puede salir
//    @Test
//    @Transactional
//    @Rollback
//    public void queMeTraigaMascotasEnRevision(){
//        Mascota mascota = new Mascota();
//        mascota.setRevision(true);
//        mascota.setNombre("firu");
//
//        repositorioMascota.agregarMascota(mascota);
//
//        List<Mascota> mascotasEnRevision = repositorioMascota.buscarMascotaEnRevision();
//
//       assertThat(mascotasEnRevision.get(0).getNombre(), equalToIgnoringCase(mascota.getNombre()));
//    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedaAprobarMascotaDonante() {

        Mascota mascota = new Mascota();
        mascota.setRevision(true);
        mascota.setId(1L);
        repositorioMascota.agregarMascota(mascota);

        repositorioMascota.aprobarMascota(mascota.getId());


        assertFalse(mascota.isEnRevision());
        assertTrue(mascota.isAprobado());
    }

    @Test
    @Transactional
    @Rollback
    public void quePuedaRechazarMascotaDonante() {

        Mascota mascota = new Mascota();
        mascota.setRevision(true);
        repositorioMascota.agregarMascota(mascota);
        repositorioMascota.rechazarMascota(mascota.getId());

        assertFalse(mascota.isEnRevision());
        assertTrue(mascota.isRechazado());
    }

    @Test
    @Transactional
    @Rollback
    public void eliminarMascota_deberia_no_eliminar_si_esta_asociada() {
        Mascota mascota = new Mascota();
        mascota.setNombre("Firulais");
        mascota.setTipo("Canino");
        mascota.setPeso(25f);
        mascota.setAnios(5);

        sessionFactory.getCurrentSession().save(mascota);

        Publicacion publicacion = new Publicacion();
        publicacion.setMascotaDonante(mascota); // Relación con la mascota
        sessionFactory.getCurrentSession().save(publicacion);

        Boolean resultado = repositorioMascota.eliminarMascota(mascota);

        assertFalse(resultado);
        Mascota mascotaRecuperada = sessionFactory.getCurrentSession().get(Mascota.class, mascota.getId());
        assertNotNull(mascotaRecuperada);
    }

    @Test
    @Transactional
    @Rollback
    public void eliminarMascota_deberia_eliminar_si_no_esta_asociada() {
        Mascota mascota = new Mascota();
        mascota.setNombre("Luna");
        mascota.setTipo("Felino");
        mascota.setPeso(4.5f);
        mascota.setAnios(3);

        sessionFactory.getCurrentSession().save(mascota);

        Boolean resultado = repositorioMascota.eliminarMascota(mascota);

        assertTrue(resultado);
        Mascota mascotaRecuperada = sessionFactory.getCurrentSession().get(Mascota.class, mascota.getId());
        assertNull(mascotaRecuperada);
    }

    @Test
    @Transactional
    @Rollback
    public void actualizarMascota_deberia_actualizar_los_datos_correctamente() {
        Mascota mascotaInicial = new Mascota();
        mascotaInicial.setNombre("Firulais");
        mascotaInicial.setTipo("Canino");
        mascotaInicial.setPeso(30f);
        mascotaInicial.setAnios(3);
        mascotaInicial.setSangre("A+");
        repositorioMascota.agregarMascota(mascotaInicial);

        Mascota mascotaActualizada = new Mascota();
        mascotaActualizada.setId(mascotaInicial.getId());
        mascotaActualizada.setNombre("Max");
        mascotaActualizada.setTipo("Canino");
        mascotaActualizada.setPeso(35f);
        mascotaActualizada.setAnios(4);
        mascotaActualizada.setSangre("B+");

        repositorioMascota.actualizarMascota(mascotaActualizada);

        Mascota mascotaRecuperada = repositorioMascota.buscarMascotaPorId(mascotaInicial.getId());
        assertNotNull(mascotaRecuperada);
        assertEquals("Max", mascotaRecuperada.getNombre());
        assertEquals("Canino", mascotaRecuperada.getTipo());
        assertEquals(35f, mascotaRecuperada.getPeso());
        assertEquals(4, mascotaRecuperada.getAnios());
        assertEquals("B+", mascotaRecuperada.getSangre());
    }

}