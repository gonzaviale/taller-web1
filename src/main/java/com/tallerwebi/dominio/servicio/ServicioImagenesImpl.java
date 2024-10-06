package com.tallerwebi.dominio.servicio;

import org.springframework.stereotype.Service;

import java.io.File;
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
}
