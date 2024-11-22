package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Felino;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioImagenes;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ControladorAgregarMascotaTest {

    ServicioMascota servicioMascotaMock = mock(ServicioMascota.class);
    ServicioImagenes servicioImagenesMock = mock(ServicioImagenes.class);
    ControladorAgregarMascota controlador = new ControladorAgregarMascota(servicioMascotaMock, servicioImagenesMock,null);
    Mascota mascotaMock = mock(Felino.class);
    Usuario usuarioMock = mock(Usuario.class);
    private HttpServletRequest requestMock;
    private HttpSession sessionMock;
    MultipartFile[] imagenesMock;
    private Mascota mascotaBuscadaMock;

    @BeforeEach
    public void init() throws IOException {
        when(mascotaMock.getNombre()).thenReturn("Manchas");
        when(mascotaMock.getAnios()).thenReturn(5);
        when(mascotaMock.getPeso()).thenReturn(6f);
        when(mascotaMock.getTipo()).thenReturn("Felino");
        when(mascotaMock.getId()).thenReturn(1L);  // Agregar ID de la mascota

        requestMock = mock(HttpServletRequest.class);
        sessionMock = mock(HttpSession.class);

        // Mock para devolver la sesión cuando se llame a request.getSession()
        when(requestMock.getSession()).thenReturn(sessionMock);
        // Mock para devolver el usuario cuando se llame a session.getAttribute("usuarioEnSesion")
        when(sessionMock.getAttribute("usuarioEnSesion")).thenReturn(usuarioMock);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1L); // El usuario tiene id 1

        MultipartFile imagen1 = mock(MultipartFile.class);
        MultipartFile imagen2 = mock(MultipartFile.class);

        // Configura el comportamiento del mock
        when(imagen1.getOriginalFilename()).thenReturn("imagen1.jpg");
        when(imagen1.isEmpty()).thenReturn(false);
        when(imagen1.getBytes()).thenReturn(new byte[]{1, 2, 3}); // Simulación de bytes de la imagen

        when(imagen2.getOriginalFilename()).thenReturn("imagen2.png");
        when(imagen2.isEmpty()).thenReturn(false);
        when(imagen2.getBytes()).thenReturn(new byte[]{4, 5, 6}); // Simulación de bytes de la imagen

        mascotaBuscadaMock = mock(Mascota.class);

        imagenesMock = new MultipartFile[]{imagen1, imagen2};

        // Configura el dueño de la mascota
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);
        when(usuarioMock.getId()).thenReturn(1L);  // El dueño tiene el mismo ID

        // Mock para simular la mascota a editar
        when(servicioMascotaMock.buscarMascotaPorId(1L)).thenReturn(mascotaMock);
        when(servicioMascotaMock.obtenerSangreSegunTipoDeMascota("Felino")).thenReturn(Arrays.asList("O+", "A+"));
    }/*
    @Test
    public void queUnDuenoPuedaAgregarUnaMascotaDonante() {
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), mascotaMock.getPeso(), mascotaMock.getTipo(), "No", imagenesMock, requestMock);

        thenRegistroExitoso(mav, "redirect:/home");
        assertThat(mascotaMock.getDuenio().getId(), equalTo(usuarioMock.getId()));
    }

    @Test
    public void queUnDuenoNoPuedaAgregarUnaMascotaDonanteSiFueTransfundida() {
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), mascotaMock.getPeso(), mascotaMock.getTipo(), "Si", imagenesMock, requestMock);

        thenRegistroFalla(mav, "redirect:/home", "mensaje", "no puede registrar a su mascota si ya recibio una transfusion");
    }

    @Rollback
    @Test
    public void queUnDuenoPuedaAgregarUnaMascotaReceptora() {
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarReceptora(mascotaMock.getNombre(), mascotaMock.getAnios(), mascotaMock.getPeso(), mascotaMock.getTipo(), imagenesMock, requestMock);

        thenRegistroExitoso(mav, "redirect:/home");
        assertThat(mascotaMock.getDuenio().getId(), equalTo(usuarioMock.getId()));
    }

    @Test
    public void queNoSePuedaRegistrarMascotaSiNoSeEnvianLasImagenesDeLosEstudios(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);
        MultipartFile[] imagenesVacias = new MultipartFile[0];

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), mascotaMock.getPeso(), mascotaMock.getTipo(), "No", imagenesVacias, requestMock);

        thenRegistroFalla(mav, "agregar-mascota", "errorImagenes", "Una mascota no se puede registrar sin imágenes de sus estudios");
    }

    @Test
    public void queNoSePuedaRegistrarMascotaSiElNombreEstaVacio(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante("", mascotaMock.getAnios(), mascotaMock.getPeso(), mascotaMock.getTipo(), "No", imagenesMock, requestMock);

        thenRegistroFalla(mav, "agregar-mascota", "errorNombre", "El nombre de la mascota es obligatorio");
    }

    @Test
    public void queNoSePuedaRegistrarMascotaSiLaEdadEstaVacia(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), 0, mascotaMock.getPeso(), mascotaMock.getTipo(), "No", imagenesMock, requestMock);

        thenRegistroFalla(mav, "agregar-mascota", "errorEdad", "La edad de la mascota es obligatoria");
    }

    @Test
    public void queNoSePuedaRegistrarMascotaSiElPesoEstaVacio(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), 0f, mascotaMock.getTipo(), "No", imagenesMock, requestMock);

        thenRegistroFalla(mav, "agregar-mascota", "errorPeso", "El peso de la mascota es obligatorio");
    }

    @Test
    public void queNoSePuedaRegistrarMascotaSinoSeIndicaSuTipo(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarReceptora(mascotaMock.getNombre(), mascotaMock.getAnios(), mascotaMock.getPeso(), "", imagenesMock, requestMock);

        thenRegistroFalla(mav, "agregar-mascota", "errorTipo", "Es obligatorio ingresar el tipo de mascota");
    }

    @Test
    public void queNoSePuedaRegistrarMascotaDonanteSiNoSeIngresaSiRecibioONoTransfusion(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), mascotaMock.getPeso(), mascotaMock.getTipo(), "", imagenesMock, requestMock);

        thenRegistroFalla(mav, "agregar-mascota", "errorTransfusion", "Es obligatorio ingresar si el animal recibió o no una transfusión");
    }

    @Test
    public void queNoSePuedaRegistrarMascotaDonanteCaninaSiNoCumpleConElPeso(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), 20f, "Canino", "No", imagenesMock, requestMock);

        thenRegistroFalla(mav, "redirect:/home", "mensaje", "Para que un perro sea donante debe pesar mas de 25 kilos y tener entre 1 y 8 anios");
    }

    @Test
    public void queNoSePuedaRegistrarMascotaDonanteCaninaSiNoCumpleConLaEdad(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), 10, 25f, "Canino", "No", imagenesMock, requestMock);

        thenRegistroFalla(mav, "redirect:/home", "mensaje", "Para que un perro sea donante debe pesar mas de 25 kilos y tener entre 1 y 8 anios");
    }

    @Test
    public void queNoSePuedaRegistrarMascotaDonanteFelinaSiNoCumpleConElPeso(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), mascotaMock.getAnios(), 2.5f, "Felino", "No", imagenesMock, requestMock);

        thenRegistroFalla(mav, "redirect:/home", "mensaje", "Para que un gato sea donante debe pesar mas de 3,5 kilos y tener entre 1 y 8 anios");
    }

    @Test
    public void queNoSePuedaRegistrarMascotaDonanteFelinaSiNoCumpleConLaEdad(){
        when(usuarioMock.getId()).thenReturn(1L);
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView mav = agregarMascota.agregarDonante(mascotaMock.getNombre(), 15, 3.5f, "Felino", "No", imagenesMock, requestMock);

        thenRegistroFalla(mav, "redirect:/home", "mensaje", "Para que un gato sea donante debe pesar mas de 3,5 kilos y tener entre 1 y 8 anios");
    }

    private void thenRegistroExitoso(ModelAndView mav, String vista) {
        assertThat(mav.getViewName(), equalToIgnoringCase(vista));
    }

    private void thenRegistroFalla(ModelAndView mav, String vista, String errorKey, String error) {
        assertThat(mav.getViewName(), equalToIgnoringCase(vista));
        assertThat(mav.getModel().get(errorKey).toString(), equalToIgnoringCase(error));
    }*/

    @Test
    void testEliminarMascota_UsuarioNoAutenticado() {
        when(sessionMock.getAttribute("usuarioEnSesion")).thenReturn(null);

        ModelAndView resultado = controlador.eliminarMascota(requestMock, 1L);

        assertEquals("redirect:/home", resultado.getViewName(), "Debe redirigir al home si el usuario no está autenticado.");
        assertEquals("accion invalida o no esta logeado", resultado.getModel().get("mensaje"));
    }

    @Test
    void testEliminarMascota_MascotaNoPerteneceAlUsuario() {
        when(sessionMock.getAttribute("usuarioEnSesion")).thenReturn(true);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(2L); // Usuario en sesión
        when(servicioMascotaMock.buscarMascotaPorId(1L)).thenReturn(mascotaMock);
        when(usuarioMock.getId()).thenReturn(1L); // Dueño de la mascota es otro usuario

        ModelAndView resultado = controlador.eliminarMascota(requestMock, 1L);

        assertEquals("redirect:/home", resultado.getViewName(), "Debe redirigir al home si la mascota no pertenece al usuario.");
        assertEquals("accion invalida no se puede eliminar pertenece a una solicitud o publicacion", resultado.getModel().get("mensaje"));
    }

    @Test
    void testEliminarMascota_EliminacionExitosa() {
        when(sessionMock.getAttribute("usuarioEnSesion")).thenReturn(true);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1L); // Usuario en sesión
        when(servicioMascotaMock.buscarMascotaPorId(1L)).thenReturn(mascotaMock);
        when(usuarioMock.getId()).thenReturn(1L); // Dueño de la mascota es el usuario en sesión
        when(servicioMascotaMock.eliminarMascota(mascotaMock)).thenReturn(true);

        ModelAndView resultado = controlador.eliminarMascota(requestMock, 1L);

        assertEquals("redirect:/home", resultado.getViewName(), "Debe redirigir al home tras eliminar correctamente.");
        assertEquals("se elimino correctamente su mascota", resultado.getModel().get("mensaje"));
    }

    @Test
    void testEliminarMascota_FalloEnEliminacion() {
        when(sessionMock.getAttribute("usuarioEnSesion")).thenReturn(true);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1L); // Usuario en sesión
        when(servicioMascotaMock.buscarMascotaPorId(1L)).thenReturn(mascotaMock);
        when(usuarioMock.getId()).thenReturn(1L); // Dueño de la mascota es el usuario en sesión
        when(servicioMascotaMock.eliminarMascota(mascotaMock)).thenReturn(false);

        ModelAndView resultado = controlador.eliminarMascota(requestMock, 1L);

        assertEquals("redirect:/home", resultado.getViewName(), "Debe redirigir al home si la eliminación falla.");
        assertEquals("accion invalida no se puede eliminar pertenece a una solicitud o publicacion", resultado.getModel().get("mensaje"));
    }

    @Test
    void testEliminarMascota_MascotaNoEncontrada() {
        when(sessionMock.getAttribute("usuarioEnSesion")).thenReturn(true);
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1L); // Usuario en sesión
        when(servicioMascotaMock.buscarMascotaPorId(1L)).thenReturn(null); // Mascota no encontrada

        ModelAndView resultado = controlador.eliminarMascota(requestMock, 1L);

        assertEquals("redirect:/home", resultado.getViewName(), "Debe redirigir al home si la mascota no existe.");
        assertEquals("accion invalida no se puede eliminar pertenece a una solicitud o publicacion", resultado.getModel().get("mensaje"));
    }

    @Test
    void testEditarPublicacion_UsuarioNoAutenticado() {
        when(sessionMock.getAttribute("usuarioEnSesion")).thenReturn(null);

        ModelAndView resultado = controlador.editarPublicacion(requestMock, mascotaMock, imagenesMock);

        assertEquals("redirect:/home", resultado.getViewName(), "Debe redirigir al home si el usuario no está autenticado.");
        assertEquals("accion invalida o no esta logeado o no es su publicacion", resultado.getModel().get("mensaje"));
    }

    @Test
    void testEditarPublicacion_MascotaNoPerteneceAlUsuario() {
        when(servicioMascotaMock.buscarMascotaPorId(anyLong())).thenReturn(mascotaBuscadaMock);
        when(mascotaBuscadaMock.getDuenio()).thenReturn(usuarioMock);
        when(usuarioMock.getId()).thenReturn(2L); // Dueño de la mascota es otro usuario

        ModelAndView resultado = controlador.editarPublicacion(requestMock, mascotaMock, imagenesMock);

        assertEquals("redirect:/home", resultado.getViewName(), "Debe redirigir al home si la mascota no pertenece al usuario.");
        assertEquals("no es dueño de la mascota o no se pudo editar", resultado.getModel().get("mensaje"));
    }

    @Test
    void testEditarPublicacion_RestriccionEdadDonante() {
        when(servicioMascotaMock.buscarMascotaPorId(anyLong())).thenReturn(mascotaBuscadaMock);
        when(mascotaMock.isDonante()).thenReturn(true);
        when(servicioMascotaMock.isEdadApropiadaDonante(mascotaMock)).thenReturn(false);

        ModelAndView resultado = controlador.editarPublicacion(requestMock, mascotaMock, imagenesMock);

        assertEquals("redirect:/home", resultado.getViewName(), "Debe redirigir al home si la edad no es adecuada.");
        assertEquals("La mascota debe tener entre 1 y 8 anios.", resultado.getModel().get("mensaje"));
    }

    @Test
    void testActualizarMascota_NoSesion() {
        // Simula que no hay sesión activa
        when(sessionMock.getAttribute("usuarioEnSesion")).thenReturn(null);

        ModelAndView resultado = controlador.actualizarMascota(requestMock, 1L);

        assertEquals("redirect:/home", resultado.getViewName(), "Debe redirigir al home si no hay sesión activa.");
        assertEquals("accion invalida o no esta logeado", resultado.getModel().get("mensaje"));
    }

    @Test
    void testActualizarMascota_NoEsDuenio() {
        // Simula que el usuario en sesión no es el dueño de la mascota
        when(sessionMock.getAttribute("usuarioId")).thenReturn(2L); // Usuario en sesión con id diferente
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock); // El dueño tiene id 1

        ModelAndView resultado = controlador.actualizarMascota(requestMock, 1L);

        assertEquals("redirect:/home", resultado.getViewName(), "Debe redirigir al home si el usuario no es dueño.");
        assertEquals("no se pudo encontrar la mascota que quiere editar", resultado.getModel().get("mensaje"));
    }

    @Test
    void testActualizarMascota_CorrectaCargaDeEdicion() {
        // Simula que el usuario en sesión es el dueño
        when(sessionMock.getAttribute("usuarioId")).thenReturn(1L); // El usuario en sesión es el dueño
        when(mascotaMock.getDuenio()).thenReturn(usuarioMock);

        ModelAndView resultado = controlador.actualizarMascota(requestMock, 1L);

        assertEquals("editar-mascota", resultado.getViewName(), "Debe redirigir a la página de edición de la mascota.");
        assertEquals(mascotaMock, resultado.getModel().get("mascota"));
        assertTrue(((List<?>) resultado.getModel().get("sangres")).contains("O+"));
    }


}