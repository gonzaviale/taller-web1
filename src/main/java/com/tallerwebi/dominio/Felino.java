package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Felino implements Mascota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String raza;
    private String sangre;
    private boolean donante;
    private boolean receptor;
    private Integer anios;
    private Float peso;

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
    public String getRaza() {
        return raza;
    }

    @Override
    public void setRaza(String raza) {
        this.raza = raza;
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

    @Override
    public void setAnios(Integer anios) {this.anios = anios;}

    @Override
    public Integer getAnios() {return this.anios;}

    @Override
    public void setPeso(Float peso) {this.peso = peso;}

    @Override
    public Float getPeso() {return this.peso;}
}
