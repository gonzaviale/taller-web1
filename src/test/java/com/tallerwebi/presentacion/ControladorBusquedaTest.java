package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.*;
import com.tallerwebi.dominio.servicio.ServicioFiltro;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import com.tallerwebi.presentacion.DTO.BancoConTiposDeSangreDTO;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ControladorBusquedaTest {

    /*ServicioFiltro servicioFiltro;
    ServicioMascota servicioMascota;
    ControladorBusqueda controladorBusqueda;

    @BeforeEach
    public void init(){
        servicioFiltro= mock(ServicioFiltro.class);
        servicioMascota = mock(ServicioMascota.class)
        controladorBusqueda= new ControladorBusqueda(servicioFiltro, servicioMascota);
    }

    @Test
    public void siElTipoDeBusquedaEsIgualAMascotasTendreEnMiModeloUnaClaveDeListaDeMascotas(){
        givenMockeoElServicioParaQueMeDevuelvaUnaCiertaCantidadDeMascotas(2);

        ModelAndView modelAndView = controladorBusqueda.buscar("mascotas");

        assertThat(modelAndView.getModel().get("listaMascotas"), not(IsEmptyCollection.class));
        assertThat(modelAndView.getModel().get("listaMascotas"), equalTo(servicioFiltro.consultarMascota("","","")));

    }

    @Test
    public void siElTipoDeBusquedaEsIgualAPublicacionTendreEnMiModeloUnaClaveDePublicaciones(){

        givenMockeoElServicioParaQueMeDevuelvaUnaListaDePublicaciones();

        ModelAndView modelAndView = controladorBusqueda.buscar("publicacion");

        List<Publicacion> publicacionList= (List<Publicacion>) modelAndView.getModel().get("publicaciones");

        assertThat(publicacionList, not(IsEmptyCollection.class));
        assertThat(publicacionList.size(), equalTo(2));

    }

    @Test
    public void siElTipoDeBusquedaEsIgualABancoDeSangreTendreEnMiModeloUnaClaveDeBancoDeSangre(){

        givenMockeoElServicioParaQueMeDevuelvaUnaListaDeBancoConTiposDeSangre();

        ModelAndView modelAndView = controladorBusqueda.buscar("banco de sangre");

        List<BancoConTiposDeSangreDTO> bancoConTiposDeSangres= (List<BancoConTiposDeSangreDTO>) modelAndView.getModel().get("listaBancos");

        assertThat(bancoConTiposDeSangres, not(IsEmptyCollection.class));
        assertThat(bancoConTiposDeSangres.size(), equalTo(2));

    }

    @Test
    public void siElTipoDeBusquedaEsIgualusuariosTendreEnMiModeloUnaClaveDeUsuarios(){

        givenMockeoElServicioParaQueMeDevuelvaUnaListaDeDueñosDeMascotas();

        ModelAndView modelAndView = controladorBusqueda.buscar("usuarios");

        List<Usuario> usuarios= (List<Usuario>) modelAndView.getModel().get("listaUsuarios");

        assertThat(usuarios, not(IsEmptyCollection.class));
        assertThat(usuarios.size(), equalTo(2));

    }

    @Test
    public void siElTipoDeBusquedaEsIgualVeterinarioTendreEnMiModeloUnaClaveDelistaVeterinario(){

        givenMockeoElServicioParaQueMeDevuelvaUnaListaDeVeterinario();

        ModelAndView modelAndView = controladorBusqueda.buscar("veterinarios");

        List<Usuario> usuarios= (List<Usuario>) modelAndView.getModel().get("listaVeterinario");

        assertThat(usuarios, not(IsEmptyCollection.class));
        assertThat(usuarios.size(), equalTo(2));

    }

    private void givenMockeoElServicioParaQueMeDevuelvaUnaListaDeVeterinario() {
        ArrayList<Usuario> usuarios= new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            usuarios.add(new Veterinario());
        }

        when(servicioFiltro.obtenerTodosLosVeterinariosVerificados()).thenReturn(usuarios);

    }


    private void givenMockeoElServicioParaQueMeDevuelvaUnaListaDeDueñosDeMascotas() {
        ArrayList<Usuario> usuarios= new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            usuarios.add(new DuenoMascota());
        }

        when(servicioFiltro.obtenerTodosLosUsuariosConPublicacionesOMascotasDadasDeAlta()).thenReturn(usuarios);

    }

    private void givenMockeoElServicioParaQueMeDevuelvaUnaListaDeBancoConTiposDeSangre() {
        ArrayList<BancoConTiposDeSangreDTO> bancoConTiposDeSangres= new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            bancoConTiposDeSangres.add(new BancoConTiposDeSangreDTO());
        }

        when(servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","")).thenReturn(bancoConTiposDeSangres);
    }

    private void givenMockeoElServicioParaQueMeDevuelvaUnaListaDePublicaciones() {

        ArrayList<Publicacion> publicaciones= new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            publicaciones.add(new Publicacion());
        }

        when(servicioFiltro.consultarPublicaciones("","","","")).thenReturn(publicaciones);
    }


    @Test
    public void siNoIngresaNadaAFiltrarDevuelveTodasLasMascotas() {
        //given
        givenMockeoElServicioParaQueMeDevuelvaUnaCiertaCantidadDeMascotas(2);

        ModelAndView modelAndView = controladorBusqueda.filtrarMascotas("", "","");

        assertThat(modelAndView.getModel().get("listaMascotas"), not(IsEmptyCollection.class));
        assertThat(modelAndView.getModel().get("listaMascotas"), equalTo(servicioFiltro.consultarMascota("","","")));
    }

    private void givenMockeoElServicioParaQueMeDevuelvaUnaCiertaCantidadDeMascotas(Integer cantidad) {
        ArrayList<Mascota> mascotas= new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            mascotas.add(new Mascota());
        }
        Mascota mascota = getMascotConNombreSangreYTipo("Mascota", "Donante","0+");
        mascotas.add(mascota);
        when(servicioFiltro.consultarMascota("","","")).thenReturn(mascotas);
    }

    private static Mascota getMascotConNombreSangreYTipo(String nombre, String tipo,String sangre) {
        Mascota mascota = new Mascota();
        mascota.setNombre(nombre);
        mascota.setSangre(sangre);
        mascota.setTipo(tipo);
        return mascota;
    }

    @Test
    public void filtrarVariasMascotas() {

        givenMockeoElServicioParaQueMeDevuelvaDosMascotasDonantes();

        ModelAndView modelAndView= controladorBusqueda.filtrarMascotas("","+","");

        thenEncuentraCoincidenciasDeDosMascotas(modelAndView);
    }

    private static void thenEncuentraCoincidenciasDeDosMascotas(ModelAndView modelAndView) {
        ArrayList <Mascota> mascotas = (ArrayList<Mascota>) modelAndView.getModelMap().get("listaMascotas");
        assertThat(mascotas, hasItem(hasProperty("sangre",is("A+"))));
        assertThat(mascotas.size(), is(2));
    }

    private void givenMockeoElServicioParaQueMeDevuelvaDosMascotasDonantes() {
        ArrayList<Mascota> mascotas= new ArrayList<>();

        Mascota mascota = getMascotConNombreSangreYTipo("Mascota", "Donante","A+");

        Mascota mascota1 = getMascotConNombreSangreYTipo("Mascota1", "Donante","B+");

        mascotas.add(mascota);
        mascotas.add(mascota1);
        when(servicioFiltro.consultarMascota("","+","")).thenReturn(mascotas);

    }

    @Test
    public void filtrarUnaPublicacion() {
        ArrayList <Publicacion> publicacions= new ArrayList<>();
        Publicacion publicacion1 = getPublicacion("Mascota","A+","Donante");
        publicacions.add(publicacion1);
        when(servicioFiltro.consultarPublicaciones("","A+","","")).thenReturn(publicacions);

        ModelAndView modelAndView= controladorBusqueda.filtrarPublicaciones("","A+","","");

        ArrayList <Publicacion> publicaciones = (ArrayList<Publicacion>) modelAndView.getModel().get("publicaciones");
        assertThat(publicaciones, hasItem(hasProperty("titulo",is("Mascota"))));
        assertThat(publicaciones.size(), is(1));
    }

    @Test
    public void filtrarUnBancoDeSangre() {
        ArrayList <Publicacion> publicacions= new ArrayList<>();
        Publicacion publicacion1 = getPublicacion("Mascota","A+","Donante");
        publicacions.add(publicacion1);
        when(servicioFiltro.consultarPublicaciones("","A+","","")).thenReturn(publicacions);

        ModelAndView modelAndView= controladorBusqueda.filtrarPublicaciones("","A+","","");

        ArrayList <Publicacion> publicaciones = (ArrayList<Publicacion>) modelAndView.getModel().get("publicaciones");
        assertThat(publicaciones, hasItem(hasProperty("titulo",is("Mascota"))));
        assertThat(publicaciones.size(), is(1));
    }


    private static Publicacion getPublicacion(String titulo, String sangre,String tipo) {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo(titulo);
        publicacion.setTipoDeSangre(sangre);
        publicacion.setTipoDePublicacion(tipo);
        return publicacion;
    }

    @Test
    public void filtrarVariasPublicaciones() {

        Publicacion publicacion1 = new Publicacion();
        publicacion1.setTitulo("Mascota");
        publicacion1.setTipoDeSangre("A+");
        publicacion1.setTipoDePublicacion("Donante");

        Publicacion publicacion2 = new Publicacion();
        publicacion2.setTitulo("Mascota");
        publicacion2.setTipoDeSangre("A+");
        publicacion2.setTipoDePublicacion("Donante");
        ArrayList <Publicacion> publicacions= new ArrayList<>();
        publicacions.add(publicacion1);
        publicacions.add(publicacion2);

        when(servicioFiltro.consultarPublicaciones("","A+","","")).thenReturn(publicacions);

        ModelAndView modelAndView= controladorBusqueda.filtrarPublicaciones("","A+","","");

        ArrayList <Publicacion> publicaciones = (ArrayList<Publicacion>) modelAndView.getModel().get("publicaciones");
        assertThat(publicaciones, hasItem(hasProperty("tipoDeSangre",is("A+"))));
        assertThat(publicaciones.size(), is(2));
    }

    @Test
    public void siNoHayParametrosElResultadoEsUnaListaConTodasLasPublicaciones() {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo("Mascota");
        publicacion.setTipoDeSangre("0+");
        publicacion.setTipoDePublicacion("Donante");

        Publicacion publicacion1 = new Publicacion();
        publicacion1.setTitulo("Mascota");
        publicacion1.setTipoDeSangre("A+");
        publicacion1.setTipoDePublicacion("Donante");

        Publicacion publicacion2 = new Publicacion();
        publicacion2.setTitulo("-");
        publicacion2.setTipoDeSangre("A+");
        publicacion2.setTipoDePublicacion("Donante");

        ArrayList<Publicacion> publicacions= new ArrayList<>();
        publicacions.add(publicacion);
        publicacions.add(publicacion1);
        publicacions.add(publicacion2);
        when(servicioFiltro.consultarPublicaciones("","","","")).thenReturn(publicacions);

        ModelAndView modelAndView = controladorBusqueda.filtrarPublicaciones("", "","","");
        ArrayList <Publicacion> publicaciones = (ArrayList<Publicacion>) modelAndView.getModel().get("publicaciones");
        assertThat(publicaciones, hasItem(hasProperty("titulo",is("-"))));
        assertThat(publicaciones.size(), equalTo(3));
    }

    @Test
    public void siNoHayCoincidenciasDevuelveUnaListaVacia() {

        when(servicioFiltro.consultarPublicaciones("","","","")).thenReturn(new ArrayList<>());

        ModelAndView modelAndView = controladorBusqueda.filtrarPublicaciones("", "A++","","");

        ArrayList <Publicacion> publicaciones = (ArrayList<Publicacion>) modelAndView.getModel().get("publicaciones");
        assertThat(publicaciones.size(), equalTo(0));
    }

    @Test
    public void siNoHayCoincidenciasDevuelveUnaListaVaciaDeBancosDeTipo() {

        when(servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","")).thenReturn(new ArrayList<>());

        ModelAndView modelAndView = controladorBusqueda.filtrarBancos("sadasd", "");

        ArrayList <BancoConTiposDeSangreDTO> BancoConTiposDeSangre = (ArrayList<BancoConTiposDeSangreDTO>) modelAndView.getModel().get("listaBancos");
        assertThat(BancoConTiposDeSangre.size(), equalTo(0));
    }

    @Test
    public void siSeEnviaVacioComoParametroMeDevuelveLaListaEnteraDeBancos() {

        givenMockeoElServicioParaQueMeDevuelvaLaListaCompletaDeBancosConSagre();

        ModelAndView modelAndView = controladorBusqueda.filtrarBancos("", "");

        ArrayList <BancoConTiposDeSangreDTO> BancoConTiposDeSangre = (ArrayList<BancoConTiposDeSangreDTO>) modelAndView.getModel().get("listaBancos");
        assertThat(BancoConTiposDeSangre.size(), equalTo(3));
    }

    @Test
    public void siSeEnviaUnParametroMeDevuelveElObjetoDeLaLista() {

        givenMockeoElServicioParaQueMeDevuelvaLaListaCompletaDeBancosConSagreConValores();

        ModelAndView modelAndView = controladorBusqueda.filtrarBancos("1", "");

        ArrayList <BancoConTiposDeSangreDTO> BancoConTiposDeSangre = (ArrayList<BancoConTiposDeSangreDTO>) modelAndView.getModel().get("listaBancos");
        assertThat(BancoConTiposDeSangre.size(),equalTo(1));
        assertThat(BancoConTiposDeSangre, hasItem(hasProperty("tipoSangre",is("1"))));
    }


    private void givenMockeoElServicioParaQueMeDevuelvaLaListaCompletaDeBancosConSagre() {

        ArrayList <BancoConTiposDeSangreDTO> bancos= new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            bancos.add(new BancoConTiposDeSangreDTO());
        }

        when(servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","")).thenReturn(bancos);

    }

    private void givenMockeoElServicioParaQueMeDevuelvaLaListaCompletaDeBancosConSagreConValores() {

        ArrayList <BancoConTiposDeSangreDTO> bancos= new ArrayList<>();
        BancoConTiposDeSangreDTO bancoPrueba=new BancoConTiposDeSangreDTO();
            bancoPrueba.setTipoSangre("1");
            bancos.add(bancoPrueba);

        when(servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("1","")).thenReturn(bancos);

    }*/
}
