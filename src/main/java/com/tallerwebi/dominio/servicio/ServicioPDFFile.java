package com.tallerwebi.dominio.servicio;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ServicioPDFFile {


    void guardarPdf(MultipartFile file,String prefijo) throws IOException;

    String obtenerNombreArchivoPorPrefijo(String prefijo);
}
