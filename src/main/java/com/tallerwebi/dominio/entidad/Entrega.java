package com.tallerwebi.dominio.entidad;


import javax.persistence.*;

@Entity
public class Entrega

{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private int solicitudId;
    @Column
    private Long paqueteId;
    @Column
    private  String direccion;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Entrega() {

    }

    public Entrega(int solicitudId, Long paqueteId, String direccion) {

        this.solicitudId= solicitudId;
        this.paqueteId = paqueteId;
        this.direccion = direccion;
    }

    public int getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(int solicitudId) {
        this.solicitudId = solicitudId;
    }

    public Long getPaqueteId() {
        return paqueteId;
    }

    public void setPaqueteId(Long paqueteId) {
        this.paqueteId = paqueteId;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
