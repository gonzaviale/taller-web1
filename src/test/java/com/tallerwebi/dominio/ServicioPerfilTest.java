package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioPerfil;
import com.tallerwebi.dominio.servicio.ServicioPerfilImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ServicioPerfilTest {

    private RepositorioUsuario repositorioUsuario;

    private ServicioPerfil servicioPerfil;

    @BeforeEach
    public void setUp() {
        repositorioUsuario=mock (RepositorioUsuario.class);
        servicioPerfil = new ServicioPerfilImpl(repositorioUsuario);
    }


    @Test
    public void cuandoBuscoUnUsuarioConIdExistenteMeDevuelveAlUsuario() {

        Long id = 1L;
        Usuario usuarioEsperado = new Usuario();
        usuarioEsperado.setId(id);
        usuarioEsperado.setNombre("Juan");

        when(repositorioUsuario.buscarPorId(id)).thenReturn(usuarioEsperado);

        Usuario usuarioEncontrado = servicioPerfil.buscarUsuarioPorId(id);

        assertThat(usuarioEncontrado, notNullValue());
        assertThat(usuarioEncontrado.getId(), equalTo(id));
        assertThat(usuarioEncontrado.getNombre(), equalTo ("Juan"));
        verify(repositorioUsuario, times(1)).buscarPorId(id);
    }

    @Test
    void queCuandoQuieraActualizarElUsuarioLlameAlRepo() {
        // Datos de prueba
        Usuario usuarioEnSesion = new Usuario();
        usuarioEnSesion.setId(1L);
        usuarioEnSesion.setNombre("John Doe");

        // Ejecutar el método a probar
        servicioPerfil.actualizarUsuario(usuarioEnSesion);

        // Verificar que el método del repositorio fue llamado con el usuario correcto
        verify(repositorioUsuario, times(1)).actualizarUsuario(usuarioEnSesion);
    }

    @Test
    void obtenerMascotasDelUsuarioDeberaOrdenarPorRechazadoEnRevisionYAprobado() {
        // Datos de prueba
        Mascota mascota1 = new Mascota();
        mascota1.setRechazado(true);
        mascota1.setRevision(false);
        mascota1.setAprobado(false);

        Mascota mascota2 = new Mascota();
        mascota2.setRechazado(false);
        mascota2.setRevision(true);
        mascota2.setAprobado(false);

        Mascota mascota3 = new Mascota();
        mascota3.setRechazado(false);
        mascota3.setRevision(false);
        mascota3.setAprobado(true);

        List<Mascota> listaMascotas = Arrays.asList(mascota1, mascota2, mascota3);

        when(repositorioUsuario.obtenerMascotaDelUsuario(1L)).thenReturn(listaMascotas);

        List<Mascota> resultado = servicioPerfil.obtenerMascotasDelUsuario(1L);

        assertThat(resultado.get(0),is(mascota3)); // Aprobado primero
        assertThat(resultado.get(1),is(mascota2)); // En revisión después
        assertThat(resultado.get(2),is(mascota1)); // Rechazado al final
        verify(repositorioUsuario, times(1)).obtenerMascotaDelUsuario(1L);
    }

    @Test
    void obtenerMascotasDelUsuarioQueNoTieneMascotasMRetornarListaVaciaCuandoNoHayMascotas() {

        when(repositorioUsuario.obtenerMascotaDelUsuario(1L)).thenReturn(Arrays.asList());

        List<Mascota> resultado = servicioPerfil.obtenerMascotasDelUsuario(1L);

        assertThat(resultado, emptyCollectionOf(Mascota.class));
        verify(repositorioUsuario, times(1)).obtenerMascotaDelUsuario(1L);
    }

    @Test
    void obtenerPublicacionesDelUsuario_deberiaOrdenarPorEstaActivaYFecha() {
        // Datos de prueba
        Long idUsuario = 1L;

        Publicacion publicacion1 = new Publicacion();
        publicacion1.setEstaActiva(true);
        publicacion1.setLocalDateTime(LocalDateTime.of(2023, 10, 10, 10, 0));

        Publicacion publicacion2 = new Publicacion();
        publicacion2.setEstaActiva(false);
        publicacion2.setLocalDateTime(LocalDateTime.of(2023, 10, 9, 9, 0));

        Publicacion publicacion3 = new Publicacion();
        publicacion3.setEstaActiva(true);
        publicacion3.setLocalDateTime(LocalDateTime.of(2023, 10, 8, 8, 0));

        // Simular que el repositorio devuelve estas publicaciones
        when(repositorioUsuario.obtenerPublicacionesDelUsuario(idUsuario)).thenReturn(Arrays.asList(publicacion1, publicacion2, publicacion3));

        // Ejecutar el método a probar
        List<Publicacion> publicaciones = servicioPerfil.obtenerPublicacionesDelUsuario(idUsuario);

        // Verificar el orden esperado:

        assertThat(publicaciones,hasItem(publicacion1));
        assertThat(publicaciones,hasItem(publicacion2));
        assertThat(publicaciones,hasItem(publicacion3));

        // Primero las publicaciones activas, luego por fecha
        assertThat(publicaciones.get(0),is(publicacion1));
        assertThat(publicaciones.get(1),is(publicacion3));
        assertThat(publicaciones.get(2),is(publicacion2));

        // Verificar que el método del repositorio fue llamado con el ID correcto
        verify(repositorioUsuario, times(1)).obtenerPublicacionesDelUsuario(idUsuario);
    }

    @Test
    void obtenerPublicacionesDelUsuario_deberiaRetornarListaVaciaCuandoNoHayPublicaciones() {
        when(repositorioUsuario.obtenerPublicacionesDelUsuario(1L)).thenReturn(Arrays.asList());

        List<Publicacion> resultado = servicioPerfil.obtenerPublicacionesDelUsuario(1L);

        assertThat(resultado, emptyCollectionOf(Publicacion.class));

        verify(repositorioUsuario, times(1)).obtenerPublicacionesDelUsuario(1L);
    }


    @Test
    public void cuandoBuscoUnUsuarioPorUnIdQueNoExisteElRepositorioMeEntregaUnUsuarioNulo() {

        Long id = 1L;

        when(repositorioUsuario.buscarPorId(id)).thenReturn(null);

        Usuario usuarioEncontrado = servicioPerfil.buscarUsuarioPorId(id);

        assertThat(usuarioEncontrado,nullValue());

        verify(repositorioUsuario, times(1)).buscarPorId(id);
    }

}
