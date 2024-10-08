package com.tallerwebi.dominio;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.entidad.Publicacion;
import com.tallerwebi.dominio.servicio.ServicioFiltro;
import com.tallerwebi.dominio.servicio.ServicioFiltroImpl;
import com.tallerwebi.integracion.config.HibernateTestConfig;
import com.tallerwebi.integracion.config.SpringWebTestConfig;
import com.tallerwebi.presentacion.BancoConTiposDeSangre;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {SpringWebTestConfig.class, HibernateTestConfig.class})
public class ServicioFiltroTest {

    @Autowired
    RepositorioMascota repositorioMascota;
    @Autowired
    RepositorioPublicacion repositorioPublicacion;
    @Autowired
    RepositorioBanco repositorioBanco;
    @Autowired
    ServicioFiltro servicioFiltro = new ServicioFiltroImpl(repositorioMascota,repositorioPublicacion, repositorioBanco);

    @Test
    @Transactional
    @Rollback
    public void filtrarMascota() {
        Mascota mascota = new Mascota();
        mascota.setNombre("Mascota");
        mascota.setSangre("0+");
        mascota.setTipo("Donante");

        repositorioMascota.agregarMascota(mascota);
        ArrayList<Mascota> mascotas = servicioFiltro.consultarMascota("Mascota", "0+", "Donante");
        Mascota recibida = mascotas.get(0);

        assertThat(recibida, equalTo(mascota));
    }

    @Test
    @Transactional
    @Rollback
    public void filtrarVariasMascotas() {
        Mascota mascota = new Mascota();
        mascota.setNombre("Mascota");
        mascota.setSangre("0+");
        mascota.setTipo("Donante");

        Mascota mascota1 = new Mascota();
        mascota1.setNombre("Mascota1");
        mascota1.setSangre("0+");
        mascota1.setTipo("Donante");

        Mascota mascota2 = new Mascota();
        mascota2.setNombre("Mascota2");
        mascota2.setSangre("0+");
        mascota2.setTipo("Recibe");

        repositorioMascota.agregarMascota(mascota);
        repositorioMascota.agregarMascota(mascota1);
        repositorioMascota.agregarMascota(mascota2);

        ArrayList<Mascota> mascotas = servicioFiltro.consultarMascota("", "", "Donante");
        ArrayList<Mascota> esperadas = new ArrayList<>();
        esperadas.add(mascota);
        esperadas.add(mascota1);

        assertThat(mascotas, equalTo(esperadas));
    }

    @Test
    @Transactional
    @Rollback
    public void filtrarUnaPublicacion() {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo("Mascota");
        publicacion.setTipoDeSangre("0+");
        publicacion.setTipoDePublicacion("Donante");
        Publicacion publicacion1 = new Publicacion();
        publicacion.setTitulo("Mascota");
        publicacion.setTipoDeSangre("A+");
        publicacion.setTipoDePublicacion("Donante");


        repositorioPublicacion.guardarPublicacion(publicacion);
        repositorioPublicacion.guardarPublicacion(publicacion1);
        ArrayList<Publicacion> publicaciones = servicioFiltro.consultarPublicaciones("Mascota","A+","","Donante");

        assertThat(publicaciones.size(), equalTo(1));
        assertThat(publicaciones, hasItem(hasProperty("titulo",is("Mascota"))));
    }

    @Test
    @Transactional
    @Rollback
    public void filtrarVariasPublicaciones() {
        Publicacion publicacion = new Publicacion();
        publicacion.setTitulo("Mascota");
        publicacion.setTipoDeSangre("0+");
        publicacion.setTipoDePublicacion("Donante");

        Publicacion publicacion1 = new Publicacion();
        publicacion1.setTitulo("Mascota");
        publicacion1.setTipoDeSangre("A+");
        publicacion1.setTipoDePublicacion("Donante");

        Publicacion publicacion2 = new Publicacion();
        publicacion2.setTitulo("Mascota");
        publicacion2.setTipoDeSangre("A+");
        publicacion2.setTipoDePublicacion("Donante");

        repositorioPublicacion.guardarPublicacion(publicacion);
        repositorioPublicacion.guardarPublicacion(publicacion2);
        repositorioPublicacion.guardarPublicacion(publicacion1);

        ArrayList<Publicacion> publicaciones = servicioFiltro.consultarPublicaciones("Mascota", "", "","Donante");

        assertThat(publicaciones.size(), equalTo(3));
    }

    @Test
    @Transactional
    @Rollback
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
        publicacion2.setTitulo("Mascota");
        publicacion2.setTipoDeSangre("A+");
        publicacion2.setTipoDePublicacion("Donante");

        repositorioPublicacion.guardarPublicacion(publicacion);
        repositorioPublicacion.guardarPublicacion(publicacion2);
        repositorioPublicacion.guardarPublicacion(publicacion1);

        ArrayList<Publicacion> publicaciones = servicioFiltro.consultarPublicaciones("", "", "","");

