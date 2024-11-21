package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMascotaImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioMascotaTest {

    private RepositorioMascota repositorioMascotaMock;
    private ServicioMascotaImpl servicioMascota;

    @BeforeEach
    public void setUp() {
        // Crear manualmente el mock y el servicio
        repositorioMascotaMock = mock(RepositorioMascota.class);
        servicioMascota = new ServicioMascotaImpl(repositorioMascotaMock);
    }

    @Test
    public void queSePuedaTraerUnaMascotaPorSuDueno() {
        // Crear mocks
        Usuario dueno = new Usuario();
        dueno.setNombre("Lucia");

        Mascota mascota = new Mascota();
        mascota.setNombre("Firulais");
        mascota.setDuenio(dueno);

        List<Mascota> mascotasMock = List.of(mascota);

        // Configurar comportamiento del mock
        when(repositorioMascotaMock.obtenerMascotasPorDueno(dueno)).thenReturn(mascotasMock);

        // Actuar
        List<Mascota> mascotasRecuperadas = servicioMascota.obtenerMascotasPorDueno(dueno);

        // Verificar
        assertThat(mascotasRecuperadas.size(), is(1));
        assertThat(mascotasRecuperadas.get(0).getNombre(), is("Firulais"));
    }

    @Test
    public void queSePuedanObtenerLasMascotasEnRevision() {
        // Crear mocks
        Usuario dueno = new Usuario();
        dueno.setNombre("Lucia");

        Mascota mascota = new Mascota();
        mascota.setNombre("Firulais");
        mascota.setDuenio(dueno);

        List<Mascota> mascotasMock = List.of(mascota);

        // Configurar comportamiento del mock
        when(repositorioMascotaMock.buscarMascotaEnRevision()).thenReturn(mascotasMock);

        // Actuar
        List<Mascota> mascotasRecuperadas = servicioMascota.obtenerMascotasEnRevision();

        // Verificar
        assertThat(mascotasRecuperadas.size(), is(1));
        assertThat(mascotasRecuperadas.get(0).getNombre(), is("Firulais"));
    }



}
