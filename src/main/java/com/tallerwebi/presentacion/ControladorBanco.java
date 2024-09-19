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

    private final ServicioBanco servicioBanco;


    @Autowired
    public ControladorBanco(ServicioBanco servicioBanco) {
        this.servicioBanco = servicioBanco;
    }


    @RequestMapping("/BancoHome")
    public ModelAndView BancoHome(@RequestParam(value = "idBanco") Integer idBanco,
                                  @RequestParam(value = "error", required = false) String error) {
        ModelMap modelo = new ModelMap();
        Banco banco = servicioBanco.BuscarBancoId(1L);

        modelo.addAttribute("nombreBanco", banco.getNombreBanco());
        modelo.addAttribute("idBanco", idBanco );


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
                                         @RequestParam("cantidad") int cantidad
                                         ) {
        try {
            Banco banco = servicioBanco.BuscarBancoId(idBanco);
            PaqueteDeSangre paquete = new PaqueteDeSangre(tipoSangre, cantidad, banco);
            servicioBanco.agregarPaqueteDeSangre(idBanco, paquete);
            return "redirect:/BancoHome?idBanco=" + idBanco + "&success=" +
                    URLEncoder.encode("Paquete de sangre agregado con éxito", StandardCharsets.UTF_8);
        }

        catch (BancoNoEncontrado e) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Banco no Registrado inicia session";
            return "redirect:/BancoHome?idBanco=" + idBanco + "&error=" +
                    URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        }
    }

    @RequestMapping("/buscarBancoConIdCero")
    public String buscarBancoConIdCero() {
        // Lógica para buscar el banco con id = 0 (o alguna validación)
        Banco banco = servicioBanco.BuscarBancoId(1L);

        if (banco != null) {
            // Redirige a la página BancoHome con el idBanco = 0
            return "redirect:/BancoHome?idBanco=0";


        }
        return "";
    }
}












