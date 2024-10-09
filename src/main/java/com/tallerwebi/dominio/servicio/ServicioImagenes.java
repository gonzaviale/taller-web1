package com.tallerwebi.dominio.servicio;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ServicioImagenes {
    List<String> obtenerImagenesPorUsuario(Long idMascota);
    public void guardarImagenes(MultipartFile[] imagenes, Long id) throws IOException;
}
