package com.tallerwebi.presentacion.DTO;

public class UsuarioFiltradoDTO {

    private Integer cantidadDeMascotas; // Número total de mascotas
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private Integer cantidadPublicacionesBusqueda;
    private Integer cantidadPublicacionesDonacion;
    private Integer cantidadPublicacionesVenta;
    private Integer cantidadMascotaReceptora;
    private Integer cantidadMascotaDonadora;

    public UsuarioFiltradoDTO(){

    }

    // Constructor para publicaciones
    public UsuarioFiltradoDTO(Long id, String nombre, String apellido, String email, Integer cantidadPublicacionesBusqueda, Integer cantidadPublicacionesDonacion, Integer cantidadPublicacionesVenta) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.cantidadPublicacionesBusqueda = cantidadPublicacionesBusqueda;
        this.cantidadPublicacionesDonacion = cantidadPublicacionesDonacion;
        this.cantidadPublicacionesVenta = cantidadPublicacionesVenta;
    }

    // Constructor para mascotas
    public UsuarioFiltradoDTO(Long id, String nombre, String apellido, String email, Integer cantidadMascotaReceptora, Integer cantidadMascotaDonadora) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.cantidadMascotaReceptora = cantidadMascotaReceptora;
        this.cantidadMascotaDonadora = cantidadMascotaDonadora;
    }

    // Constructor para total de mascotas
    public UsuarioFiltradoDTO(Long id, String nombre, String apellido, String email, Integer cantidadDeMascotas) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.cantidadDeMascotas = cantidadDeMascotas;
    }

    public Integer getCantidadDeMascotas() {
        return cantidadDeMascotas;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public Integer getCantidadPublicacionesBusqueda() {
        return cantidadPublicacionesBusqueda;
    }

    public Integer getCantidadPublicacionesDonacion() {
        return cantidadPublicacionesDonacion;
    }

    public Integer getCantidadMascotaReceptora() {
        return cantidadMascotaReceptora;
    }

    public Integer getCantidadPublicacionesVenta() {
        return cantidadPublicacionesVenta;
    }

    public Integer getCantidadMascotaDonadora() {
        return cantidadMascotaDonadora;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCantidadDeMascotas(Integer cantidadDeMascotas) {
        this.cantidadDeMascotas = cantidadDeMascotas;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCantidadPublicacionesBusqueda(Integer cantidadPublicacionesBusqueda) {
        this.cantidadPublicacionesBusqueda = cantidadPublicacionesBusqueda;
    }

    public void setCantidadPublicacionesDonacion(Integer cantidadPublicacionesDonacion) {
        this.cantidadPublicacionesDonacion = cantidadPublicacionesDonacion;
    }

    public void setCantidadPublicacionesVenta(Integer cantidadPublicacionesVenta) {
        this.cantidadPublicacionesVenta = cantidadPublicacionesVenta;
    }

    public void setCantidadMascotaReceptora(Integer cantidadMascotaReceptora) {
        this.cantidadMascotaReceptora = cantidadMascotaReceptora;
    }

    public void setCantidadMascotaDonadora(Integer cantidadMascotaDonadora) {
        this.cantidadMascotaDonadora = cantidadMascotaDonadora;
    }
}
