package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.RepositorioBanco;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.DTO.BancoConTiposDeSangreDTO;
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
import static org.junit.jupiter.api.Assertions.*;


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

    @Test
    @Transactional
    @Rollback
    public void testActualizarBanco() {
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");

        Banco bancoGuardado = repositorioBanco.guardar(banco);

        bancoGuardado.setCiudad("Nueva Ciudad");
        bancoGuardado.setHorario("Nuevo Horario");

        repositorioBanco.actualizarBanco(bancoGuardado);

        Banco bancoActualizado = repositorioBanco.buscarPorId(bancoGuardado.getId());
        assertEquals("Nueva Ciudad", bancoActualizado.getCiudad());
        assertEquals("Nuevo Horario", bancoActualizado.getHorario());
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
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");

        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5,"", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3,"", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7,"", banco);

        banco.agregarPaqueteDeSangre(paqueteA);
        banco.agregarPaqueteDeSangre(paqueteB);
        banco.agregarPaqueteDeSangre(paqueteO);

        Banco bancoConPaquetes = repositorioBanco.guardar(banco);

        assertThat(bancoConPaquetes, is(notNullValue()));

        List<PaqueteDeSangre> paquetes = bancoConPaquetes.getPaquetesDeSangre();
        assertThat(paquetes, hasSize(3));


        assertThat(paquetes, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("B-")), hasProperty("cantidad", is(3))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));

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

        List<BancoConTiposDeSangreDTO> resultados= repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos("C");

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

        List<BancoConTiposDeSangreDTO> resultados= repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos("+");

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

        List<BancoConTiposDeSangreDTO> resultados= repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos("");

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

        List<BancoConTiposDeSangreDTO> resultados= repositorioBanco.obtenerLaCoincidenciaEnTipoDeProductoDeTodosLosBancos("");

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

        List<BancoConTiposDeSangreDTO> resultados= repositorioBanco.obtenerLaCoincidenciaEnTipoDeProductoDeTodosLosBancos("A+");

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

        List<BancoConTiposDeSangreDTO> resultados= repositorioBanco.obtenerLaCoincidenciaEnTipoDeProductoDeTodosLosBancos("total");

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

        List<BancoConTiposDeSangreDTO> resultados= repositorioBanco.obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos("","");

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

        List<BancoConTiposDeSangreDTO> resultados= repositorioBanco.obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos("---","---");

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

        List<BancoConTiposDeSangreDTO> resultados= repositorioBanco.obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos("+","total");

        assertThat(resultados.size(), is(2) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }

    @Test
    @Transactional
    @Rollback
    void testObtenerTodosLosBancosNoVerificados() {

        Banco banco1 = new Banco("Banco 1", "Dirección 1", "Ciudad 1", "País 1",
                "111111111", "banco1@example.com", "password1", "Horario 1");
        banco1.setActivo(false);

        Banco banco2 = new Banco("Banco 2", "Dirección 2", "Ciudad 2", "País 2",
                "222222222", "banco2@example.com", "password2", "Horario 2");
        banco2.setActivo(false);

        Banco banco3 = new Banco("Banco 3", "Dirección 3", "Ciudad 3", "País 3",
                "333333333", "banco3@example.com", "password3", "Horario 3");
        banco3.setActivo(true);

        repositorioBanco.guardar(banco1);
        repositorioBanco.guardar(banco2);
        repositorioBanco.guardar(banco3);

        List<Banco> bancosNoVerificados = repositorioBanco.obtenerTodosLosBancosNoVerificados();

        assertThat(bancosNoVerificados, hasSize(2));
        assertThat(bancosNoVerificados, containsInAnyOrder(banco1, banco2));
        assertThat(bancosNoVerificados, not(contains(banco3)));


        for (Banco banco : bancosNoVerificados) {
            assertFalse(banco.getActivo());
        }
    }

    @Test
    @Transactional
    @Rollback
    void borrarBancoDeLaBaseDeDatos() {

        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");

        repositorioBanco.guardar(banco);

        Banco bancoGuardado = repositorioBanco.buscarPorId(banco.getId());
        assertNotNull(bancoGuardado);

        boolean resultado = repositorioBanco.borrarBanco(banco.getId());

        assertTrue(resultado);

        Banco bancoEliminado = repositorioBanco.buscarPorId(banco.getId());
        assertNull(bancoEliminado);
    }

}