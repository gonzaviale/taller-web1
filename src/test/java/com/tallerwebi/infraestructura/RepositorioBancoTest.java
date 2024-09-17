package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Banco;
import com.tallerwebi.dominio.PaqueteDeSangre;
import com.tallerwebi.dominio.RepositorioBanco;
import com.tallerwebi.dominio.RepositorioUsuario;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})


public class RepositorioBancoTest {

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    private RepositorioBanco bancoRepository;

    @Transactional
    @Test
    @Rollback
    void testSaveBanco() {


        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");

        Banco savedBanco = bancoRepository.guardar(banco);

        assertThat(savedBanco, is(notNullValue()));
        assertThat(savedBanco.getId(), is(notNullValue()));
        assertThat(savedBanco.getNombreBanco(), is(equalTo("Banco Test")));
    }

    @Transactional
    @Test
    @Rollback
    void testBuscarPorCorreoyPass() {

        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");

        Banco savedBanco = bancoRepository.guardar(banco);


        assertThat(savedBanco, is(notNullValue()));
        assertThat(savedBanco.getId(), is(notNullValue()));
        assertThat(savedBanco.getNombreBanco(), is(equalTo("Banco Test")));
        assertThat(savedBanco.getDireccion(), is(equalTo("Dirección Test")));

    }
    @Test
    @Transactional
    @Rollback
    void testBuscarBancoPorId() {
        // Crear y guardar un banco de prueba
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
        Banco savedBanco = bancoRepository.guardar(banco);

        // Buscar el banco por ID
        Banco bancoEncontrado = bancoRepository.buscarPorId(savedBanco.getId());

        // Verificar que el banco encontrado no sea nulo
        assertThat(bancoEncontrado, is(notNullValue()));

        // Verificar que el banco encontrado tenga los mismos detalles que el banco guardado
        assertThat(bancoEncontrado.getId(), is(equalTo(savedBanco.getId())));
        assertThat(bancoEncontrado.getNombreBanco(), is(equalTo("Banco Test")));
    }

    @Test
    @Transactional
    @Rollback
    void testAgregarPaqueteDeSangreSiBancoExiste() {
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");


        banco.agregarPaqueteDeSangre("A+", 5);
        Banco bancoConPaquetes = bancoRepository.guardar(banco);


        assertThat(bancoConPaquetes, is(notNullValue()));
        assertThat(bancoConPaquetes.getPaquetesDeSangre().size(), is(1));




    }
    @Test
    @Transactional
    @Rollback
    void testAgregarVariosPaquetesDeSangreSiBancoExiste() {
        // Crear un banco de prueba
        Banco banco = new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");


        banco.agregarPaqueteDeSangre("A+", 5);
        banco.agregarPaqueteDeSangre("B-", 3);
        banco.agregarPaqueteDeSangre("O+", 7);


        Banco bancoConPaquetes = bancoRepository.guardar(banco);


        assertThat(bancoConPaquetes, is(notNullValue()));


        assertThat(bancoConPaquetes.getPaquetesDeSangre().size(), is(3));


        List<PaqueteDeSangre> paquetes = bancoConPaquetes.getPaquetesDeSangre();
        assertThat(paquetes, hasSize(3));
        assertThat(paquetes, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("B-")), hasProperty("cantidad", is(3))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }



}
