package com.tallerwebi.dominio.servicio;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service("servicioImagenes")
public class ServicioImagenesImpl implements ServicioImagenes {

    @Override
    public List<String> obtenerImagenesPorUsuario(Long idMascota) {
        String uploadDirectory = Paths.get(System.getProperty("user.dir"), "src", "main", "webapp", "resources", "images", "subidas").toString();

        File directorio = new File(uploadDirectory);
        File[] archivos = directorio.listFiles();

        List<String> imagenesDelUsuario = new ArrayList<>();

        if (archivos != null) {
            for (File archivo : archivos) {
                if (archivo.getName().startsWith(idMascota.toString() + "_")) {
                    imagenesDelUsuario.add(archivo.getName());
                }
            }

        }
        return imagenesDelUsuario;
    }

    @Override
    public void guardarImagenes(MultipartFile[] imagenes, Long id) throws IOException {
        String basePath = System.getProperty("user.dir");

        Path uploadDirectory = Paths.get(basePath, "src", "main", "webapp", "resources", "images", "subidas");

        if (Files.notExists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
        }

        for (MultipartFile imagen : imagenes) {
            if (!imagen.isEmpty()) {
                String nombreOriginal = imagen.getOriginalFilename();

                if (nombreOriginal != null) {
                    String nuevoNombre = id + "_" + nombreOriginal;

                    Path path = uploadDirectory.resolve(nuevoNombre);

                    Files.write(path, imagen.getBytes());
                }
            }
        }
    }
}
