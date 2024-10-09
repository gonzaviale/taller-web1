package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import com.tallerwebi.dominio.servicio.ServicioMascotaImpl;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class ServicioMascotaTest {
    private RepositorioMascota repositorioMascotaMock = mock(RepositorioMascota.class);
    private ServicioMascota servicioMascota = new ServicioMascotaImpl(repositorioMascotaMock);

    @Test
    @Rollback
    @Transactional
    public void queSePuedaTraerUnaMascotaPorSuDueno() {
        Mascota mascota = mock(Mascota.class);
        Usuario dueno = mock(Usuario.class);

        when(dueno.getNombre()).thenReturn("Lucia");

        when(mascota.getDuenio()).thenReturn(dueno);
        when(mascota.getNombre()).thenReturn("Firulais");

        List<Mascota> mascotasMock = Arrays.asList(mascota);
        when(repositorioMascotaMock.obtenerMascotasPorDueno(dueno)).thenReturn(mascotasMock);

        List<Mascota> miMascota = servicioMascota.obtenerMascotasPorDueno(dueno);

        assertThat(miMascota.get(0).getNombre(), equalToIgnoringCase("Firulais"));
    }
}
