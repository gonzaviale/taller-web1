package com.tallerwebi.dominio.servicio;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ServicioImagenes {
    List<String> obtenerImagenesPorUsuario(Long idMascota);
    public void guardarImagen(MultipartFile imagen, String nuevoNombre, Path uploadDirectory) throws IOException;
    public void guardarExamen(MultipartFile[] imagenes, Long id) throws IOException;
}
