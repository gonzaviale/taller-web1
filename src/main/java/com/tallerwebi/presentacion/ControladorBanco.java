package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.PaqueteDeSangre;
import com.tallerwebi.dominio.servicio.ServicioBanco;
import com.tallerwebi.dominio.entidad.Solicitud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ControladorBanco {
    private final ServicioBanco servicioBanco;

    @Autowired
    public ControladorBanco(ServicioBanco servicioBanco) {
        this.servicioBanco = servicioBanco;
    }


    @RequestMapping("/bancoHome")
    public ModelAndView BancoHome(HttpSession session,
                                  @RequestParam(value = "error", required = false) String error) {

        if (!verificarSesion(session)) {
            return new ModelAndView("redirect:/login");
        }

        Long idBanco = (Long) session.getAttribute("idBanco");
        ModelMap modelo = new ModelMap();
        Banco banco = servicioBanco.BuscarBancoId(idBanco);
        modelo.addAttribute("nombreBanco", banco.getNombreBanco());
        modelo.addAttribute("idBanco", idBanco);

        if (error != null && !error.isEmpty()) {
            modelo.addAttribute("error", error);
        }

        return new ModelAndView("bancoHome", modelo);
    }

    @RequestMapping("/verStock")
    public ModelAndView VerStock(HttpSession session) {
        if (!verificarSesion(session)) {
            return new ModelAndView("redirect:/login");
        }

        Long idBanco = (Long) session.getAttribute("idBanco");
        Banco banco = servicioBanco.BuscarBancoId(idBanco);

        ModelMap modelo = new ModelMap();
        modelo.put("datosBanco", new Banco());
        List<PaqueteDeSangre> paquetes = servicioBanco.obtenerPaquetesDeSangrePorBanco(idBanco);
        modelo.addAttribute("paquetes", paquetes);
        return new ModelAndView("bancoVerStock", modelo);
    }


    @RequestMapping("/agregarPaquete")
    public String agregarPaqueteDeSangre(HttpSession session,
                                         @RequestParam("tipoSangre") String tipoSangre,
                                         @RequestParam("cantidad") int cantidad,
                                         @RequestParam("tipoProducto") String tipoProducto

    ) {

        Long idBanco = (Long) session.getAttribute("idBanco");

        Banco banco = servicioBanco.BuscarBancoId(idBanco);
        PaqueteDeSangre paquete = new PaqueteDeSangre(tipoSangre, cantidad, tipoProducto, banco);
        servicioBanco.agregarPaqueteDeSangre(paquete, banco);
        String errorMessage = "Paquete de sangre agregado con exito";
        return "redirect:/bancoHome?success=" + errorMessage;

    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    private boolean verificarSesion(HttpSession session) {
        Long idBanco = (Long) session.getAttribute("idBanco");
        return idBanco != null;
    }


}
