package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Canino;
import com.tallerwebi.dominio.Mascota;
import com.tallerwebi.dominio.RepositorioMascota;
import com.tallerwebi.dominio.Usuario;
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
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

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
        //preparacion
        Mascota mascota = new Canino();
        mascota.setNombre("tobi");

        //ejecucion
        repositorioMascota.agregarMascota(mascota);

        //validacion
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

//    @Test
//    @Transactional
//    @Rollback
//    public void queMeTraigaMascotasEnRevision(){
//        Mascota mascota = new Mascota();
//        mascota.setRevision(true);
//
//        Mascota mascota2 = new Mascota();
//        mascota2.setRevision(true);
//
//        Mascota mascota3 = new Mascota();
//        mascota3.setRevision(false);
//
//        repositorioMascota.agregarMascota(mascota);
//        repositorioMascota.agregarMascota(mascota2);
//        repositorioMascota.agregarMascota(mascota3);
//        List<Mascota> mascotasEnRevision = repositorioMascota.buscarMascotaEnRevision();
//
//        assertEquals(mascotasEnRevision.size(), 2);
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void queSePuedaAprobarMascotaDonante() {
//
//        Mascota mascota = new Mascota();
//        mascota.setRevision(true);
//        mascota.setId(1l);
//        repositorioMascota.agregarMascota(mascota);
//
//        repositorioMascota.aprobarMascotaDonante(mascota.getId());
//
//
//        assertFalse(mascota.isEnRevision());
//        assertTrue(mascota.isAprobado());
//    }
//
//    @Test
//    @Transactional
//    @Rollback
//    public void queRechazarMascotaDonante() {
//
//        Mascota mascota = new Mascota();
//        mascota.setRevision(true);
//        repositorioMascota.agregarMascota(mascota);
//        repositorioMascota.rechazarMascotaDonante(mascota.getId());
//
//        assertFalse(mascota.isEnRevision());
//        assertTrue(mascota.isRechazado());
//    }
}