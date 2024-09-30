package com.tallerwebi.dominio;

public interface Mascota {

    Long getId();

    void setId(Long id);

    String getNombre();

    void setNombre(String nombre);

    String getRaza();

    void setRaza(String raza);

    String getSangre();

    void setSangre(String sangre);

    Usuario getDuenio();

    void setDuenio(Usuario duenio);

    boolean isDonante();

    void setDonante(boolean donante);

    boolean isReceptor();

    void setReceptor(boolean receptor);

    void setAnios(Integer anios);

    Integer getAnios();

    void setPeso(Float peso);

    Float getPeso();
}