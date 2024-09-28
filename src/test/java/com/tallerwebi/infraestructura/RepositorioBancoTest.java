package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Banco;
import com.tallerwebi.dominio.PaqueteDeSangre;
import com.tallerwebi.dominio.RepositorioBanco;
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
        PaqueteDeSangre paquete = new PaqueteDeSangre("O-", 5,"",banco );

        repositorioBanco.guardarSangre(paquete, banco);

        PaqueteDeSangre  paqueteGuardado=repositorioBanco.buscarSangre("O-");
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


        PaqueteDeSangre paquete = new PaqueteDeSangre("A+", 5,"", banco);
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





}
