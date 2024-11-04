package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Veterinario;
import com.tallerwebi.dominio.servicio.ServicioImagenes;
import com.tallerwebi.dominio.servicio.ServicioLogin;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsMapContaining.hasKey;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.*;

public class ControladorLoginTest {

	private ControladorLogin controladorLogin;
	private Usuario usuarioMock;
	private DatosLogin datosLoginMock;
	private HttpServletRequest requestMock;
	private HttpSession sessionMock;
	private ServicioLogin servicioLoginMock;
	private ServicioImagenes servicioImagenes;


	@BeforeEach
	public void init(){
		datosLoginMock = new DatosLogin("dami@unlam.com", "123");
		usuarioMock = mock(Usuario.class);
		servicioImagenes= mock(ServicioImagenes.class);
		when(usuarioMock.getEmail()).thenReturn("dami@unlam.com");
		when(usuarioMock.getPassword()).thenReturn("123");
		when(usuarioMock.getApellido()).thenReturn("Alarcón");
		when(usuarioMock.getNombre()).thenReturn("Lucía");
		when(usuarioMock.getRol()).thenReturn("a");
		when(usuarioMock.getEstado()).thenReturn("activo");
		requestMock = mock(HttpServletRequest.class);
		sessionMock = mock(HttpSession.class);
		servicioLoginMock = mock(ServicioLogin.class);
		controladorLogin = new ControladorLogin(servicioLoginMock,servicioImagenes);
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
		when(usuarioEncontradoMock.getEstado()).thenReturn("activo");

		when(requestMock.getSession()).thenReturn(sessionMock);
		when(servicioLoginMock.consultarUsuario(anyString(), anyString())).thenReturn(usuarioEncontradoMock);
		
		// ejecucion
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);
		
		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/home"));
		verify(sessionMock, times(1)).setAttribute("ROL", usuarioEncontradoMock.getRol());
	}

	@Test
	public void loginConBancoYPasswordCorrectosDeberiaLlevarABancoHome() {
		// preparación
		Banco bancoEncontradoMock = mock(Banco.class);
		when(requestMock.getSession()).thenReturn(sessionMock);

		when(requestMock.getSession()).thenReturn(sessionMock);
		when(servicioLoginMock.ConsultarBanco(anyString(), anyString())).thenReturn(bancoEncontradoMock);

		// ejecución
		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		// validación
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/bancohome"));
		verify(sessionMock, times(1)).setAttribute(eq("ROL"), eq("banco"));
	}

	@Test
	public void registrameSiUsuarioNoExisteDeberiaCrearUsuarioYVolverAlLogin() throws UsuarioExistente {

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123","",null);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("redirect:/login"));
		verify(servicioLoginMock, times(1)).registrar(any());
	}

	@Test
	public void registrarmeSiUsuarioExisteDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
		// preparacion
		doThrow(UsuarioExistente.class).when(servicioLoginMock).registrar(any());

		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123","",null);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("El usuario ya existe"));
	}

	@Test
	public void errorEnRegistrarmeDeberiaVolverAFormularioYMostrarError() throws UsuarioExistente {
		// preparacion
		doThrow(RuntimeException.class).when(servicioLoginMock).registrar(any());


		// ejecucion
		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123","",null);

		// validacion
		assertThat(modelAndView.getViewName(), equalToIgnoringCase("nuevo-usuario"));
		assertThat(modelAndView.getModel().get("error").toString(), equalToIgnoringCase("Error al registrar el nuevo usuario"));
	}

	@Test
	public void siNoSeIngresaMailElRegistroFalla(){
		when(usuarioMock.getEmail()).thenReturn("");

		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123","",null);

		thenRegistroFalla(modelAndView, "errorEmail", "El campo email es obligatorio");
	}

	@Test
	public void siNoSeIngresaApellidoElRegistroFalla(){
		when(usuarioMock.getApellido()).thenReturn("");

		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123","",null);

		thenRegistroFalla(modelAndView, "errorApellido", "El campo apellido es obligatorio");
	}

	@Test
	public void siNoSeIngresaNombreElRegistroFalla(){
		when(usuarioMock.getNombre()).thenReturn("");

		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123","",null);

		thenRegistroFalla(modelAndView, "errorNombre", "El campo nombre es obligatorio");
	}

	@Test
	public void siNoSeIngresaUnaDeLasPasswordElRegistroFalla(){

		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "","",null);

		thenRegistroFalla(modelAndView, "errorPassword", "El campo contraseña es obligatorio");
	}

	@Test
	public void siLasPasswordsSonDistintasElRegistroFalla(){
		when(usuarioMock.getPassword()).thenReturn("123");

		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "abc","",null);

		thenRegistroFalla(modelAndView, "errorPasswordsDistintas", "Las contraseñas deben ser iguales");
	}

	@Test
	public void siNoSeIngresaUnRolElRegistroFalla(){
		when(usuarioMock.getRol()).thenReturn("");

		ModelAndView modelAndView = controladorLogin.registrarme(usuarioMock, "123","",null);

		thenRegistroFalla(modelAndView, "errorRol", "Debe ingresar un rol");
	}

	private void thenRegistroFalla(ModelAndView mav, String errorKey, String mensajeError) {
		assertThat(mav.getViewName(), equalToIgnoringCase("nuevo-usuario"));

		assertThat(mav.getModel().get(errorKey).toString(), equalToIgnoringCase(mensajeError));
	}

	@Test
	public void intentoLogearmeConUnUsuarioVeterinarioPeroEstaPendientePorLoCualMeRedirigueAlLogin(){
		when(usuarioMock.getEmail()).thenReturn("veterinario@correo.com");
		when(usuarioMock.getPassword()).thenReturn("password123");
		when(usuarioMock.getApellido()).thenReturn("Pérez");
		when(usuarioMock.getNombre()).thenReturn("Juan");
		when(usuarioMock.getRol()).thenReturn("veterinario");
		when(usuarioMock.getEstado()).thenReturn("pendiente");

		when(requestMock.getSession()).thenReturn(sessionMock);

		when(servicioLoginMock.consultarUsuario("veterinario@correo.com", "password123")).thenReturn(usuarioMock);

		datosLoginMock = new DatosLogin("veterinario@correo.com", "password123");

		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		assertThat(modelAndView.getViewName(), is("login"));

		assertThat(modelAndView.getModel(), hasKey("error"));
		assertThat(modelAndView.getModel().get("error"), is("Usuario no verificado"));
	}

	@Test
	public void meLogeoComoAdministradorMeRedirigueAVistaAdministrador(){
		Usuario usuario= new Usuario();

		usuario.setEstado("activo");
		usuario.setRol("administrador");
		usuario.setPassword("1234");
		usuario.setEmail("admiistrador@gmail.com");

		when(requestMock.getSession()).thenReturn(sessionMock);

		datosLoginMock = new DatosLogin("administrador@gmail.com", "1234");

		when(servicioLoginMock.consultarUsuario("administrador@gmail.com", "1234")).thenReturn(usuario);

		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		assertThat(modelAndView.getViewName(), is("redirect:/administrador"));
	}

	@Test
	public void meIntentoLogearComoVeterinarioEnEstadoInactivoYTengoUnaClaveError(){
		Usuario usuario= new Veterinario();

		usuario.setEstado("inactivo");
		usuario.setRol("veterinario");
		usuario.setPassword("1234");
		usuario.setEmail("veterinario@gmail.com");

		when(requestMock.getSession()).thenReturn(sessionMock);

		datosLoginMock = new DatosLogin("veterinario@gmail.com", "1234");

		when(servicioLoginMock.consultarUsuario("veterinario@gmail.com", "1234")).thenReturn(usuario);

		ModelAndView modelAndView = controladorLogin.validarLogin(datosLoginMock, requestMock);

		assertThat(modelAndView.getViewName(), is("login"));
		assertThat(modelAndView.getModel(), hasKey("error"));
		assertThat(modelAndView.getModel().get("error"), is("Usuario no verificado"));

	}


}
