package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.MensajeUsuarioBanco;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.servicio.ServicioBot;
import com.tallerwebi.dominio.servicio.ServicioMensajeUsuarioBanco;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.springframework.http.HttpHeaders;

@RestController
public class FileUploadController {
    private ServicioMensajeUsuarioBanco servicioMensajeUsuarioBanco;

    @Autowired
    public FileUploadController(ServicioBot servicioBot, ServicioMensajeUsuarioBanco servicioMensajeUsuarioBanco) {
        this.servicioMensajeUsuarioBanco = servicioMensajeUsuarioBanco;
    }

    // Definir la ruta de guardado en la carpeta "uploads" al nivel del directorio del proyecto
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/";

    @PostMapping("/uploadPdf")
    public ModelAndView uploadPdf(@RequestParam("archivo") MultipartFile file,
                                  Long bankId,
                                  Long userId,
                                  HttpServletRequest request
    ) throws IOException {
        if (file.isEmpty()) {
            return new ModelAndView("home");
        }

        try {
            if (!file.getContentType().equals("application/pdf")) {
                return new ModelAndView("home");
            }

            // Crear el directorio de subida si no existe
            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Guardar el archivo en el directorio "uploads"
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.write(filePath, file.getBytes());

            // Leer el PDF para verificar (opcional)
            PDDocument document = PDDocument.load(filePath.toFile());
            int numberOfPages = document.getNumberOfPages();
            document.close();
            System.out.println(filePath.toString());
            String pdfRoute = filePath.toString();
            this.servicioMensajeUsuarioBanco.enviarArchivo("Archivo", "Usuario", (Usuario) request.getSession().getAttribute("usuarioEnSesion"), bankId, pdfRoute);
            String messageExit = "Archivo PDF subido correctamente con " + numberOfPages + " páginas y guardado en: " + filePath.toString();
            ModelMap model = new ModelMap();
            ArrayList<MensajeUsuarioBanco> messages = this.servicioMensajeUsuarioBanco.getMessagesByIds(userId, bankId);
            model.put("listMessages", messages);
            model.put("usuario", userId);
            model.put("banco", bankId);
            if(request.getSession().getAttribute("usuarioEnSesion")!=null)
                model.put("sesion", request.getSession().getAttribute("usuarioEnSesion"));
            if(request.getSession().getAttribute("idBanco")!=null)
                model.put("idBanco", request.getSession().getAttribute("idBanco"));
            return new ModelAndView("chattUsers", model);
        } catch (IOException e) {
            e.printStackTrace();
            return new ModelAndView("home");
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView("home");
        }
    }

    @GetMapping("/downloadPdf")
    public ResponseEntity<InputStreamResource> downloadPdf(@RequestParam("fileName") String fileName) {
        try {
            // Extraer solo el nombre del archivo para evitar problemas de seguridad
            String safeFileName = Paths.get(fileName).getFileName().toString();

            // Crear referencia al archivo dentro del directorio seguro
            File file = new File(UPLOAD_DIRECTORY + safeFileName);

            // Verificar si el archivo existe
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            // Preparar el archivo para la descarga
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamResource resource = new InputStreamResource(fileInputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + safeFileName);
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
