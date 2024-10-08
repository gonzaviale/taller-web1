package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.MensajeUsuarioBanco;
import com.tallerwebi.dominio.entidad.Usuario;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioMensajeUsuarioBancoTest {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioMensajeUsuarioBancoImpl repositorioMensajeUsuarioBanco;

    @Test
    @Transactional
    @Rollback
    public void queGuardeMensaje(){
        MensajeUsuarioBanco mensaje =
                this.repositorioMensajeUsuarioBanco
                        .crearMensaje("hola banco", "Usuario", new Usuario(), new Banco());

        assertThat(mensaje, notNullValue());
        assertThat(mensaje.getMensaje(), mensaje.getMensaje().equals("hola banco"));
    }

    @Test
    @Transactional
    @Rollback
    public void queDevuelvaNull() {
        MensajeUsuarioBanco mensaje =
                this.repositorioMensajeUsuarioBanco
                        .crearMensaje("hola banco", "", new Usuario(), new Banco());

        assertThat(mensaje, is(nullValue()));
    }

}
