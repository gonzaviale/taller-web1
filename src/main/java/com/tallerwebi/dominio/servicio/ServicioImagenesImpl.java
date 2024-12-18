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
    public void guardarImagen(MultipartFile imagen, String nuevoNombre, Path uploadDirectory) throws IOException {
        if (!imagen.isEmpty()) {
            Path path = uploadDirectory.resolve(nuevoNombre); // Crear la ruta completa
            Files.write(path, imagen.getBytes()); // Guardar la imagen en el directorio
        }
    }

    @Override
    public void guardarExamen(MultipartFile[] imagenes, Long id) throws IOException {
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

                    guardarImagen(imagen, nuevoNombre, uploadDirectory);
                }
            }
        }
    }

    @Override
    public void guardarFotoDePerfilUsuario(MultipartFile[] imagenes, Long id) throws IOException {
        String basePath = System.getProperty("user.dir");
        Path uploadDirectory = Paths.get(basePath, "src", "main", "webapp", "resources", "images", "subidas");

        if (Files.notExists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
        }

        // quiero eliminar la anterior para que la busqueda solo me arroje una sola coincidencia
        File directorio = new File(uploadDirectory.toString());
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                //pa que no borre fotos de otros
                if (archivo.getName().startsWith(id + "_perfil")) {
                    archivo.delete();
                }
            }
        }

        for (MultipartFile imagen : imagenes) {
            if (!imagen.isEmpty()) {
                String nombreOriginal = imagen.getOriginalFilename();
                if (nombreOriginal != null) {
                    String nuevoNombre = id + "_perfil" + nombreOriginal;

                    guardarImagen(imagen, nuevoNombre, uploadDirectory);
                }
            }
        }
    }

    @Override
    public void eliminarFotoDePerfil(Long id) throws IOException {
        String basePath = System.getProperty("user.dir");
        Path uploadDirectory = Paths.get(basePath, "src", "main", "webapp", "resources", "images", "subidas");

        if (Files.notExists(uploadDirectory)) {
            Files.createDirectories(uploadDirectory);
        }

        // quiero eliminar la anterior para que la busqueda solo me arroje una sola coincidencia
        File directorio = new File(uploadDirectory.toString());
        File[] archivos = directorio.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                //pa que no borre fotos de otros
                if (archivo.getName().startsWith(id + "_perfil")) {
                    archivo.delete();
                }
            }
        }

    }

}