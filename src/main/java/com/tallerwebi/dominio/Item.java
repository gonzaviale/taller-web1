package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
//@Table(name = "item_db") // Para usar una tabla existente, pero en esta clase
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private TipoItem tipoItem;

    @Column(length = 100, nullable = false)
    private String descripcion;

    @Transient // Este atributo no es considerado para la tabla en la bd
    private String noEnDb;

    public Item() {

    }

    public Item(TipoItem tipoItem, String descripcion) {
        this.tipoItem = tipoItem;
        this.descripcion = descripcion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoItem getTipoItem() {
        return tipoItem;
    }

    public void setTipoItem(TipoItem tipoItem) {
        this.tipoItem = tipoItem;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
