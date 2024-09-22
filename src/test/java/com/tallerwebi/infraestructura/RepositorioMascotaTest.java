package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Mascota;
import com.tallerwebi.dominio.RepositorioMascota;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Optional;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        Mascota mascota = new Mascota();
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
        Mascota mascota = new Mascota();
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
        Mascota mascota = new Mascota();
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
        mascota.setTipo("Donante");

        repositorioMascota.agregarMascota(mascota);
        ArrayList<Mascota> mascotasPorTipo = repositorioMascota.buscarMascota("","","Donante");
        String tipoRecibida = mascotasPorTipo.get(0).getTipo();

        assertEquals(tipoRecibida, "Donante");
    }

}
