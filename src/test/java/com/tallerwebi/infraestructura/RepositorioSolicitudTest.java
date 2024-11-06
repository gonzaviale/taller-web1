package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioBanco;
import com.tallerwebi.dominio.RepositorioSolicitud;
import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Entrega;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Solicitud;
import com.tallerwebi.dominio.servicio.ServicioSolicitudImpl;
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

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioSolicitudTest {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioSolicitud repositorioSolicitud;
    @Autowired
    RepositorioBanco repositorioBanco;



    @Test
    @Transactional
    @Rollback
    void testGuardarSolicitud() {
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        repositorioBanco.guardar(banco);

        Solicitud solicitud = new Solicitud(banco.getId(), 1L, "Sangre total", "DEA 1.2+", 4);

        Solicitud solicitudGuardada = repositorioSolicitud.guardarSolicitud(solicitud);


        assertThat(solicitudGuardada.getId(), notNullValue());
        assertThat(solicitudGuardada.getTipoSangre(), is("DEA 1.2+"));
        assertThat(solicitudGuardada.getCantidad(), is(4));
    }

    @Test
    @Transactional
    @Rollback
    void testGuardarEntrega() {

        Entrega entrega = new Entrega();
        entrega.setSolicitudId(1);
        entrega.setPaqueteId(100L);
        entrega.setDireccion("Calle de Prueba 123");

        Entrega entregaGuardada = repositorioSolicitud.guardarEntrega(entrega);

        assertThat(entregaGuardada.getId(), notNullValue());
        assertThat(entregaGuardada.getSolicitudId(), is(1));
        assertThat(entregaGuardada.getPaqueteId(), is(100L));
        assertThat(entregaGuardada.getDireccion(), is("Calle de Prueba 123"));
    }



    @Test
    @Transactional
    @Rollback
    void testBuscarSolicitudPorId() {
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        repositorioBanco.guardar(banco);

        Solicitud solicitud = new Solicitud(banco.getId(), 1L, "Sangre total", "DEA 1.1+", 5);
        Solicitud solicitudGuardada = repositorioSolicitud.guardarSolicitud(solicitud);

        Solicitud solicitudEncontrada = repositorioSolicitud.buscarSolicitudPorId(solicitudGuardada.getId());

        assertThat(solicitudEncontrada.getId(), is(solicitudGuardada.getId()));
        assertThat(solicitudEncontrada.getTipoSangre(), is("DEA 1.1+"));
        assertThat(solicitudEncontrada.getCantidad(), is(5));
    }


    @Test
    @Transactional
    @Rollback
    void testSolicitudesPorBanco() {
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        repositorioBanco.guardar(banco);

        Solicitud solicitud1 = new Solicitud(banco.getId(), 1L, "Plasma fresco congelado", "DEA 1.1+", 3);
        Solicitud solicitud2 = new Solicitud(banco.getId(), 2L, "Glóbulos rojos empaquetados", "DEA 1.1-", 2);

        repositorioSolicitud.guardarSolicitud(solicitud1);
        repositorioSolicitud.guardarSolicitud(solicitud2);

        List<Solicitud> solicitudes = repositorioSolicitud.solicitudesPorBanco(banco.getId());

        assertThat(solicitudes, hasSize(2));
        assertThat(solicitudes, hasItems(
                allOf(hasProperty("tipoSangre", is("DEA 1.1+")), hasProperty("cantidad", is(3))),
                allOf(hasProperty("tipoSangre", is("DEA 1.1-")), hasProperty("cantidad", is(2)))
        ));
    }

    @Test
    @Transactional
    @Rollback
    void testObtenerPaquetesDeSangreCompatible() {
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        repositorioBanco.guardar(banco);

        PaqueteDeSangre paqueteA = new PaqueteDeSangre("DEA 1.1+", 10, "Sangre total", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("DEA 1.1-", 5, "Plasma fresco congelado", banco);
        PaqueteDeSangre paqueteC = new PaqueteDeSangre("DEA 1.1+", 7, "Sangre total", banco);
        repositorioBanco.guardarSangre(paqueteA, banco);
        repositorioBanco.guardarSangre(paqueteB, banco);
        repositorioBanco.guardarSangre(paqueteC, banco);

        Solicitud solicitud = new Solicitud(banco.getId(), 1L, "Sangre total", "DEA 1.1+", 7);

        List<PaqueteDeSangre> paquetesCompatibles =  repositorioSolicitud.obtenerPaquetesDeSangreCompatible(solicitud);


        assertThat(paquetesCompatibles, hasSize(2));
        assertThat(paquetesCompatibles, hasItems(
                allOf(hasProperty("tipoSangre", is("DEA 1.1+")), hasProperty("cantidad", greaterThanOrEqualTo(7)))
        ));

    }

    @Test
    @Transactional
    @Rollback
    void testRechazarSolicitud() {
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        repositorioBanco.guardar(banco);

        Solicitud solicitud = new Solicitud(banco.getId(), 1L, "Sangre total", "DEA 1.1+", 5);
        Solicitud solicitudGuardada = repositorioSolicitud.guardarSolicitud(solicitud);

        repositorioSolicitud.rechazarSolicitud(solicitudGuardada.getId());

        Solicitud solicitudRechazada = repositorioSolicitud.buscarSolicitudPorId(solicitudGuardada.getId());
        assertThat(solicitudRechazada.getEstado(), is("Rechazada"));
    }

    @Test
    @Transactional
    @Rollback
    void testAprobarSolicitud() {
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        repositorioBanco.guardar(banco);

        Solicitud solicitud = new Solicitud(banco.getId(), 1L, "Sangre total", "DEA 1.1+", 5);
        Solicitud solicitudGuardada = repositorioSolicitud.guardarSolicitud(solicitud);

        repositorioSolicitud.solicitudAprobar(solicitudGuardada.getId());

        Solicitud solicitudAprobada = repositorioSolicitud.buscarSolicitudPorId(solicitudGuardada.getId());
        assertThat(solicitudAprobada.getEstado(), is("aprobada"));
    }



}
