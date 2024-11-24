package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.servicio.ServicioFiltro;
import com.tallerwebi.dominio.servicio.ServicioFiltroImpl;
import com.tallerwebi.presentacion.DTO.BancoConTiposDeSangreDTO;
import com.tallerwebi.presentacion.DTO.UsuarioFiltradoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class ServicioFiltroTest {

    RepositorioMascota repositorioMascota;
    RepositorioPublicacion repositorioPublicacion;
    RepositorioBanco repositorioBanco;
    RepositorioUsuario repositorioUsuario;
    ServicioFiltro servicioFiltro;

    @BeforeEach
    public void init() {
        repositorioPublicacion = mock(RepositorioPublicacion.class);
        repositorioMascota= mock(RepositorioMascota.class);
        repositorioBanco=mock(RepositorioBanco.class);
        repositorioUsuario=mock(RepositorioUsuario.class);
        servicioFiltro = new ServicioFiltroImpl(
                repositorioMascota,
                repositorioPublicacion,
                repositorioBanco,
                repositorioUsuario);
    }


    @Test
    public void filtrarMascota() {
        Mascota mascota = getMascotaConSangreCeroPositivoDonante("mascota","0+","Donante");

        ArrayList<Mascota> listaMascotas = new ArrayList<>();
        listaMascotas.add(mascota);

        when(repositorioMascota.buscarMascota("Mascota", "0+", "Donante")).thenReturn(listaMascotas);

        ArrayList<Mascota> mascotas = servicioFiltro.consultarMascota("Mascota", "0+", "Donante");
        Mascota recibida = mascotas.get(0);

        assertThat(recibida, equalTo(mascota));
    }

    @Test
    public void filtrarVariasMascotas() {
        Mascota mascota = getMascotaConSangreCeroPositivoDonante("mascota","0+","Donante");

        Mascota mascota1 = getMascotaConSangreCeroPositivoDonante("mascota1","0+","Donante");

        ArrayList<Mascota> listaMascotas = new ArrayList<>();
        listaMascotas.add(mascota);
        listaMascotas.add(mascota1);

        when(repositorioMascota.buscarMascota("", "", "Donante")).thenReturn(listaMascotas);

        ArrayList<Mascota> mascotas = servicioFiltro.consultarMascota("", "", "Donante");
        ArrayList<Mascota> esperadas = new ArrayList<>();
        esperadas.add(mascota);
        esperadas.add(mascota1);

        assertThat(mascotas, equalTo(esperadas));
    }

    @Test
    public void filtrarUnaPublicacion() {
        Publicacion publicacion1 = getPublicacion("A+");

        ArrayList<Publicacion> listaDepublicaciones = new ArrayList<>();
        listaDepublicaciones.add(publicacion1);

        when(repositorioPublicacion.buscarPublicaciones("Mascota", "A+", "","Donante")).thenReturn(listaDepublicaciones);

        ArrayList<Publicacion> publicaciones = servicioFiltro.consultarPublicaciones("Mascota","A+","","Donante");

        assertThat(publicaciones.size(), equalTo(1));
        assertThat(publicaciones, hasItem(hasProperty("titulo",is("Mascota"))));
    }

    private static Publicacion getPublicacion(String tipoDeSangre) {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo("Mascota");
        publicacion.setTipoDeSangre(tipoDeSangre);
        return publicacion;
    }

    @Test
    public void filtrarVariasPublicaciones() {
        Publicacion publicacion = getPublicacion("0+");

        Publicacion publicacion1 = getPublicacion("A+");

        Publicacion publicacion2 = getPublicacion("A+");

        ArrayList<Publicacion> listaDepublicaciones = new ArrayList<>();
        listaDepublicaciones.add(publicacion1);
        listaDepublicaciones.add(publicacion);
        listaDepublicaciones.add(publicacion2);

        when(repositorioPublicacion.buscarPublicaciones("Mascota", "", "","Donante")).thenReturn(listaDepublicaciones);

        ArrayList<Publicacion> publicaciones = servicioFiltro.consultarPublicaciones("Mascota", "", "","Donante");

        assertThat(publicaciones.size(), equalTo(3));
    }

    @Test
    public void siNoHayParametrosElResultadoEsUnaListaConTodasLasPublicaciones() {
        Publicacion publicacion = getPublicacion("0+");

        Publicacion publicacion1 = getPublicacion("A+");

        Publicacion publicacion2 = getPublicacion("A+");

        ArrayList<Publicacion> listaDepublicaciones = new ArrayList<>();
        listaDepublicaciones.add(publicacion1);
        listaDepublicaciones.add(publicacion);
        listaDepublicaciones.add(publicacion2);

        when(repositorioPublicacion.buscarPublicaciones("", "", "","")).thenReturn(listaDepublicaciones);

        ArrayList<Publicacion> publicaciones = servicioFiltro.consultarPublicaciones("", "", "","");

        assertThat(publicaciones.size(), equalTo(3));
    }

    @Test
    void siNoIngresoUnaSangreValidaParaBuscarNoEncuentroResultados() {
        ArrayList <BancoConTiposDeSangreDTO> list = new ArrayList<>();

        when (repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos("C")).thenReturn(list);

        List<BancoConTiposDeSangreDTO> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("C","");

        assertThat(resultados.size(),is(0));
    }
/*
    @Test
    public void siIngresoUnaSangreValidaMeDevuelveTodosLosResultados() {
        // Crear un banco de prueba
        Banco banco = getBancoTextExamplePuntoCom();

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "", banco);

        banco.agregarPaqueteDeSangre(paqueteA);
        banco.agregarPaqueteDeSangre(paqueteO);

        BancoConTiposDeSangreDTO bancoConTiposDeSangre= getBancoConTiposDeSangre(banco,paqueteA);
        BancoConTiposDeSangreDTO bancoConTiposDeSangre1= getBancoConTiposDeSangre(banco,paqueteO);

        ArrayList <BancoConTiposDeSangreDTO> list = new ArrayList<>();
        list.add(bancoConTiposDeSangre);
        list.add(bancoConTiposDeSangre1);

        when(repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos("+")).thenReturn(list);

        List<BancoConTiposDeSangreDTO> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("+","");

        assertThat(resultados.size(), is(2) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }

    @Test
    void siIngresaSangreComoVacioMeDaraTodosLosResultados() {
        // Crear un banco de prueba
        Banco banco = getBancoTextExamplePuntoCom();

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "", banco);

        banco.agregarPaqueteDeSangre(paqueteA);
        banco.agregarPaqueteDeSangre(paqueteO);

        BancoConTiposDeSangreDTO bancoConTiposDeSangre= getBancoConTiposDeSangre(banco,paqueteA);
        BancoConTiposDeSangreDTO bancoConTiposDeSangre1= getBancoConTiposDeSangre(banco,paqueteO);
        BancoConTiposDeSangreDTO bancoConTiposDeSangre2=getBancoConTiposDeSangre(banco,paqueteB);

        ArrayList <BancoConTiposDeSangreDTO> list = new ArrayList<>();
        list.add(bancoConTiposDeSangre);
        list.add(bancoConTiposDeSangre1);
        list.add(bancoConTiposDeSangre2);

        when(repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos("")).thenReturn(list);

        List<BancoConTiposDeSangreDTO> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","");

        assertThat(resultados.size(), is(3) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("B-")), hasProperty("cantidad", is(3))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }

    @Test
    void siIngresaSangreComoVacioMeDaraTodosLosResultadosEnCoincidenciasDeTipo() {
        Banco banco = getBancoTextExamplePuntoCom();

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "", banco);

        // Guardar el banco en la base de datos
        banco.agregarPaqueteDeSangre(paqueteA);
        banco.agregarPaqueteDeSangre(paqueteO);

        BancoConTiposDeSangreDTO bancoConTiposDeSangre= getBancoConTiposDeSangre(banco,paqueteA);
        BancoConTiposDeSangreDTO bancoConTiposDeSangre1= getBancoConTiposDeSangre(banco,paqueteO);
        BancoConTiposDeSangreDTO bancoConTiposDeSangre2=getBancoConTiposDeSangre(banco,paqueteB);

        ArrayList <BancoConTiposDeSangreDTO> list = new ArrayList<>();
        list.add(bancoConTiposDeSangre);
        list.add(bancoConTiposDeSangre1);
        list.add(bancoConTiposDeSangre2);

        when(repositorioBanco.obtenerLaCoincidenciaEnSangreDeTodosLosBancos("")).thenReturn(list);

        List<BancoConTiposDeSangreDTO> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","");

        assertThat(resultados.size(), is(3) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("B-")), hasProperty("cantidad", is(3))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    } */

    @Test
    void noObtengoCoincidenciasSiElParametroDeTipoDeProductoEsInvalidoNoExiste() {

        ArrayList <BancoConTiposDeSangreDTO> list = new ArrayList<>();

        when(repositorioBanco.obtenerLaCoincidenciaEnTipoDeProductoDeTodosLosBancos("A+")).thenReturn(list);

        List<BancoConTiposDeSangreDTO> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","A+");

        assertThat(resultados.size(), is(0) );
    }

/*
    @Test
    void obtenerDosCoincidenciasEnTipoDeProductoDeTodosLosBancos() {
        Banco banco = getBancoTextExamplePuntoCom();

        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "total", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "total", banco);

        BancoConTiposDeSangreDTO bancoConTiposDeSangre= getBancoConTiposDeSangre(banco,paqueteA);
        BancoConTiposDeSangreDTO bancoConTiposDeSangre1= getBancoConTiposDeSangre(banco,paqueteO);

        ArrayList <BancoConTiposDeSangreDTO> list = new ArrayList<>();
        list.add(bancoConTiposDeSangre);
        list.add(bancoConTiposDeSangre1);
        when(repositorioBanco.obtenerLaCoincidenciaEnTipoDeProductoDeTodosLosBancos("total")).thenReturn(list);

        List<BancoConTiposDeSangreDTO> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","total");

        assertThat(resultados.size(), is(2) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    } */

    @Test
    void noObtengoCoincidenciasSiElParametroDeTipoDeProductoYElDeSangreEsInvalidoNoExiste() {
        ArrayList <BancoConTiposDeSangreDTO> list = new ArrayList<>();

        when(repositorioBanco.obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos("---","---")).thenReturn(list);

        List<BancoConTiposDeSangreDTO> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("---","---");

        assertThat(resultados.size(), is(0) );
    }

/*
    @Test
    void obtenerDosCoincidenciasEnTipoDeProductoYSangreDeTodosLosBancos() {
        // Crear un banco de prueba
        Banco banco = getBancoTextExamplePuntoCom();

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "total", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "total", banco);

        BancoConTiposDeSangreDTO bancoConTiposDeSangre= getBancoConTiposDeSangre(banco,paqueteA);
        BancoConTiposDeSangreDTO bancoConTiposDeSangre1= getBancoConTiposDeSangre(banco,paqueteO);

        ArrayList <BancoConTiposDeSangreDTO> list = new ArrayList<>();
        list.add(bancoConTiposDeSangre);
        list.add(bancoConTiposDeSangre1);

        when(repositorioBanco.obtenerCoincidenciaEnTipoDeProductoYSangreDeTodosLosBancos("+","total")).thenReturn(list);

        List<BancoConTiposDeSangreDTO> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("+","total");

        assertThat(resultados.size(), is(2) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }*/

    @Test
    void siNoTengoMascotasOPublicacionesDadasDeAltaMeRetornaUnaListaVacia(){
        List<Usuario> list= new ArrayList<>();

        when(repositorioUsuario.obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta()).thenReturn(list);

        List<Usuario> listaEsperda= servicioFiltro.obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta();

        assertThat(listaEsperda.size(),is(0));

    }

    @Test
    void siNoTengoUsuarioVeterinariosDadosDeAltaMeDevuelveUnaListaVacia(){
        List<Usuario> list= new ArrayList<>();

        when(repositorioUsuario.obtenerTodosLosVeterinariosVerificados()).thenReturn(list);

        List<Usuario> listaEsperda= servicioFiltro.obtenerTodosLosVeterinariosVerificados();

        assertThat(listaEsperda.size(),is(0));

    }

    @Test
    void siTengoUsuarioVeterinariosDadoDeAltaMeDevuelveUnaListaConEseUsuario(){
        List<Usuario> list= new ArrayList<>();
        Usuario usuario= new Veterinario();
        usuario.setRol("veterinario");
        list.add(usuario);

        when(repositorioUsuario.obtenerTodosLosVeterinariosVerificados()).thenReturn(list);

        List<Usuario> listaEsperda= servicioFiltro.obtenerTodosLosVeterinariosVerificados();

        assertThat(listaEsperda.size(),is(1));
        assertThat(listaEsperda, hasItems(hasProperty("rol",is("veterinario"))));

    }


    @Test
    void siTengoMascotasOPublicacionesDadasDeAltaMeRetornaListaConElUsuario(){

        Usuario usuario=getUsuarioConMascota();
        Usuario usuario1=getUsuarioConPublicacion();

        List<Usuario> list= new ArrayList<>();
        list.add(usuario);
        list.add(usuario1);

        when(repositorioUsuario.obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta()).thenReturn(list);

        List<Usuario> listaEsperda= servicioFiltro.obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta();

        assertThat(listaEsperda.size(),is(2));
        assertThat(listaEsperda, hasItems(hasProperty("rol",is("dueno mascota"))));

    }

    @Test
    public void testObtenerCoincidenciasPorPublicacionConSangreBuscada() {
        String sangreBuscada = "A+";
        String tipoDeBusqueda = "publicacion";

        List<Usuario> usuariosMock = new ArrayList<>();
        Usuario usuario = new Usuario();
        usuariosMock.add(usuario);

        // Configurar mock para simular el repositorio
        when(repositorioUsuario.obtenerTodosLosUsuariosQueContenganPublicacionesConLaSangreBuscada(sangreBuscada)).thenReturn(usuariosMock);

        // Ejecutar el método
        List<UsuarioFiltradoDTO> resultado = servicioFiltro.obtenerCoincidenciasEnSangreBuscadaYSuTipoDeBusqueda(sangreBuscada, tipoDeBusqueda);

        // Verificar el resultado
        assertThat(resultado, is(notNullValue()));
        assertThat(resultado.size(), is(1));
        verify(repositorioUsuario).obtenerTodosLosUsuariosQueContenganPublicacionesConLaSangreBuscada(sangreBuscada);
    }

    @Test
    public void testObtenerCoincidenciasPorMascotaConSangreBuscada() {
        String sangreBuscada = "B-";
        String tipoDeBusqueda = "mascota";

        List<Usuario> usuariosMock = new ArrayList<>();
        Usuario usuario = new Usuario();
        usuariosMock.add(usuario);

        // Configurar mock para simular el repositorio
        when(repositorioUsuario.obtenerTodosLosUsuariosQueContenganMascotasConLaSangreBuscada(sangreBuscada)).thenReturn(usuariosMock);

        // Ejecutar el método
        List<UsuarioFiltradoDTO> resultado = servicioFiltro.obtenerCoincidenciasEnSangreBuscadaYSuTipoDeBusqueda(sangreBuscada, tipoDeBusqueda);

        // Verificar el resultado
        assertThat(resultado, is(Collections.emptyList()));
    }

    @Test
    public void testNoObtenerCoincidenciasTipoDeBusquedaInvalido() {
        String sangreBuscada = "O+";
        String tipoDeBusqueda = "otro";

        List<Usuario> usuariosMock = new ArrayList<>();
        Usuario usuario = new Usuario();
        usuariosMock.add(usuario);

        // Configurar mock para simular el método alternativo
        when(servicioFiltro.obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta()).thenReturn(usuariosMock);

        // Ejecutar el método
        List<UsuarioFiltradoDTO> resultado = servicioFiltro.obtenerCoincidenciasEnSangreBuscadaYSuTipoDeBusqueda(sangreBuscada, tipoDeBusqueda);

        // Verificar el resultado
        assertThat(resultado, is(nullValue()));

    }

    @Test
    public void testObtenerCoincidenciasConParametrosNulosOVacios() {
        String sangreBuscada = "";
        String tipoDeBusqueda = "publicacion";

        List<Usuario> usuariosMock = new ArrayList<>();
        Usuario usuario = new Usuario();
        usuariosMock.add(usuario);

        // Configurar mock para simular el repositorio
        when(repositorioUsuario.obtenerTodosLosUsuariosQueContenganPublicacionesConLaSangreBuscada(sangreBuscada)).thenReturn(usuariosMock);

        // Ejecutar el método con parámetros vacíos
        List<UsuarioFiltradoDTO> resultado = servicioFiltro.obtenerCoincidenciasEnSangreBuscadaYSuTipoDeBusqueda(sangreBuscada, tipoDeBusqueda);

        // Verificar el resultado
        assertThat(resultado, is(notNullValue()));
        assertThat(resultado.size(), is(1));
        verify(repositorioUsuario).obtenerTodosLosUsuariosQueContenganPublicacionesConLaSangreBuscada(sangreBuscada);
    }

    @Test
    void obtenerTodosLosVeterinariosNoVerificadosMeDevuelveUnaListaDeVeterinarios() {
        // Configuración de Mockito

        Veterinario veterinario=new Veterinario();
        Veterinario veterinario1=new Veterinario();

        List<Usuario> veterinariosNoVerificados= new ArrayList<>();

        veterinariosNoVerificados.add(veterinario);
        veterinariosNoVerificados.add(veterinario1);

        when(repositorioUsuario.obtenerTodosLosVeterinariosNoVerificados()).thenReturn(veterinariosNoVerificados);

        // Ejecución
        List<Usuario> resultado = servicioFiltro.obtenerTodosLosVeterinariosNoVerificados();

        // Verificación
        assertThat(resultado.size(),is(2));
        verify(repositorioUsuario, times(1)).obtenerTodosLosVeterinariosNoVerificados();
    }

    @Test
    void obtenerTodosLosVeterinariosNoVerificadosMeDevuelveUnaListaVaciaDeVeterinarios() {

        when(repositorioUsuario.obtenerTodosLosVeterinariosNoVerificados()).thenReturn(new ArrayList<>());

        // Ejecución
        List<Usuario> resultado = servicioFiltro.obtenerTodosLosVeterinariosNoVerificados();

        // Verificación
        assertThat(resultado.size(),is(0));
        verify(repositorioUsuario, times(1)).obtenerTodosLosVeterinariosNoVerificados();
    }

    @Test
    void activarUsuarioMeDevuelveTrue() {
        // Configuración de Mockito
        Long idUsuario = 1L;
        when(repositorioUsuario.activarUsuarioBuscadoPor(idUsuario)).thenReturn(true);

        // Ejecución
        Boolean resultado = servicioFiltro.activarUsuarioBuscadoPor(idUsuario);

        // Verificación
        assertThat(resultado, is(Boolean.TRUE));
        verify(repositorioUsuario, times(1)).activarUsuarioBuscadoPor(idUsuario);
    }

    @Test
    void activarUsuarioMeDevuelveFalseSiNoEncuentraAlUsuario() {
        Long idUsuario = 1L;
        when(repositorioUsuario.activarUsuarioBuscadoPor(idUsuario)).thenReturn(false);

        boolean resultado = servicioFiltro.activarUsuarioBuscadoPor(idUsuario);

        assertThat(resultado, is(Boolean.FALSE));
        verify(repositorioUsuario, times(1)).activarUsuarioBuscadoPor(idUsuario);
    }

    @Test
    void cuandoDesactivoUnUsuarioMeRetornaTrueSiElUsuarioSePudoDesactivar() {
        Long idUsuario = 1L;
        when(repositorioUsuario.desactivarUsuarioBuscadoPor(idUsuario)).thenReturn(true);

        boolean resultado = servicioFiltro.desactivarUsuarioBuscadoPor(idUsuario);

        assertThat(resultado, is(Boolean.TRUE));
        verify(repositorioUsuario, times(1)).desactivarUsuarioBuscadoPor(idUsuario);
    }

    @Test
    void desactivarUsuarioMeRetornaFalseSiNoEncuentraQueUsuarioDesactivar() {
        Long idUsuario = 1L;
        when(repositorioUsuario.desactivarUsuarioBuscadoPor(idUsuario)).thenReturn(false);

        boolean resultado = servicioFiltro.desactivarUsuarioBuscadoPor(idUsuario);

        assertThat(resultado,is(Boolean.FALSE));
        verify(repositorioUsuario, times(1)).desactivarUsuarioBuscadoPor(idUsuario);
    }


    private Usuario getUsuarioConPublicacion() {
        Usuario usuario= new DuenoMascota();
        usuario.setRol("dueno mascota");
        usuario.agregarPublicaciones(new Publicacion());
        return usuario;
    }

    private Usuario getUsuarioConMascota()
    {
        Usuario usuario= new DuenoMascota();
        usuario.setRol("dueno mascota");
        usuario.agregarMascota(new Mascota());
        return usuario;

    }

    private static Banco getBancoTextExamplePuntoCom() {
        return new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
    }

    private Mascota getMascotaConSangreCeroPositivoDonante(String nombre, String sangre,String tipo) {
        Mascota mascota= new Mascota();
        mascota.setNombre(nombre);
        mascota.setSangre(sangre);
        mascota.setTipo(tipo);
        return mascota;
    }

    private BancoConTiposDeSangreDTO getBancoConTiposDeSangre(Banco banco, PaqueteDeSangre paquete) {
        BancoConTiposDeSangreDTO bancoConTipos = new BancoConTiposDeSangreDTO();
        bancoConTipos.setBancoId(banco.getId());
        bancoConTipos.setNombreBanco(banco.getNombreBanco());
        bancoConTipos.setDireccion(banco.getDireccion());
        bancoConTipos.setTelefono(banco.getTelefono());
        bancoConTipos.setCiudad(banco.getCiudad());
        bancoConTipos.setEmail(banco.getEmail());
        bancoConTipos.setTipoSangre(paquete.getTipoSangre());
        bancoConTipos.setSangreId(paquete.getId());
        bancoConTipos.setTipoProducto(paquete.getTipoProducto());
        bancoConTipos.setCantidad(paquete.getCantidad());
        return bancoConTipos;
    }


}