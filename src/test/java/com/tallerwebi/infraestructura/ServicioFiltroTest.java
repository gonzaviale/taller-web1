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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class ServicioFiltroTest {

    @Autowired
    RepositorioMascota repositorioMascota;
    @Autowired
    ServicioFiltro servicioFiltro = new ServicioFiltroImpl(repositorioMascota);

    @Test
    @Transactional
    @Rollback
    public void filtrarMascota() {
        Mascota mascota = new Canino();
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
        Mascota mascota = new Felino();
        mascota.setNombre("Mascota");
        mascota.setSangre("0+");
        mascota.setTipo("Donante");

        Mascota mascota1 = new Canino();
        mascota1.setNombre("Mascota1");
        mascota1.setSangre("0+");
        mascota1.setTipo("Donante");

        Mascota mascota2 = new Felino();
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

        assertEquals(mascotas.size(), esperadas.size());
    }
}
