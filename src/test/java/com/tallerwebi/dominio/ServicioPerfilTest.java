package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioPerfil;
import com.tallerwebi.dominio.servicio.ServicioPerfilImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ServicioPerfilTest {

    private RepositorioUsuario repositorioUsuario;

    private ServicioPerfil servicioUsuario;

    @BeforeEach
    public void setUp() {
        repositorioUsuario=mock (RepositorioUsuario.class);
        servicioUsuario = new ServicioPerfilImpl(repositorioUsuario);
    }


    @Test
    public void cuandoBuscoUnUsuarioConIdExistenteMeDevuelveAlUsuario() {

        Long id = 1L;
        Usuario usuarioEsperado = new Usuario();
        usuarioEsperado.setId(id);
        usuarioEsperado.setNombre("Juan");

        when(repositorioUsuario.buscarPorId(id)).thenReturn(usuarioEsperado);

        Usuario usuarioEncontrado = servicioUsuario.buscarUsuarioPorId(id);

        assertThat(usuarioEncontrado, notNullValue());
        assertThat(usuarioEncontrado.getId(), equalTo(id));
        assertThat(usuarioEncontrado.getNombre(), equalTo ("Juan"));
        verify(repositorioUsuario, times(1)).buscarPorId(id);
    }

    @Test
    public void cuandoBuscoUnUsuarioPorUnIdQueNoExisteElRepositorioMeEntregaUnUsuarioNulo() {

        Long id = 1L;

        when(repositorioUsuario.buscarPorId(id)).thenReturn(null);

        Usuario usuarioEncontrado = servicioUsuario.buscarUsuarioPorId(id);

        assertThat(usuarioEncontrado,nullValue());

        verify(repositorioUsuario, times(1)).buscarPorId(id);
    }

}
