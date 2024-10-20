package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioBanco;
import com.tallerwebi.dominio.RepositorioCampania;
import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Campana;
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
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioCampaniaTest {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioCampania repositorioCampania;

    @Autowired
    RepositorioBanco repositorioBanco;


    @Test
    @Transactional
    @Rollback
    public void testObtenerCampañasActualesYProximas() {
       Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        repositorioBanco.guardar(banco);

        Campana campanaActual = new Campana("Campaña Actual", LocalDate.now().minusDays(1), LocalDate.now().plusDays(5), "Ubicación Actual", "Descripción Actual", banco);
        Campana campanaFutura = new Campana("Campaña Futura", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), "Ubicación Futura", "Descripción Futura", banco);

        banco.agregarCampania(campanaActual);
        banco.agregarCampania(campanaFutura);

        repositorioCampania.guardarCampania(campanaActual,banco);
        repositorioCampania.guardarCampania(campanaFutura,banco);

        List<Campana> campañas = repositorioCampania.obtenerCampanasActualesYproximas(LocalDate.now());


        assertThat(campañas, hasSize(2));
        assertThat(campañas, containsInAnyOrder(campanaActual, campanaFutura));


    }

    @Test
    @Transactional
    @Rollback
    public void testObtenerCampañasActualesYProximasnoDeberiaMostrarLasQueNoSon() {
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        repositorioBanco.guardar(banco);

        Campana campanaActual = new Campana("Campaña Actual", LocalDate.now().minusDays(1), LocalDate.now().plusDays(5), "Ubicación Actual", "Descripción Actual", banco);
        Campana campanaFutura = new Campana("Campaña Futura", LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), "Ubicación Futura", "Descripción Futura", banco);
        Campana campanaPasada = new Campana("Campaña Pasada", LocalDate.now().minusDays(10), LocalDate.now().minusDays(2), "Ubicación Pasada", "Descripción Pasada", banco);
        banco.agregarCampania(campanaActual);
        banco.agregarCampania(campanaFutura);
        banco.agregarCampania( campanaPasada);

        repositorioCampania.guardarCampania(campanaActual,banco);
        repositorioCampania.guardarCampania(campanaFutura,banco);
        repositorioCampania.guardarCampania(campanaPasada,banco);

        List<Campana> campañas = repositorioCampania.obtenerCampanasActualesYproximas(LocalDate.now());

        assertThat(campañas, hasSize(2));
        assertThat(campañas, containsInAnyOrder(campanaActual, campanaFutura));
        assertThat(campañas, not(hasItem(campanaPasada)));
    }


    @Test
    @Transactional
    @Rollback
    public void testGuardarCampania() {

        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        Banco bancoGuardado = repositorioBanco.guardar(banco);
        Campana campana = new Campana();
        campana.setBanco(bancoGuardado);
        repositorioCampania.guardarCampania(campana, bancoGuardado);
        Campana campanaGuardada = repositorioCampania.buscarCampaniaPorId(campana.getId());

        assertThat(campanaGuardada, notNullValue());
        assertThat(campanaGuardada.getId(), is(campana.getId()));

    }


}
