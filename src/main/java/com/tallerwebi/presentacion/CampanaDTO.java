package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Banco;

import java.time.LocalDate;

public class CampanaDTO {

    private Long id;
    private String nombre;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String ubicacion;
    private String descripcion;
    private Banco banco;

    // Constructor vacío
    public CampanaDTO() {
    }

    // Constructor con parámetros
    public CampanaDTO(String nombre, LocalDate fechaInicio, LocalDate fechaFin, String ubicacion, String descripcion) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
    }

    // Getters y Setters

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

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setBanco(Banco banco) {
        this.banco=banco;
    }
}
