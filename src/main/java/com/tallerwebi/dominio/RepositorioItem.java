package com.tallerwebi.dominio;

import java.util.List;

public interface RepositorioItem {

    void guardar(Item item);

    /**
     * Obtiene items por tipo de item
     * @param tipoItem Tipo de item por el cual se filtrara
     * @author Ger
     *
     * */
    List<Item> obtenerPorTipoItem(TipoItem tipoItem);

    void actualizar(Item item);
}
