package com.tallerwebi.presentacion;


import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Campana;
import com.tallerwebi.dominio.servicio.ServicioBanco;
import com.tallerwebi.dominio.servicio.ServicioCampania;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ControladorCampanias {

    private final ServicioCampania servicioCampania;
    private final ServicioBanco servicioBanco;

    @Autowired
    public ControladorCampanias(ServicioCampania servicioCampania ,ServicioBanco servicioBanco) {
        this.servicioCampania = servicioCampania;
        this.servicioBanco = servicioBanco;
    }


    @RequestMapping(value = "/crearCampania", method = RequestMethod.GET)
    public ModelAndView mostrarFormularioCrearCampania(HttpSession session,
                                                       @RequestParam(value = "error", required = false) String error) {
        Long idBanco = (Long) session.getAttribute("idBanco");
        ModelMap modelo = new ModelMap();
        if (idBanco == null) {
            return new ModelAndView("redirect:/login");
        }
        modelo.put("ubicacion",servicioBanco.BuscarBancoId(idBanco).getDireccion());
        modelo.put("error", error);
        return new ModelAndView("crearCampania", modelo);
    }
    @Transactional
    @PostMapping("/crearcamp")
    public String crearCampania(HttpSession session,
                                @RequestParam("nombre") String nombre,
                                @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
                                @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                                @RequestParam("ubicacion") String ubicacion,
                                @RequestParam("descripcion") String descripcion,
                                RedirectAttributes redirectAttributes) {


        Long idBanco = (Long) session.getAttribute("idBanco");
        Banco banco = servicioBanco.BuscarBancoId(idBanco);

        if (banco == null) {
            redirectAttributes.addFlashAttribute("error", "Banco no encontrado");
            return "redirect:/crearCampania";
        }
        Campana nuevaCampana = new Campana();
        nuevaCampana.setNombre(nombre);
        nuevaCampana.setFechaInicio(fechaInicio);
        nuevaCampana.setFechaFin(fechaFin);
        nuevaCampana.setUbicacion(ubicacion);
        nuevaCampana.setDescripcion(descripcion);
        nuevaCampana.setBanco(banco);
        banco.agregarCampania(nuevaCampana);
        servicioCampania.guardarCampania(nuevaCampana,banco);

        redirectAttributes.addFlashAttribute("success", "Campaña creada con éxito");
        return "redirect:/bancoHome";
    }

    @Transactional
    @RequestMapping(value = "/misCampanias", method = RequestMethod.GET)
    public ModelAndView mostrarMisCampanias(HttpSession session) {
        Long idBanco = (Long) session.getAttribute("idBanco");
        if (idBanco == null) {
            return new ModelAndView("redirect:/login");
        }
        Banco banco = servicioBanco.BuscarBancoId(idBanco);

        ModelMap modelo = new ModelMap();
        List<Campana> campanias = servicioCampania.obtenerCampaniasPorBanco(idBanco);
        modelo.put("campanias", campanias);

        return new ModelAndView("bancoMisCampanias", modelo);
    }


}




