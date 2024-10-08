package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.MensajeUsuarioBanco;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMensajeUsuarioBanco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ControladorMensajeUsuarioBancoTest {

    private ControladorMensajeUsuarioBanco controladorMensajeUsuarioBanco;
    private Usuario usuarioMock;
    private ServicioMensajeUsuarioBanco servicioMensajeUsuarioBancoMock;
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;

    @BeforeEach
    public void setUp() {
        usuarioMock = mock(Usuario.class);
        servicioMensajeUsuarioBancoMock = mock(ServicioMensajeUsuarioBanco.class);
        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);

        when(requestMock.getSession()).thenReturn(sessionMock);
        when(sessionMock.getAttribute("usuarioEnSesion")).thenReturn(usuarioMock);

        controladorMensajeUsuarioBanco = new ControladorMensajeUsuarioBanco(servicioMensajeUsuarioBancoMock);
    }

    @Test
    public void testEnviarMensajeUsuarioBancoSuccess() throws Exception {
        String mensaje = "Hola Banco";
        int idBanco = 1;
        MensajeUsuarioBanco mensajeEnviadoMock = new MensajeUsuarioBanco();
        mensajeEnviadoMock.setMensaje(mensaje);

        when(servicioMensajeUsuarioBancoMock.enviarMensaje(mensaje, "Usuario", usuarioMock, (long) idBanco))
                .thenReturn(mensajeEnviadoMock);

        ModelAndView model = controladorMensajeUsuarioBanco.enviarMensajeUsuarioBanco(mensaje, idBanco, requestMock);

        assertThat(model.getModel().get("mensajeUsuarioBanco"), is(equalTo("Mensaje enviado al banco con exito")));
        assertThat(model.getModel().get("errorAlEnviarMensaje"), is(nullValue()));
    }

    @Test
    public void testEnviarMensajeUsuarioBancoError() throws Exception {
        String mensaje = "Hola Banco";
        int idBanco = 1;
        when(servicioMensajeUsuarioBancoMock.enviarMensaje(mensaje, "Usuario", usuarioMock, (long) idBanco))
                .thenThrow(new Exception("No se pudo crear el mensaje"));

        ModelAndView model = controladorMensajeUsuarioBanco.enviarMensajeUsuarioBanco(mensaje, idBanco, requestMock);

        assertThat(model.getModel().get("errorAlEnviarMensaje"), is(equalTo("No se pudo crear el mensaje")));
        assertThat(model.getModel().get("mensajeUsuarioBanco"), is(nullValue()));
    }

    @Test
    public void testEnviarMensajeUsuarioBancoErrorDestinatario() throws Exception {
        String mensaje = "Hola Banco";
        int idBanco = 1;
        when(servicioMensajeUsuarioBancoMock.enviarMensaje(mensaje, "Usuario", usuarioMock, (long) idBanco))
                .thenThrow(new Exception("El destinatario no existe"));

        ModelAndView model = controladorMensajeUsuarioBanco.enviarMensajeUsuarioBanco(mensaje, idBanco, requestMock);

        assertThat(model.getModel().get("errorAlEnviarMensaje"), is(equalTo("El destinatario no existe")));
        assertThat(model.getModel().get("mensajeUsuarioBanco"), is(nullValue()));
    }
}
