package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Canino;
import com.tallerwebi.dominio.entidad.Felino;
import com.tallerwebi.dominio.entidad.Mascota;
import com.tallerwebi.dominio.servicio.ServicioMascota;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ControladorAgregarMascota {

    private final ServicioMascota servicioMascota;

    @Autowired
    public ControladorAgregarMascota(ServicioMascota servicioMascota) {
        this.servicioMascota = servicioMascota;
    }

    //requestmapping maneja las dos tipos de solicitudes dejalo ser
    @RequestMapping(path = "/agregar-mascota-donante")
    public ModelAndView formularioDonante() {
        //no puedo instanciar un objeto mascota ya que no deberia de tener instancias de tipo animal mascota
        return new ModelAndView("agregar-mascota-donante");
    }

    @PostMapping("/agregar-donante")
    public ModelAndView agregarDonante(            @RequestParam("nombre") String nombre,
                                                   @RequestParam("anios") int anios,
                                                   @RequestParam("peso") float peso,
                                                   @RequestParam("tipo") String tipo) {


        //Creamo mascota

        Mascota mascota;
        //la seteamos dependiendo del tipo
        if(tipo.equals("Felino")){
            mascota=new Felino();
            mascota.setNombre(nombre);
            mascota.setAnios(anios);
            mascota.setPeso(peso);
            mascota.setTipo(tipo);
            mascota.setDonante(true);
            mascota.setRevision(true);
            mascota.setAprobado(false);
            mascota.setRechazado(false);

            servicioMascota.registrarMascota(mascota);
        }
        if(tipo.equals("Canino")){
            mascota=new Canino();
            mascota.setNombre(nombre);
            mascota.setAnios(anios);
            mascota.setPeso(peso);
            mascota.setTipo(tipo);
            mascota.setDonante(true);
            mascota.setRevision(true);
            mascota.setAprobado(false);
            mascota.setRechazado(false);

            servicioMascota.registrarMascota(mascota);
        }

        return new ModelAndView("redirect:/home");
    }

}