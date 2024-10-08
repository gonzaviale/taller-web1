package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.MensajeUsuarioBanco;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMensajeUsuarioBancoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioMensajeUsuarioBancoTest {

    private ServicioMensajeUsuarioBancoImpl servicioMensajeUsuarioBanco;
    private RepositorioMensajeUsuarioBanco repositorioMensajeUsuarioBanco;

    @BeforeEach
    public void setUp() {
        repositorioMensajeUsuarioBanco = mock(RepositorioMensajeUsuarioBanco.class);
        servicioMensajeUsuarioBanco = new ServicioMensajeUsuarioBancoImpl(repositorioMensajeUsuarioBanco);
    }

    @Test
    public void testCrearMensajeSucces() throws Exception {
        Long idBanco = 1L;
        Banco banco = new Banco();
        banco.setId(idBanco);
        banco.setNombreBanco("Banco Test");

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Usuario Test");

        MensajeUsuarioBanco mensaje = new MensajeUsuarioBanco();
        mensaje.setId(1L);
        mensaje.setUsuario(usuario);
        mensaje.setBanco(banco);
        mensaje.setMensaje("hola banco");


        when(repositorioMensajeUsuarioBanco.searchBankById(idBanco)).thenReturn(banco);
        when(repositorioMensajeUsuarioBanco.crearMensaje("hola banco", "Usuario", usuario, banco))
                .thenReturn(mensaje);

        MensajeUsuarioBanco mensajeSucces = servicioMensajeUsuarioBanco
                .enviarMensaje("hola banco", "Usuario", usuario, idBanco);

        assertThat(mensajeSucces, is(notNullValue()));
        assertThat(mensajeSucces.getMensaje(), is(equalTo("hola banco")));
    }

    @Test
    public void testCrearMensajeFailSearchBank() {
        Long idBanco = 1L;
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Usuario Test");

        when(repositorioMensajeUsuarioBanco.searchBankById(idBanco)).thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            servicioMensajeUsuarioBanco.enviarMensaje("hola banco", "Usuario", usuario, idBanco);
        });

        assertThat(exception.getMessage(), is(equalTo("El destinatario no existe")));
    }

    @Test
    public void testCrearMensajeFail() {
        Long idBanco = 1L;
        Banco banco = new Banco();
        banco.setId(idBanco);
        banco.setNombreBanco("Banco Test");
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombre("Usuario Test");

        when(repositorioMensajeUsuarioBanco.searchBankById(idBanco)).thenReturn(banco);
        when(repositorioMensajeUsuarioBanco.crearMensaje("hola banco", "Usuario", usuario, banco))
                .thenReturn(null);

        Exception exception = assertThrows(Exception.class, () -> {
            servicioMensajeUsuarioBanco.enviarMensaje("hola banco", "Usuario", usuario, idBanco);
        });

        assertThat(exception.getMessage(), is(equalTo("No se pudo crear el mensaje")));
    }

}
