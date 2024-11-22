package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMascotaImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServicioMascotaTest {

    private RepositorioMascota repositorioMascotaMock;
    private ServicioMascotaImpl servicioMascota;

    @BeforeEach
    public void setUp() {
        repositorioMascotaMock = mock(RepositorioMascota.class);
        servicioMascota = new ServicioMascotaImpl(repositorioMascotaMock);
    }

    @Test
    public void queSePuedaTraerUnaMascotaPorSuDueno() {
        Usuario dueno = new Usuario();
        dueno.setNombre("Lucia");

        Mascota mascota = new Mascota();
        mascota.setNombre("Firulais");
        mascota.setDuenio(dueno);

        List<Mascota> mascotasMock = List.of(mascota);

        when(repositorioMascotaMock.obtenerMascotasPorDueno(dueno)).thenReturn(mascotasMock);

        List<Mascota> mascotasRecuperadas = servicioMascota.obtenerMascotasPorDueno(dueno);

        assertThat(mascotasRecuperadas.size(), is(1));
        assertThat(mascotasRecuperadas.get(0).getNombre(), is("Firulais"));
    }

    @Test
    public void queSePuedanObtenerLasMascotasEnRevision() {
        Usuario dueno = new Usuario();
        dueno.setNombre("Lucia");

        Mascota mascota = new Mascota();
        mascota.setNombre("Firulais");
        mascota.setDuenio(dueno);

        List<Mascota> mascotasMock = List.of(mascota);

        when(repositorioMascotaMock.buscarMascotaEnRevision()).thenReturn(mascotasMock);

        List<Mascota> mascotasRecuperadas = servicioMascota.obtenerMascotasEnRevision();

        assertThat(mascotasRecuperadas.size(), is(1));
        assertThat(mascotasRecuperadas.get(0).getNombre(), is("Firulais"));
    }

    @Test
    void testPesoCorrectoFelino_ValidFelinoConPesoCorrecto() {
        Mascota felino = crearMascota("Gato", 4.0f, 3, true); // Peso >= 3.5
        assertTrue(servicioMascota.isPesoCorrectoFelino(felino), "El peso debería ser considerado correcto.");
    }

    @Test
    void testPesoCorrectoFelino_ValidFelinoConPesoBajo() {
        Mascota felino = crearMascota("Gato", 3.0f, 3, true); // Peso < 3.5
        assertFalse(servicioMascota.isPesoCorrectoFelino(felino), "El peso debería ser considerado incorrecto.");
    }

    @Test
    void testPesoCorrectoFelino_MascotaNoDonante() {
        Mascota felinoNoDonante = crearMascota("Gato", 4.0f, 2, false); // No donante
        assertFalse(servicioMascota.isPesoCorrectoFelino(felinoNoDonante), "El peso no debería ser válido si no es donante.");
    }

    @Test
    void testPesoCorrectoFelino_MascotaInvalida() {
        Mascota mascotaInvalida = new Mascota(); // Mascota con datos nulos
        assertFalse(servicioMascota.isPesoCorrectoFelino(mascotaInvalida), "No debería validar mascotas inválidas.");
    }

    @Test
    void testPesoCorrectoFelino_NullMascota() {
        assertFalse(servicioMascota.isPesoCorrectoFelino(null), "No debería validar una mascota nula.");
    }

    private Mascota crearMascota(String tipo, Float peso, Integer anios, boolean isDonante) {
        Mascota mascota = new Mascota();
        mascota.setTipo(tipo);
        mascota.setPeso(peso);
        mascota.setAnios(anios);
        mascota.setDonante(isDonante);
        return mascota;
    }

    @Test
    void testPesoCorrectoCanino_ValidCaninoConPesoCorrecto() {
        Mascota canino = crearMascota("Perro", 30.0f, 5, true); // Peso >= 25
        assertTrue(servicioMascota.isPesoCorrectoCanino(canino), "El peso debería ser considerado correcto.");
    }

    @Test
    void testPesoCorrectoCanino_ValidCaninoConPesoBajo() {
        Mascota canino = crearMascota("Perro", 20.0f, 3, true); // Peso < 25
        assertFalse(servicioMascota.isPesoCorrectoCanino(canino), "El peso debería ser considerado incorrecto.");
    }

    @Test
    void testPesoCorrectoCanino_MascotaNoDonante() {
        Mascota caninoNoDonante = crearMascota("Perro", 28.0f, 6, false); // No donante
        assertFalse(servicioMascota.isPesoCorrectoCanino(caninoNoDonante), "El peso no debería ser válido si no es donante.");
    }

    @Test
    void testPesoCorrectoCanino_MascotaInvalida() {
        Mascota mascotaInvalida = new Mascota(); // Mascota con datos nulos
        assertFalse(servicioMascota.isPesoCorrectoCanino(mascotaInvalida), "No debería validar mascotas inválidas.");
    }

    @Test
    void testPesoCorrectoCanino_NullMascota() {
        assertFalse(servicioMascota.isPesoCorrectoCanino(null), "No debería validar una mascota nula.");
    }

    @Test
    void testEdadApropiadaDonante_EdadValidaEnRango() {
        Mascota mascota = crearMascota("Perro", 30.0f, 5, true); // Edad dentro del rango [1, 8]
        assertTrue(servicioMascota.isEdadApropiadaDonante(mascota), "La edad debería ser considerada válida.");
    }

    @Test
    void testEdadApropiadaDonante_EdadIgualAlLimiteInferior() {
        Mascota mascota = crearMascota("Perro", 30.0f, 1, true); // Edad igual al límite inferior
        assertTrue(servicioMascota.isEdadApropiadaDonante(mascota), "La edad debería ser válida en el límite inferior.");
    }

    @Test
    void testEdadApropiadaDonante_EdadIgualAlLimiteSuperior() {
        Mascota mascota = crearMascota("Perro", 30.0f, 8, true); // Edad igual al límite superior
        assertTrue(servicioMascota.isEdadApropiadaDonante(mascota), "La edad debería ser válida en el límite superior.");
    }

    @Test
    void testEdadApropiadaDonante_EdadMenorAlLimite() {
        Mascota mascota = crearMascota("Perro", 30.0f, 0, true); // Edad menor al límite inferior
        assertFalse(servicioMascota.isEdadApropiadaDonante(mascota), "La edad debería ser inválida por ser demasiado joven.");
    }

    @Test
    void testEdadApropiadaDonante_EdadMayorAlLimite() {
        Mascota mascota = crearMascota("Perro", 30.0f, 10, true); // Edad mayor al límite superior
        assertFalse(servicioMascota.isEdadApropiadaDonante(mascota), "La edad debería ser inválida por ser demasiado mayor.");
    }

    @Test
    void testEdadApropiadaDonante_MascotaInvalida() {
        Mascota mascotaInvalida = new Mascota(); // Mascota con datos nulos
        assertFalse(servicioMascota.isEdadApropiadaDonante(mascotaInvalida), "No debería validar mascotas inválidas.");
    }

    @Test
    void testEdadApropiadaDonante_NullMascota() {
        assertFalse(servicioMascota.isEdadApropiadaDonante(null), "No debería validar una mascota nula.");
    }


}
