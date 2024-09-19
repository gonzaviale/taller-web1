package com.tallerwebi.dominio;

import javax.persistence.*;

@Entity
public class Publicacion {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String descripcion;
    private Boolean puedeMovilizarse;
    private String tipoDeSangre;
    private String zonaDeResidencia;
    private Boolean estaActiva;
    private String tipoDePublicacion;
    //private Mascota mascota;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getPuedeMovilizarse() {
        return puedeMovilizarse;
    }

    public void movilzarse() {
        this.puedeMovilizarse = true;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean getEstaActiva() {
        return estaActiva;
    }

    public void activar() {
        this.estaActiva = true;
    }

    public String getTipoDeSangre() {
        return tipoDeSangre;
    }

    public void setTipoDeSangre(String tipoDeSangre) {
        this.tipoDeSangre = tipoDeSangre;
    }

    public String getZonaDeResidencia() {
        return zonaDeResidencia;
    }

    public void setZonaDeResidencia(String zonaDeResidencia) {
        this.zonaDeResidencia = zonaDeResidencia;
    }

    public String getTipoDePublicacion() {
        return tipoDePublicacion;
    }

    public void setTipoDePublicacion(String tipoDePublicacion) {
        this.tipoDePublicacion = tipoDePublicacion;
    }
}
