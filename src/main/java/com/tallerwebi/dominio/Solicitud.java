package com.tallerwebi.dominio;


import javax.persistence.*;
import java.util.Objects;

@Entity
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private long  bancoId;
    @Column
    private  long usuarioId;
    @Column
    private String tipoProducto;
    @Column
    private String tipoSangre;
    @Column
    private int cantidad;
    @Column
    private String estado;


    public Solicitud(long bancoId, long usuarioId, String tipoProducto, String tipoSangre, int cantidad) {
        this.bancoId = bancoId;
        this.usuarioId = usuarioId;
        this.tipoProducto = tipoProducto;
        this.tipoSangre = tipoSangre;
        this.cantidad = cantidad;
        this.estado="pendiente";
    }

    public Solicitud() {

    }

    public int getId() {
        return id;
    }
    public String getEstado() {
        return estado;
    }

    public long getBancoId() {
        return bancoId;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public String getTipoSangre() {
        return tipoSangre;
    }

    public String getTipoProducto() {
        return tipoProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solicitud solicitud = (Solicitud) o;
        return id == solicitud.id && bancoId == solicitud.bancoId && usuarioId == solicitud.usuarioId && cantidad == solicitud.cantidad && Objects.equals(tipoProducto, solicitud.tipoProducto) && Objects.equals(tipoSangre, solicitud.tipoSangre) && Objects.equals(estado, solicitud.estado);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bancoId, usuarioId, tipoProducto, tipoSangre, cantidad, estado);
    }
}
