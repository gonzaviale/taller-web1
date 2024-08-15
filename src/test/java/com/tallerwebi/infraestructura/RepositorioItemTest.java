package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.Item;
import com.tallerwebi.dominio.RepositorioItem;
import com.tallerwebi.dominio.TipoItem;
import com.tallerwebi.infraestructura.config.HibernateTestInfraestructuraConfig;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {HibernateTestInfraestructuraConfig.class})
public class RepositorioItemTest {

    @Autowired
    private SessionFactory sessionFactory;

    private RepositorioItem repositorioItem;

    @BeforeEach
    public void init(){
        this.repositorioItem = new RepositorioItemImpl(this.sessionFactory);
    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedaGuardarUnItem(){
        // preparacion
        Item item = this.crearItem(TipoItem.ESPADA, "Espada comun");

        // ejecucion
        this.repositorioItem.guardar(item);

        // verificacion
        Item itemObtenido = (Item) this.sessionFactory.getCurrentSession()
                .createQuery("FROM Item where id = 1")
                .getSingleResult();

        assertThat(itemObtenido, equalTo(item));
    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedanObtenerSoloItemsDeTipoItemEscudoCuandoObtengoItemsPorTipoDeItem(){
        // preparacion
        dadoQueExistenItems();

        // ejecucion
        List<Item> itemsObtenidos = this.repositorioItem.obtenerPorTipoItem(TipoItem.ESCUDO);

        // verificacion
        entoncesObtengoCantidadDeItems(itemsObtenidos.size());
    }

    @Test
    @Transactional
    @Rollback
    public void queSePuedaActualizarLaDescripcionDeUnItemExistente(){
        // preparacion
        Item item = this.crearItem(TipoItem.ESCUDO, "Escudo normal");

        // ejecucion
        String descripcionEsperada = "Escudo epico";
        item.setDescripcion(descripcionEsperada);

        this.repositorioItem.actualizar(item);

        // verificacion
        Item itemObtenido = (Item)this.sessionFactory.getCurrentSession()
                .createQuery("FROM Item WHERE descripcion = :descripcion AND tipoItem = :tipoItem")
                .setParameter("descripcion", descripcionEsperada)
                .setParameter("tipoItem", TipoItem.ESCUDO)
                .getSingleResult();

        assertThat(itemObtenido.getDescripcion(), equalTo(descripcionEsperada));
    }

    private Item crearItem(TipoItem tipoItem, String descripcion) {
        Item item = new Item(tipoItem, descripcion);
        this.sessionFactory.getCurrentSession().save(item);
        return item;
    }

    private static void entoncesObtengoCantidadDeItems(Integer cantidadItems) {
        assertThat(cantidadItems, equalTo(1));
    }

    private void dadoQueExistenItems() {
        String descripcionMock = "";
        Item itemEspada = new Item(TipoItem.ESPADA, descripcionMock);
        Item itemEspada2 = new Item(TipoItem.ESPADA, descripcionMock);
        Item itemEscudo = new Item(TipoItem.ESCUDO, descripcionMock);

        this.sessionFactory.getCurrentSession().save(itemEspada);
        this.sessionFactory.getCurrentSession().save(itemEspada2);
        this.sessionFactory.getCurrentSession().save(itemEscudo);
    }
}
