package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioUsuario;
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
import org.springframework.transaction.annotation.Transactional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioUsuarioTest {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioUsuario repositorio;

    @Test
    @Transactional
    @Rollback
    public void buscarPorIdAUnUsuarioExistenteMeRetornaAlUsuario() {
        //given
        Long id = 1L;
        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(id);
        usuarioGuardado.setNombre("Carlos");
        //when
        repositorio.guardar(usuarioGuardado);
        Usuario usuarioEncontrado= repositorio.buscarPorId(id);

        //then
        assertThat(usuarioEncontrado, notNullValue());
        assertThat(usuarioEncontrado.getNombre(),equalToIgnoringCase("Carlos"));
    }

    @Test
    @Transactional
    @Rollback
    public void buscarPorIdAUnUsuarioNoExistenteMeRetornaNull() {
        //given
        Long id = 1L;
        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(id);
        usuarioGuardado.setNombre("Carlos");
        //when
        repositorio.guardar(usuarioGuardado);
        Usuario usuarioEncontrado= repositorio.buscarPorId(2L);

        //then
        assertThat(usuarioEncontrado, nullValue());
    }

}
