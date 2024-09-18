package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.Banco;
import com.tallerwebi.dominio.PaqueteDeSangre;
import com.tallerwebi.dominio.ServicioBanco;
import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.excepcion.BancoNoEncontrado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class ControladorBanco {

    private ServicioBanco servicioBanco;


    @Autowired
    public ControladorBanco( ServicioBanco servicioBanco) {
        this.servicioBanco = servicioBanco;
    }




    @RequestMapping("/BancoHome")
    public ModelAndView BancoHome(@RequestParam(value = "error", required = false) String error) {
        ModelMap modelo = new ModelMap();
        modelo.put("datosBanco", new Banco());
        modelo.addAttribute("idBanco", 0);

        // Añadir el mensaje de error al modelo si está presente
        if (error != null && !error.isEmpty()) {
            modelo.addAttribute("error", error);
        }

        return new ModelAndView("BancoHome", modelo);
    }





    @RequestMapping("/VerStock")
    public ModelAndView VerStock() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosBanco", new Banco());
        return new ModelAndView("BancoVerStock", modelo);
    }
    @RequestMapping("/VerPeticiones")
    public ModelAndView BancoVerPeticiones() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosBanco", new Banco());
        return new ModelAndView("BancoVerPeticiones", modelo);
    }


    @RequestMapping("/agregarPaquete")
    public String agregarPaqueteDeSangre(@RequestParam("idBanco") Long idBanco,
                                         @RequestParam("tipoSangre") String tipoSangre,
                                         @RequestParam("cantidad") int cantidad,
                                         RedirectAttributes redirectAttributes) {
        try {
            Banco banco = new Banco();
            PaqueteDeSangre paquete = new PaqueteDeSangre(tipoSangre, cantidad, banco);
            servicioBanco.agregarPaqueteDeSangre(idBanco, paquete);
            return "redirect:/BancoHome?success=" + URLEncoder.encode("Paquete de sangre agregado con éxito", StandardCharsets.UTF_8);

        } catch (BancoNoEncontrado e) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Banco no Registrado inicia session";
            // Redirige a BancoHome con un parámetro de error
            return "redirect:/BancoHome?error=" + URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        }
    }










    }




