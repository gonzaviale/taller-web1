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


    @Column
    private Long usuarioId;

    @Column
    private String horario;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Entrega() {

    }

    public Entrega(int solicitudId, Long paqueteId, String direccion,String horario,Long usuarioId) {

        this.solicitudId= solicitudId;
        this.paqueteId = paqueteId;
        this.direccion = direccion;
        this.horario = horario;
        this.usuarioId = usuarioId;
    }


    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }


    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
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
