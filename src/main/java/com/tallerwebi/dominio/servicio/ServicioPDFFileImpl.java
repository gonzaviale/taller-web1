package com.tallerwebi.dominio.servicio;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;


@Service
public class ServicioPDFFileImpl implements ServicioPDFFile {

    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/";

    @Override
    public void guardarPdf(MultipartFile file,String prefijo) throws IOException {

        //le dejo un prefijo para que no se pisen si otro quiere subir pdfs con esta logica
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío.");
        }

        if (!file.getContentType().equals("application/pdf")) {
            throw new IllegalArgumentException("El archivo no es un PDF válido.");
        }

        Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = file.getOriginalFilename();
        Path filePath = uploadPath.resolve(prefijo + fileName);
        Files.write(filePath, file.getBytes());

    }

    @Override
    public String obtenerNombreArchivoPorPrefijo(String prefijo) {
        // Obtener el directorio de uploads
        File folder = new File(UPLOAD_DIRECTORY);

        // Listar los archivos en el directorio
        File[] files = folder.listFiles((dir, name) -> name.startsWith(prefijo));

        if (files != null && files.length > 0) {
            // Si se encuentran archivos que coinciden con el prefijo, devolver el primer archivo encontrado
            return files[0].getName();  // El nombre completo del archivo
        } else {
            return null;
        }
    }


}
