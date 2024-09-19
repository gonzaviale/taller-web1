package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Banco;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.excepcion.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ControladorLoginTest {

	private ControladorLogin controladorLogin;
	private Usuario usuarioMock;
	private DatosLogin datosLoginMock;
	private HttpServletRequest requestMock;
	private HttpSession sessionMock;
	private ServicioLogin servicioLoginMock;
	private Banco bancoMock;

	@BeforeEach
	public void init(){
		datosLoginMock = new DatosLogin("dami@unlam.com", "123");
		usuarioMock = mock(Usuario.class);
		 bancoMock = mock(Banco.class);
		when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
		when(usuarioMock.getPassword()).thenReturn("123");
		when(usuarioMock.getApellido()).thenReturn("Alarcón");
		when(usuarioMock.getNombre()).thenReturn("Lucía");
		when(usuarioMock.getRol()).thenReturn("a");
		requestMock = mock(HttpServletRequest.class);
		sessionMock = mock(HttpSession.class);
		servicioLoginMock = mock(ServicioLogin.class);
		controladorLogin = new ControladorLogin(servicioLoginMock);
	}

	@Test
	public void loginConUsuarioYPasswordInorrectosDeberiaLlevarALoginNuevamente(){
		// preparacion
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(null);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("login"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Usuario o clave incorrecta"));
		verify(sessionMock, times(0)).setAttribute("ROL", "ADMIN");
	}
	
	@Test
	public void loginConUsuarioYPasswordCorrectosDeberiaLLevarAHome(){
		// preparacion
		Usuario usuarioEncontradoMock = mock(Usuario.class);
		when(usuarioEncontradoMock.getRol()).thenReturn("ADMIN");

		when(requestMock.getSession()).thenReturn(sessionMock);
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(usuarioEncontradoMock);
		
		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);
		
		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
		verify(sessionMock, times(1)).setAttribute("ROL", usuarioEncontradoMock.getRol());
	}

	@Test
	public void registrameSiUsuarioNoExisteDeberiaCrearUsuarioYVolverAlLogin() throws UsuarioExistente {

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123");

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
		verify(servicioLoginMock, times(1)).registrar(usuarioMock);
	}

	@Test
	public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
		// preparacion
		doThrow(UsuarioExistente.class).when(servicioLoginMock).registrar(usuarioMock);

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123");

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El usuario ya existe"));
	}

	@Test
	public void errorEnRegistrarmeDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
		// preparacion
		doThrow(RuntimeException.class).when(servicioLoginMock).registrar(usuarioMock);


		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123");

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al registrar el nuevo usuario"));
	}

	@Test
	public void siNoSeIngresaMailElRegistroFalla(){
		when(usuarioMock.getEmail()).thenReturn("");

		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123");

		thenRegistroFalla(modelAndView, "errorEmail", "El campo email es obligatorio");
	}

	@Test
	public void siNoSeIngresaApellidoElRegistroFalla(){
		when(usuarioMock.getApellido()).thenReturn("");

		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123");

		thenRegistroFalla(modelAndView, "errorApellido", "El campo apellido es obligatorio");
	}

	@Test
	public void siNoSeIngresaNombreElRegistroFalla(){
		when(usuarioMock.getNombre()).thenReturn("");

		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123");

		thenRegistroFalla(modelAndView, "errorNombre", "El campo nombre es obligatorio");
	}

	@Test
	public void siNoSeIngresaUnaDeLasPasswordElRegistroFalla(){

		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "");

		thenRegistroFalla(modelAndView, "errorPassword", "El campo contraseña es obligatorio");
	}

	@Test
	public void siLasPasswordsSonDistintasElRegistroFalla(){
		when(usuarioMock.getPassword()).thenReturn("123");

		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "abc");

		thenRegistroFalla(modelAndView, "errorPasswordsDistintas", "Las contraseñas deben ser iguales");
	}

	@Test
	public void siNoSeIngresaUnRolElRegistroFalla(){
		when(usuarioMock.getRol()).thenReturn("");

		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123");

		thenRegistroFalla(modelAndView, "errorRol", "Debe ingresar un rol");
	}

	private void thenRegistroFalla(ModelAndView mav, String errorKey, String mensajeError) {
		assertThat(mav.getViewName(), equalToIgnoringCase("nuevo-usuario"));

		assertThat(mav.getModel().get(errorKey).toString(), equalToIgnoringCase(mensajeError));
	}

}

