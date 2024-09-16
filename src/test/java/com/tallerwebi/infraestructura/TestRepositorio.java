package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.Banco;
import com.tallerwebi.infraestructura.RepositorioUsuarioImpl;

import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;



import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})

public class TestRepositorio{
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    private RepositorioUsuario bancoRepository ;

    @Transactional
    @Test
    @Rollback
    void testSaveBanco() {


        Banco banco = new Banco();
        banco.setNombreBanco("Banco Test");
        banco.setDireccion("Dirección Test");
        banco.setCiudad("Ciudad Test");
        banco.setPais("País Test");
        banco.setTelefono("123456789");
        banco.setEmail("test@example.com");
        banco.setPassword("testpassword");
        banco.setHorario("Horario Test");

        Banco savedBanco = bancoRepository.guardarBanco(banco);

        assertThat(savedBanco, is(notNullValue()));
        assertThat(savedBanco.getId(), is(notNullValue()));
        assertThat(savedBanco.getNombreBanco(), is(equalTo("Banco Test")));
    }

    @Transactional
    @Test
    @Rollback
    void testBuscarPorCorreoyPass() {
        Banco banco = new Banco();
        banco.setNombreBanco("Banco Test");
        banco.setDireccion("Dirección Test");
        banco.setCiudad("Ciudad Test");
        banco.setPais("País Test");
        banco.setTelefono("123456789");
        banco.setEmail("test@example.com");
        banco.setPassword("testpassword");
        banco.setHorario("Horario Test");

        Banco savedBanco = bancoRepository.guardarBanco(banco);



        assertThat(savedBanco, is(notNullValue()));
        assertThat(savedBanco.getId(), is(notNullValue()));
        assertThat(savedBanco.getNombreBanco(), is(equalTo("Banco Test")));
        assertThat(savedBanco.getDireccion(), is(equalTo("Dirección Test")));
        assertThat(savedBanco.getCiudad(), is(equalTo("Ciudad Test")));
        assertThat(savedBanco.getPais(), is(equalTo("País Test")));
        assertThat(savedBanco.getTelefono(), is(equalTo("123456789")));
        assertThat(savedBanco.getEmail(), is(equalTo("test@example.com")));
        assertThat(savedBanco.getPassword(), is(equalTo("testpassword")));
        assertThat(savedBanco.getHorario(), is(equalTo("Horario Test")));
    }


}