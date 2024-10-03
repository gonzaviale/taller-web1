package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String tipo;
    private String sangre;
    private boolean donante;
    private boolean receptor;
    private Integer anios;
    private Float peso;
    private boolean enRevision;
    private boolean aprobado;
    private boolean rechazado;

    @ManyToOne
    private Usuario duenio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSangre() {
        return sangre;
    }

    public void setSangre(String sangre) {
        this.sangre = sangre;
    }

    public Usuario getDuenio() {
        return duenio;
    }

    public void setDuenio(Usuario duenio) {
        this.duenio = duenio;
    }


    public void setDonante(boolean donante) {
        this.donante = donante;
    }

    public boolean isEnRevision() {
        return this.enRevision;
    }

    public void setRevision(boolean revision) {
        this.enRevision = revision;
    }

    public boolean isAprobado() {
        return this.aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public boolean isRechazado() {
        return this.rechazado;
    }

    public void setRechazado(boolean rechazado) {
        this.rechazado = rechazado;
    }

    public boolean isReceptor() {
        return receptor;
    }

    public void setReceptor(boolean receptor) {
        this.receptor = receptor;
    }

    public void setAnios(Integer anios) {
        this.anios = anios;
    }

    public Integer getAnios() {
        return this.anios;
    }

    public void setPeso(Float peso) {
        this.peso = peso;
    }

    public Float getPeso() {
        return this.peso;
    }
}