        assertThat(publicaciones.size(), equalTo(3));
    }

    @Test
    @Transactional
    @Rollback
    void siNoIngresoUnaSangreValidaParaBuscarNoEncuentroResultados() {
        // Crear un banco de prueba
        Banco banco = getBancoTextExamplePuntoCom();
        Banco banco1 = getBancoEmailTestPuntoCom();

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5,"", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3,"", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7,"", banco);

        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("C","");

        assertThat(resultados.size(),is(0));
    }

    @Test
    @Transactional
    @Rollback
    public void siIngresoUnaSangreValidaMeDevuelveTodosLosResultados() {
        // Crear un banco de prueba
        Banco banco = getBancoTextExamplePuntoCom();
        Banco banco1 = getBancoEmailTestPuntoCom();

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("+","");

        assertThat(resultados.size(), is(2) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }

    @Test
    @Transactional
    @Rollback
    void siIngresaSangreComoVacioMeDaraTodosLosResultados() {
        // Crear un banco de prueba
        Banco banco = getBancoTextExamplePuntoCom();
        Banco banco1 = getBancoEmailTestPuntoCom();

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","");

        assertThat(resultados.size(), is(3) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("B-")), hasProperty("cantidad", is(3))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }

    @Test
    @Transactional
    @Rollback
    void siIngresaSangreComoVacioMeDaraTodosLosResultadosEnCoincidenciasDeTipo() {
        // Crear un banco de prueba
        Banco banco = getBancoTextExamplePuntoCom();
        Banco banco1 = getBancoEmailTestPuntoCom();

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","");

        assertThat(resultados.size(), is(3) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("B-")), hasProperty("cantidad", is(3))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }

    @Test
    @Transactional
    @Rollback
    void noObtengoCoincidenciasSiElParametroDeTipoDeProductoEsInvalidoNoExiste() {
        // Crear un banco de prueba
        Banco banco = getBancoTextExamplePuntoCom();
        Banco banco1 = getBancoEmailTestPuntoCom();

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "total", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "globulos", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "total", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","A+");

        assertThat(resultados.size(), is(0) );
    }


    @Test
    @Transactional
    @Rollback
    void obtenerDosCoincidenciasEnTipoDeProductoDeTodosLosBancos() {
        // Crear un banco de prueba
        Banco banco = getBancoTextExamplePuntoCom();
        Banco banco1 = getBancoEmailTestPuntoCom();

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "total", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "globulos", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "total", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","total");

        assertThat(resultados.size(), is(2) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }


    @Test
    @Transactional
    @Rollback
    void siIngresaSangreComoVacioMeDaraTodosLosResultadosEnCoincidenciasDeTipoYSangre() {
        // Crear un banco de prueba
        Banco banco = getBancoTextExamplePuntoCom();
        Banco banco1 = getBancoEmailTestPuntoCom();

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("","");

        assertThat(resultados.size(), is(3) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("B-")), hasProperty("cantidad", is(3))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }

    @Test
    @Transactional
    @Rollback
    void noObtengoCoincidenciasSiElParametroDeTipoDeProductoYElDeSangreEsInvalidoNoExiste() {
        // Crear un banco de prueba
        Banco banco = getBancoTextExamplePuntoCom();
        Banco banco1 = getBancoEmailTestPuntoCom();

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "total", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "globulos", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "total", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("---","---");

        assertThat(resultados.size(), is(0) );
    }


    @Test
    @Transactional
    @Rollback
    void obtenerDosCoincidenciasEnTipoDeProductoYSangreDeTodosLosBancos() {
        // Crear un banco de prueba
        Banco banco = getBancoTextExamplePuntoCom();
        Banco banco1 = getBancoEmailTestPuntoCom();

        // Agregar varios paquetes de sangre
        PaqueteDeSangre paqueteA = new PaqueteDeSangre("A+", 5, "total", banco);
        PaqueteDeSangre paqueteB = new PaqueteDeSangre("B-", 3, "globulos", banco);
        PaqueteDeSangre paqueteO = new PaqueteDeSangre("O+", 7, "total", banco);

        // Guardar el banco en la base de datos
        repositorioBanco.guardar(banco);
        repositorioBanco.guardar(banco1);

        repositorioBanco.guardarSangre(paqueteA,banco);
        repositorioBanco.guardarSangre(paqueteB,banco);
        repositorioBanco.guardarSangre(paqueteO,banco);

        List<BancoConTiposDeSangre> resultados= servicioFiltro.obtenerCoincidenciasEnBancosDeSangre("+","total");

        assertThat(resultados.size(), is(2) );
        assertThat(resultados, hasItems(
                allOf(hasProperty("tipoSangre", is("A+")), hasProperty("cantidad", is(5))),
                allOf(hasProperty("tipoSangre", is("O+")), hasProperty("cantidad", is(7)))
        ));
    }

    private static Banco getBancoEmailTestPuntoCom() {
        return new Banco("Banco Test", "Ciudad", "Dirección", "email@test.com", "9-18", "País", "12345", "123456789");
    }

    private static Banco getBancoTextExamplePuntoCom() {
        return new Banco("Banco Test", "Dirección Test", "Ciudad Test", "País Test",
                "123456789", "test@example.com", "testpassword", "Horario Test");
    }


}