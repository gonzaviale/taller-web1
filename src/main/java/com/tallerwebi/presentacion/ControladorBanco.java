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

    @RequestMapping("/verPeticiones")
    public ModelAndView BancoVerPeticiones(HttpSession session) {
        if (!verificarSesion(session)) {
            return new ModelAndView("redirect:/login");  
        }
        Long idBanco = (Long) session.getAttribute("idBanco");

        ModelMap modelo = new ModelMap();

       List<Solicitud>solicitudes = servicioBanco.obtenerSolicitudesXBanco(idBanco);
        modelo.addAttribute("solicitudes", solicitudes);
        modelo.put("datosBanco", new Banco());
        return new ModelAndView("bancoVerPeticiones", modelo);
    }

    @RequestMapping("/revisarSolicitud")
    public ModelAndView BancoVerPeticion(@RequestParam("solicitudId") int solicitudId, HttpSession session) {
        if (!verificarSesion(session)) {
            return new ModelAndView("redirect:/login");  
        }
        Long idBanco = (Long) session.getAttribute("idBanco");
        ModelMap modelo = new ModelMap();

        Solicitud solicitud = servicioBanco.buscarSolicitud(solicitudId);
        List<PaqueteDeSangre> paquetes = servicioBanco.obtenerPaquetesDeSangreCompatibles(solicitud);

        modelo.addAttribute("solicitud", solicitud);
        modelo.addAttribute("paquetes", paquetes);
        modelo.put("datosBanco", new Banco());
        return new ModelAndView("bancoVerSolicitud", modelo);
    }


    @RequestMapping(value = "/rechazarSolicitud", method = RequestMethod.POST)
    public String rechazarSolicitud(@RequestParam("solicitudId") int solicitudId, HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }

         servicioBanco.rechazarSolicitud(solicitudId);

            return "redirect:/verPeticiones";

    }

    @RequestMapping(value = "/asignarPaquete", method = RequestMethod.POST)
    public String asignarPaquete(@RequestParam("solicitudId") int solicitudId,
                                 @RequestParam("paqueteId") int paqueteId,
                                 HttpSession session) {
        if (!verificarSesion(session)) {
            return "redirect:/login";
        }
        servicioBanco.asignarPaqueteASolicitud(solicitudId, paqueteId);

        return "redirect:/verPeticiones";
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


    @RequestMapping("/loginsimulado")
    public String buscarBancoConIdCero(HttpSession session) {
        Banco banco = new Banco("Banco Test",  "Dirección","Ciudad","País",  "123456789","email@test.com", "9-18",  "12345");
        servicioBanco.agregarBanco(banco);
        session.setAttribute("idBanco", banco.getId());
        Solicitud solicitud1 = new Solicitud(banco.getId(), 1L, "Plasma fresco congelado", "DEA 1.1+", 300);
        Solicitud solicitud2 = new Solicitud(banco.getId(), 2L, "Globulos rojos empaquetados", "DEA 1.1-", 200);
        Solicitud solicitud3 = new Solicitud(banco.getId(), 3L, "Sangre total", "DEA 1.2+", 500);
        Solicitud solicitud4 = new Solicitud(banco.getId(), 4L, "Plaquetas", "DEA 4-", 400);

        // Agregar las solicitudes al servicio de solicitudes
        servicioBanco.agregarSolicitud(solicitud1);
        servicioBanco.agregarSolicitud(solicitud2);
        servicioBanco.agregarSolicitud(solicitud3);
        servicioBanco.agregarSolicitud(solicitud4);


        return "redirect:/bancoHome";
    }


    private boolean verificarSesion(HttpSession session) {
        Long idBanco = (Long) session.getAttribute("idBanco");
        return idBanco != null;
    }

}
