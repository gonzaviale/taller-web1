package com.tallerwebi.dominio;

public interface Mascota {

    Long getId();

    void setId(Long id);

    String getNombre();

    void setNombre(String nombre);

    String getTipo();

    void setTipo(String tipo);

    String getSangre();

    void setSangre(String sangre);

    Usuario getDuenio();

    void setDuenio(Usuario duenio);

    boolean isDonante();

    void setDonante(boolean donante);

    boolean isReceptor();

    void setReceptor(boolean receptor);
}