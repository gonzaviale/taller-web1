package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Canino implements Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String tipo;
    private String sangre;
    private boolean donante;
    private boolean receptor;

    @ManyToOne
    private Usuario duenio;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getTipo() {
        return tipo;
    }

    @Override
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String getSangre() {
        return sangre;
    }

    @Override
    public void setSangre(String sangre) {
        this.sangre = sangre;
    }

    @Override
    public Usuario getDuenio() {
        return duenio;
    }

    @Override
    public void setDuenio(Usuario duenio) {
        this.duenio = duenio;
    }

    @Override
    public boolean isDonante() {
        return donante;
    }

    @Override
    public void setDonante(boolean donante) {
        this.donante = donante;
    }

    @Override
    public boolean isReceptor() {
        return receptor;
    }

    @Override
    public void setReceptor(boolean receptor) {
        this.receptor = receptor;
    }
}