package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.RepositorioBanco;
import com.tallerwebi.dominio.entidad.Solicitud;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.BancoConTiposDeSangre;
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
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioBancoTest {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioBanco repositorioBanco;


    @Test
    @Transactional
    @Rollback
    void testSaveBanco() {

        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");

        Banco savedBanco = repositorioBanco.guardar(banco);

        assertThat(savedBanco.getId(), notNullValue());
    }

    @Test
    @Transactional
    @Rollback
    void testBuscarBancoPorId() {

        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        Banco savedBanco = repositorioBanco.guardar(banco);

        Banco bancoEncontrado = repositorioBanco.buscarPorId(savedBanco.getId());

        assertEquals(savedBanco.getId(), bancoEncontrado.getId());
    }

    @Transactional
    @Rollback
    @Test
    public void testGuardarSangre() {
        Banco banco = new Banco("Banco Test", "Ciudad", "Dirección", "email@test.com", "9-18", "País", "12345", "123456789");
        repositorioBanco.guardar(banco);
        PaqueteDeSangre paquete = new PaqueteDeSangre("O-", 5, "", banco);

        repositorioBanco.guardarSangre(paquete, banco);

        PaqueteDeSangre paqueteGuardado = repositorioBanco.buscarSangre("O-");
        assertEquals("O-", paqueteGuardado.getTipoSangre());
        assertEquals(5, paqueteGuardado.getCantidad());
    }

    @Test
    @Transactional
    @Rollback
    void testAgregarPaqueteDeSangreSiBancoExiste() {
        // Crear un banco de prueba
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");


        PaqueteDeSangre paquete = new PaqueteDeSangre("A+", 5, "", banco);
        banco.agregarPaqueteDeSangre(paquete);


        Banco bancoConPaquetes = repositorioBanco.guardar(banco);


        assertThat(bancoConPaquetes, is(notNullValue()));
        assertThat(bancoConPaquetes.getPaquetesDeSangre().size(), is(1));


        PaqueteDeSangre paqueteGuardado = bancoConPaquetes.getPaquetesDeSangre().get(0);
        assertThat(paqueteGuardado.getTipoSangre(), is("A+"));
        assertThat(paqueteGuardado.getCantidad(), is(5));
    }

    @Test
    @Transactional
    @Rollback
    void testAgregarVariosPaquetesDeSangreSiBancoExiste() {
        // Crear un banco de prueba
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5,"", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3,"", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7,"", banco);

        banco.agregarPaqueteDeSangre(paqueteA);
        banco.agregarPaqueteDeSangre(paqueteB);
        banco.agregarPaqueteDeSangre(paqueteO);

        // Guardar el banco en la base de datos
        Banco bancoConPaquetes = repositorioBanco.guardar(banco);

        // Verificar que el banco no es nulo
        assertThat(bancoConPaquetes, is(notNullValue()));

        // Verificar que se guardaron los tres paquetes
        List<PaqueteDeSangre> paquetes = bancoConPaquetes.getPaquetesDeSangre();
        assertThat(paquetes, hasSize(3));

        // Verificar las propiedades de los paquetes
        assertThat(paquetes, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("B-")), hasProperty("cantidad", is(3))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));

    }

    @Test
    @Transactional
    @Rollback
    void testGuardarSolicitud() {
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        repositorioBanco.guardar(banco);

        Solicitud solicitud = new Solicitud(banco.getId(), 1L, "Sangre total", "DEA 1.2+", 4);

        Solicitud solicitudGuardada = repositorioBanco.guardarSolicitud(solicitud);


        assertThat(solicitudGuardada.getId(), notNullValue());
        assertThat(solicitudGuardada.getTipoSangre(), is("DEA 1.2+"));
        assertThat(solicitudGuardada.getCantidad(), is(4));
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

        repositorioBanco.guardarSolicitud(solicitud1);
        repositorioBanco.guardarSolicitud(solicitud2);

        List<Solicitud> solicitudes = repositorioBanco.solicitudesPorBanco(banco.getId());

        assertThat(solicitudes, hasSize(2));
        assertThat(solicitudes, hasItems(
                allOf(hasProperty("tipoSangre", is("DEA 1.1+")), hasProperty("cantidad", is(3))),
                allOf(hasProperty("tipoSangre", is("DEA 1.1-")), hasProperty("cantidad", is(2)))
        ));
    }

    @Test
    @Transactional
    @Rollback
    void testBuscarSolicitudPorId() {
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        repositorioBanco.guardar(banco);

        Solicitud solicitud = new Solicitud(banco.getId(), 1L, "Sangre total", "DEA 1.1+", 5);
        Solicitud solicitudGuardada = repositorioBanco.guardarSolicitud(solicitud);

        Solicitud solicitudEncontrada = repositorioBanco.buscarSolicitudPorId(solicitudGuardada.getId());

        assertThat(solicitudEncontrada.getId(), is(solicitudGuardada.getId()));
        assertThat(solicitudEncontrada.getTipoSangre(), is("DEA 1.1+"));
        assertThat(solicitudEncontrada.getCantidad(), is(5));
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

        List<PaqueteDeSangre> paquetesCompatibles = repositorioBanco.obtenerPaquetesDeSangreCompatible(solicitud);


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
        Solicitud solicitudGuardada = repositorioBanco.guardarSolicitud(solicitud);

        repositorioBanco.rechazarSolicitud(solicitudGuardada.getId());

        Solicitud solicitudRechazada = repositorioBanco.buscarSolicitudPorId(solicitudGuardada.getId());
        assertThat(solicitudRechazada.getEstado(), is("Rechazada"));
    }



    @Test
    @Transactional
    @Rollback
    void siNoIngresoUnaSangreValidaParaBuscarNoEncuentroResultados() {
        // Crear un banco de prueba
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        Banco banco1 = new Banco("Banco Test", "Ciudad", "Dirección", "email@test.com", "9-18", "País", "12345", "123456789");

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5,"", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3,"", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7,"", banco);

        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos("C");

        assertThat(resultados.size(),is(0));
    }

    @Test
    @Transactional
    @Rollback
    void siIngresoUnaSangreValidaParaBuscarEncuentroResultados() {
        // Crear un banco de prueba
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        Banco banco1 = new Banco("Banco Test", "Ciudad", "Dirección", "email@test.com", "9-18", "País", "12345", "123456789");

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos("+");

        assertThat(resultados.size(), is(2) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));


    }

    @Test
    @Transactional
    @Rollback
    void siIngresaSangreComoVacioMeDaraTodosLosResultados() {
        // Crear un banco de prueba
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        Banco banco1 = new Banco("Banco Test", "Ciudad", "Dirección", "email@test.com", "9-18", "País", "12345", "123456789");

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos("");

        assertThat(resultados.size(), is(3) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("B-")), hasProperty("cantidad", is(3))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }

    @Test
    @Transactional
    @Rollback
    void siIngresaSangreComoVacioMeDaraTodosLosResultadosEnCoincidenciasDeTipo() {
        // Crear un banco de prueba
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        Banco banco1 = new Banco("Banco Test", "Ciudad", "Dirección", "email@test.com", "9-18", "País", "12345", "123456789");

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= repositorioBanco.obtenerLaCoincidenciaEnTipoDeProductoDeTodosLosBancos("");

        assertThat(resultados.size(), is(3) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("B-")), hasProperty("cantidad", is(3))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));

    }

    @Test
    @Transactional
    @Rollback
    void noObtengoCoincidenciasSiElParametroDeTipoDeProductoEsInvalidoNoExiste() {
        // Crear un banco de prueba
            Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                    "123456789", "test@example.com", "testpassword", "Horario Test");
            Banco banco1 = new Banco("Banco Test", "Ciudad", "Dirección", "email@test.com", "9-18", "País", "12345", "123456789");

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "total", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "globulos", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "total", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= repositorioBanco.obtenerLaCoincidenciaEnTipoDeProductoDeTodosLosBancos("A+");

        assertThat(resultados.size(), is(0) );
    }


    @Test
    @Transactional
    @Rollback
    void obtenerDosCoincidenciasEnTipoDeProductoDeTodosLosBancos() {
        // Crear un banco de prueba
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        Banco banco1 = new Banco("Banco Test", "Ciudad", "Dirección", "email@test.com", "9-18", "País", "12345", "123456789");

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "total", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "globulos", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "total", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= repositorioBanco.obtenerLaCoincidenciaEnTipoDeProductoDeTodosLosBancos("total");

        assertThat(resultados.size(), is(2) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }


    @Test
    @Transactional
    @Rollback
    void siIngresaSangreComoVacioMeDaraTodosLosResultadosEnCoincidenciasDeTipoYSangre() {
        // Crear un banco de prueba
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        Banco banco1 = new Banco("Banco Test", "Ciudad", "Dirección", "email@test.com", "9-18", "País", "12345", "123456789");

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= repositorioBanco.obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos("","");

        assertThat(resultados.size(), is(3) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("B-")), hasProperty("cantidad", is(3))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }

    @Test
    @Transactional
    @Rollback
    void noObtengoCoincidenciasSiElParametroDeTipoDeProductoYElDeSangreEsInvalidoNoExiste() {
        // Crear un banco de prueba
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        Banco banco1 = new Banco("Banco Test", "Ciudad", "Dirección", "email@test.com", "9-18", "País", "12345", "123456789");

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "total", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "globulos", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "total", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= repositorioBanco.obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos("---","---");

        assertThat(resultados.size(), is(0) );
    }


    @Test
    @Transactional
    @Rollback
    void obtenerDosCoincidenciasEnTipoDeProductoYSangreDeTodosLosBancos() {
        // Crear un banco de prueba
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        Banco banco1 = new Banco("Banco Test", "Ciudad", "Dirección", "email@test.com", "9-18", "País", "12345", "123456789");

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "total", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "globulos", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "total", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= repositorioBanco.obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos("+","total");

        assertThat(resultados.size(), is(2) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }


}