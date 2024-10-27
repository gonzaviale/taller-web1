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

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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

        usuario.setEstado("activo");

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

        usuario.setEstado("activo");
        usuario1.setEstado("activo");

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

    @Test @Transactional @Rollback
    void actualizarUsuario_deberiaActualizarUsuarioCorrectamente() {
        // Preparar los datos: Crear un usuario y guardarlo en la base de datos
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");
        repositorio.guardar(usuario);

        // Modificar los datos del usuario
        usuario.setNombre("Juan Actualizado");

        // Ejecutar el método a probar
        repositorio.actualizarUsuario(usuario);

        // Verificar: Recuperar el usuario actualizado y verificar los cambios
        Usuario usuarioActualizado = repositorio.buscarPorId(usuario.getId());
        assertEquals("Juan Actualizado", usuarioActualizado.getNombre());
    }

    @Test
    @Transactional
    @Rollback
    void obtenerMascotaDelUsuario_deberiaRetornarMascotasDelUsuario() {
        // Preparar: Crear un usuario y guardarlo en la base de datos
        Usuario duenio = new Usuario();
        duenio.setNombre("Juan");

        // Crear algunas mascotas para el usuario
        Mascota mascota1 = new Mascota();
        mascota1.setNombre("Fido");
        mascota1.setDuenio(duenio);
        duenio.agregarMascota(mascota1);

        Mascota mascota2 = new Mascota();
        mascota2.setNombre("Rex");
        mascota2.setDuenio(duenio);
        duenio.agregarMascota(mascota2);

        repositorio.guardar(duenio);

        // Ejecutar el método a probar
        List<Mascota> mascotas = repositorio.obtenerMascotaDelUsuario(duenio.getId());

        // Verificar: La lista debe contener las mascotas que hemos guardado
        assertThat(mascotas, notNullValue());
        assertEquals(2, mascotas.size());
        assertThat(mascotas, hasItem(hasProperty("nombre",is("Rex"))));
        assertThat(mascotas, hasItem(hasProperty("nombre",is("Fido"))));

    }

    @Test
    @Transactional
    @Rollback
    void obtenerMascotaDelUsuario_noDeberiaRetornarMascotasParaIdInexistente() {
        // Ejecutar el método con un ID de usuario que no existe
        List<Mascota> mascotas = repositorio.obtenerMascotaDelUsuario(999L); // ID inexistente

        // Verificar: La lista debe estar vacía
        assertThat(mascotas, notNullValue());
        assertThat(mascotas, is(Collections.emptyList()));
    }

    @Test
    @Rollback
    @Transactional
    void obtenerPublicacionesDelUsuario_noDeberiaRetornarPublicacionesParaIdInexistente() {
        // Ejecutar el método con un ID de usuario que no existe
        List<Publicacion> publicaciones = repositorio.obtenerPublicacionesDelUsuario(999L); // ID inexistente

        // Verificar: La lista debe estar vacía
        assertThat(publicaciones, notNullValue());
        assertThat(publicaciones, is(Collections.emptyList()));
    }

    @Test
    @Rollback
    @Transactional
    void obtenerPublicacionesDelUsuario_deberiaRetornarPublicacionesDelUsuario() {

        Usuario duenio = new Usuario();
        duenio.setNombre("Juan");

        // Crear algunas publicaciones para el usuario
        Publicacion publicacion1 = new Publicacion();
        publicacion1.setTitulo("Publicación 1");
        publicacion1.setDuenioPublicacion(duenio);
        duenio.agregarPublicaciones(publicacion1);

        Publicacion publicacion2 = new Publicacion();
        publicacion2.setTitulo("Publicación 2");
        publicacion2.setDuenioPublicacion(duenio);
        duenio.agregarPublicaciones(publicacion2);

        repositorio.guardar(duenio);

        List<Publicacion> publicaciones = repositorio.obtenerPublicacionesDelUsuario(duenio.getId());

        assertThat(publicaciones, notNullValue());
        assertEquals(2, publicaciones.size());
        assertThat(publicaciones, hasItem(hasProperty("titulo", is("Publicación 1"))));
        assertThat(publicaciones, hasItem(hasProperty("titulo", is("Publicación 2"))));
    }

    @Test
    @Rollback
    @Transactional
    void obtenerTodosLosVeterinariosNoVerificadosMeRetornaUnaListaConDosVeterinarios(){

        Usuario vet= new Veterinario();
        Usuario vet1= new Veterinario();

        vet.setRol("veterinario");
        vet1.setRol("veterinario");

        repositorio.guardar(vet);
        repositorio.guardar(vet1);

        List<Usuario> veterinarios= repositorio.obtenerTodosLosVeterinariosNoVerificados();

        assertThat(veterinarios.size(),is(2));
    }

    @Test
    @Rollback
    @Transactional
    void obtenerTodosLosVeterinariosNoVerificadosMeRetornaUnaListaVacia(){
        List<Usuario> veterinarios= repositorio.obtenerTodosLosVeterinariosNoVerificados();

        assertThat(veterinarios.size(),is(0));
    }

    @Test
    @Rollback
    @Transactional
    void obtenerTodosLosVeterinariosVerificadosMeRetornaUnaListaVacia(){
        List<Usuario> veterinarios= repositorio.obtenerTodosLosVeterinariosVerificados();

        assertThat(veterinarios.size(),is(0));
    }

    @Test
    @Rollback
    @Transactional
    void obtenerTodosLosVeterinariosVerificadosMeRetornaUnaListaConDosVeterinarios(){

        Usuario vet= new Veterinario();
        Usuario vet1= new Veterinario();

        vet.setRol("veterinario");
        vet1.setRol("veterinario");

        repositorio.guardar(vet);
        repositorio.guardar(vet1);

        vet.setEstado("activo");
        vet1.setEstado("activo");

        List<Usuario> veterinarios= repositorio.obtenerTodosLosVeterinariosVerificados();

        assertThat(veterinarios.size(),is(2));
    }

    @Test
    @Rollback
    @Transactional
    void activarUnUsuarioMeDevuelveTrue(){
        Usuario usuario= new DuenoMascota();

        repositorio.guardar(usuario);

        repositorio.activarUsuarioBuscadoPor(usuario.getId());

        assertThat(repositorio.activarUsuarioBuscadoPor(usuario.getId()),is(Boolean.TRUE));
    }

    @Test
    @Rollback
    @Transactional
    void desactivarUnUsuarioMeDevuelveTrue(){
        Usuario usuario= new DuenoMascota();

        repositorio.guardar(usuario);

        assertThat(repositorio.desactivarUsuarioBuscadoPor(usuario.getId()),is(Boolean.TRUE));
    }

    @Test
    @Rollback
    @Transactional
    void activarUnUsuarioMeDevuelveFalsePorQueNoEncuentraUnUsuario(){

        assertThat(repositorio.activarUsuarioBuscadoPor(1L),is(Boolean.FALSE));
    }


    @Test
    @Rollback
    @Transactional
    void desactivarUnUsuarioMeDevuelveFalsePorQueNoEncuentraUnUsuario(){

        assertThat(repositorio.desactivarUsuarioBuscadoPor(1L),is(Boolean.FALSE));
    }
}