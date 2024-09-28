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
import java.util.List;

@Controller
public class ControladorBanco {

    private final ServicioBanco servicioBanco;


    @Autowired
    public ControladorBanco(ServicioBanco servicioBanco) {
        this.servicioBanco = servicioBanco;
    }


    @RequestMapping("/BancoHome")
    public ModelAndView BancoHome(HttpSession session,
                                  @RequestParam(value = "error", required = false) String error) {

        if (!verificarSesion(session)) {
            return new ModelAndView("redirect:/login");  // Redirigir a la página de login si no hay sesión
        }

        ModelMap modelo = new ModelMap();
        Long idBanco = (Long) session.getAttribute("idBanco");

        if (idBanco != null) {

            Banco banco = servicioBanco.BuscarBancoId(idBanco);

            if (banco != null) {

                modelo.addAttribute("nombreBanco", banco.getNombreBanco());
                modelo.addAttribute("idBanco", idBanco);
            } else {

                modelo.addAttribute("error", "Banco no encontrado.");
            }
        } else {

            modelo.addAttribute("error", "ID de banco no encontrado en la sesión.");
        }


        if (error != null && !error.isEmpty()) {
            modelo.addAttribute("error", error);
        }

        return new ModelAndView("BancoHome", modelo);
    }


    @RequestMapping("/VerStock")
    public ModelAndView VerStock(HttpSession session) {
        if (!verificarSesion(session)) {
            return new ModelAndView("redirect:/login");  // Redirigir a la página de login si no hay sesión
        }

        Long idBanco = (Long) session.getAttribute("idBanco");
        Banco banco = servicioBanco.BuscarBancoId(idBanco);

        ModelMap modelo = new ModelMap();
        modelo.put("datosBanco", new Banco());
        List<PaqueteDeSangre> paquetes = servicioBanco.obtenerPaquetesDeSangrePorBanco(idBanco);
        modelo.addAttribute("paquetes", paquetes);
        return new ModelAndView("BancoVerStock", modelo);
    }



//TODO
    @RequestMapping("/VerPeticiones")
    public ModelAndView BancoVerPeticiones(HttpSession session) {
        if (!verificarSesion(session)) {
            return new ModelAndView("redirect:/login");  // Redirigir a la página de login si no hay sesión
        }


        ModelMap modelo = new ModelMap();
        modelo.put("datosBanco", new Banco());
        return new ModelAndView("BancoVerPeticiones", modelo);
    }


    @RequestMapping("/agregarPaquete")
    public String agregarPaqueteDeSangre(HttpSession session,
                                         @RequestParam("tipoSangre") String tipoSangre,
                                         @RequestParam("cantidad") int cantidad,
                                         @RequestParam("tipoProducto") String tipoProducto

                                         ) {
        try {
            Long idBanco = (Long) session.getAttribute("idBanco");

            Banco banco = servicioBanco.BuscarBancoId(idBanco);
            PaqueteDeSangre paquete = new PaqueteDeSangre(tipoSangre, cantidad,tipoProducto, banco);
            servicioBanco.agregarPaqueteDeSangre(paquete,banco);
            String errorMessage = "Paquete de sangre agregado con éxito";
            return "redirect:/BancoHome?success=" +
                    URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        } catch (BancoNoEncontrado e) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Banco no Registrado inicia sesión";
            return "redirect:/BancoHome?error=" +
                    URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        }
    }

    private boolean verificarSesion(HttpSession session) {
        Long idBanco = (Long) session.getAttribute("idBanco");
        return idBanco != null;
    }




    @RequestMapping("/loginsimulado")
    public String buscarBancoConIdCero(HttpSession session) {
        Banco banco = new Banco("Banco Test", "Ciudad", "Dirección", "email@test.com", "9-18", "País", "12345", "123456789");
        servicioBanco.agregarBanco(banco);

        session.setAttribute("idBanco", banco.getId());

        return "redirect:/BancoHome";
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // Invalida toda la sesión, eliminando todos los atributos
        return "redirect:/login";  // Redirige al login con un mensaje de logout exitoso
    }



}












