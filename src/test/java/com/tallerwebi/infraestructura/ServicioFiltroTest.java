package com.tallerwebi.infraestructura;


import com.tallerwebi.dominio.Mascota;
import com.tallerwebi.dominio.RepositorioMascota;
import com.tallerwebi.dominio.RepositorioPublicacion;
import com.tallerwebi.dominio.ServicioFiltro;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        assertEquals(recibida, mascota);
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

        assertEquals(mascotas, esperadas);
    }
}
