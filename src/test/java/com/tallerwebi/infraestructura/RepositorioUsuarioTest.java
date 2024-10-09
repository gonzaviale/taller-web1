package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.RepositorioUsuario;
import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class RepositorioUsuarioTest {

    @Autowired
    SessionFactory sessionFactory;
    @Autowired
    RepositorioUsuario repositorio;

    @Test
    @Transactional
    @Rollback
    public void buscarPorIdAUnUsuarioExistenteMeRetornaAlUsuario() {
        //given
        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setNombre("Carlos");
        //when
        repositorio.guardar(usuarioGuardado);
        Usuario usuarioEncontrado= repositorio.buscarPorId(usuarioGuardado.getId());

        //then
        assertThat(usuarioEncontrado, notNullValue());
        assertThat(usuarioEncontrado.getNombre(),equalToIgnoringCase("Carlos"));
    }

    @Test
    @Transactional
    @Rollback
    public void buscarPorIdAUnUsuarioNoExistenteMeRetornaNull() {
        //given
        Long id = 1L;
        Usuario usuarioGuardado = new Usuario();
        usuarioGuardado.setId(id);
        usuarioGuardado.setNombre("Carlos");
        //when
        repositorio.guardar(usuarioGuardado);
        Usuario usuarioEncontrado= repositorio.buscarPorId(2L);

        //then
        assertThat(usuarioEncontrado, nullValue());
    }

    @Test
    @Transactional
    @Rollback
    public void queSiMiUsuarioNoTienePublicacionesNiMascotasDadasDeAltaMeRetorneUnaListaVacia() {

        Usuario usuario= new DuenoMascota();

        repositorio.guardar(usuario);

        List<Usuario> usuarioList= repositorio.obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta();

        assertThat(usuario,equalTo(repositorio.buscarPorId(usuario.getId())));

        assertThat(usuarioList.size(),is(0));
    }

    @Test
    @Transactional
    @Rollback
    public void siMiUsuarioTieneMascotasDadasDeAltaMeRetorneUnaListaConMiUsuario(){

        Usuario usuario= new DuenoMascota();

        repositorio.guardar(usuario);

        usuario.agregarMascota(new Mascota());

        List<Usuario> usuarioList= repositorio.obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta();

        assertThat(usuario,equalTo(repositorio.buscarPorId(usuario.getId())));
        assertThat(usuarioList, hasItem(hasProperty("id",is(usuario.getId()))));
        assertThat(usuarioList.size(),is(1));

    }

    @Test
    @Transactional
    @Rollback
    public void siMiUsuarioTienePublicacionesDadasDeAltaMeRetorneUnaListaConMiUsuario(){

        Usuario usuario= new DuenoMascota();

        repositorio.guardar(usuario);

        usuario.agregarPublicaciones(new Publicacion());

        List<Usuario> usuarioList= repositorio.obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta();

        assertThat(usuario,equalTo(repositorio.buscarPorId(usuario.getId())));
        assertThat(usuarioList, hasItem(hasProperty("id",is(usuario.getId()))));
        assertThat(usuarioList.size(),is(1));

    }

    @Test
    @Transactional
    @Rollback
    public void siTengoUnUsuarioConPublicacionesYOtroConMascotaDadaDeAltaMeDevuelveAmbos(){

        Usuario usuario1= new DuenoMascota();

        repositorio.guardar(usuario1);

        usuario1.agregarPublicaciones(new Publicacion());

        Usuario usuario= new DuenoMascota();

        repositorio.guardar(usuario);

        usuario.agregarPublicaciones(new Publicacion());

        List<Usuario> usuarioList= repositorio.obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta();

        assertThat(usuario,equalTo(repositorio.buscarPorId(usuario.getId())));
        assertThat(usuarioList, hasItem(hasProperty("id",is(usuario.getId()))));
        assertThat(usuarioList, hasItem(hasProperty("id",is(usuario1.getId()))));
        assertThat(usuarioList.size(),is(2));
    }

    @Test
    @Transactional
    @Rollback
    public void siNoTengoUsuarioVeterinariosMeDevuelveUnaListaVacia(){

        Usuario usuario= new DuenoMascota();

        repositorio.guardar(usuario);

        List<Usuario> usuarioList= repositorio.obtenerTodosLosVeterinariosVerificados();

        assertThat(usuario,equalTo(repositorio.buscarPorId(usuario.getId())));
        assertThat(usuarioList.size(),is(0));

    }

    @Test
    @Transactional
    @Rollback
    public void siTengoUnUsuarioVeterinarioMeDevuelveElUsuarioVeterinario(){

        Usuario usuario= new Veterinario();

        usuario.setRol("veterinario");

        repositorio.guardar(usuario);

        List<Usuario> usuarioList= repositorio.obtenerTodosLosVeterinariosVerificados();

        assertThat(usuario,equalTo(repositorio.buscarPorId(usuario.getId())));
        assertThat(usuarioList.size(),is(1));

    }

    @Test
    @Transactional
    @Rollback
    public void siTengoDosUsuarioVeterinariosMeTraeAmbos(){

        Veterinario usuario= new Veterinario();
        Veterinario usuario1= new Veterinario();

        usuario.setRol("veterinario");
        usuario1.setRol("veterinario");

        repositorio.guardar(usuario);
        repositorio.guardar(usuario1);

        List<Usuario> usuarioList= repositorio.obtenerTodosLosVeterinariosVerificados();

        assertThat(usuario,equalTo(repositorio.buscarPorId(usuario.getId())));
        assertThat(usuarioList, hasItem(hasProperty("id",is(usuario.getId()))));
        assertThat(usuarioList, hasItem(hasProperty("id",is(usuario1.getId()))));
        assertThat(usuarioList.size(),is(2));

    }

    @Test
    @Transactional
    @Rollback
    public void siTengoUnUsuarioInstanciadoConUnaMascotaConLaSangreBuscadaMeDevuelveUnaListaConElUsuario(){

        Usuario usuario= new DuenoMascota();

        Mascota mascota= new Felino();
        mascota.setSangre("test");
        usuario.agregarMascota(mascota);

        repositorio.guardar(usuario);

        List<Usuario> usuarioList= repositorio.obtenerTodosLosUsuariosQueContenganMascotasConLaSangreBuscada("test");

        assertThat(usuario,equalTo(repositorio.buscarPorId(usuario.getId())));
        assertThat(usuarioList.size(),is(1));

    }

    @Test
    @Transactional
    @Rollback
    public void siTengoUsuariosInstanciadosConLaSangreBuscadaMeDevuelveUnaListaConLosUsuarios(){

        Usuario usuario= obtenerUnDuenioDeMascotaConUnaMascotaConTipoDeSangre("DEA-1","PEPE");
        Usuario usuario1= obtenerUnDuenioDeMascotaConUnaMascotaConTipoDeSangre("DEA-2","CARLOS");

        repositorio.guardar(usuario);
        repositorio.guardar(usuario1);

        List<Usuario> usuarioList= repositorio.obtenerTodosLosUsuariosQueContenganMascotasConLaSangreBuscada("DEA");

        assertThat(usuarioList.size(),is(2));
        assertThat(usuarioList, hasItem(hasProperty("id",is(usuario.getId()))));
        assertThat(usuarioList, hasItem(hasProperty("id",is(usuario1.getId()))));
        assertThat(usuarioList, hasItem(hasProperty("nombre",is("PEPE"))));
        assertThat(usuarioList, hasItem(hasProperty("nombre",is("CARLOS"))));

    }

    @Test
    @Transactional
    @Rollback
    public void siTengoUsuariosInstanciadosConLaSangreBuscadaDePublicacionesMeDevuelveUnaListaConLosUsuarios(){

        Usuario usuario= obtenerUnDuenioDeMascotaConUnaPublicacionConTipoDeSangre("DEA-1");
        Usuario usuario1= obtenerUnDuenioDeMascotaConUnaPublicacionConTipoDeSangre("DEA-2");

        repositorio.guardar(usuario);
        repositorio.guardar(usuario1);

        List<Usuario> usuarioList= repositorio.obtenerTodosLosUsuariosQueContenganPublicacionesConLaSangreBuscada("DEA");

        assertThat(usuarioList.size(),is(2));
        assertThat(usuarioList, hasItem(hasProperty("id",is(usuario.getId()))));
        assertThat(usuarioList, hasItem(hasProperty("id",is(usuario1.getId()))));

    }

    @Test
    @Transactional
    @Rollback
    public void siNoTengoUsuariosInstanciadosConLaSangreBuscadaDePublicacionesMeDevuelveUnaListaConLosUsuariosVacios(){

        Usuario usuario= obtenerUnDuenioDeMascotaConUnaPublicacionConTipoDeSangre("DEA-1");
        Usuario usuario1= obtenerUnDuenioDeMascotaConUnaPublicacionConTipoDeSangre("DEA-2");

        repositorio.guardar(usuario);
        repositorio.guardar(usuario1);

        List<Usuario> usuarioList= repositorio.obtenerTodosLosUsuariosQueContenganPublicacionesConLaSangreBuscada("test");

        assertThat(usuarioList.size(),is(0));

    }

    @Test
    @Transactional
    @Rollback
    public void siNoTengoUsuariosInstanciadosConLaSangreBuscadaDeMascotasMeDevuelveUnaListaConLosUsuariosVacios(){

        Usuario usuario= obtenerUnDuenioDeMascotaConUnaMascotaConTipoDeSangre("DEA-1","PEPE");
        Usuario usuario1= obtenerUnDuenioDeMascotaConUnaMascotaConTipoDeSangre("DEA-2","CARLOS");

        repositorio.guardar(usuario);
        repositorio.guardar(usuario1);

        List<Usuario> usuarioList= repositorio.obtenerTodosLosUsuariosQueContenganPublicacionesConLaSangreBuscada("test");

        assertThat(usuarioList.size(),is(0));

    }


    private DuenoMascota obtenerUnDuenioDeMascotaConUnaPublicacionConTipoDeSangre(String sangrePublicacion){
        DuenoMascota usuario= new DuenoMascota();
        Publicacion publicacion= new Publicacion();
        publicacion.setTipoDeSangre(sangrePublicacion);
        usuario.agregarPublicaciones(publicacion);

        return usuario;

    }


    private DuenoMascota obtenerUnDuenioDeMascotaConUnaMascotaConTipoDeSangre(String sangreMascota,String nombreUsuario){
        DuenoMascota usuario= new DuenoMascota();
        usuario.setNombre(nombreUsuario);
        Mascota mascota= new Felino();
        mascota.setSangre(sangreMascota);
        usuario.agregarMascota(mascota);

        return usuario;
    }

}