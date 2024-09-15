package com.tallerwebi.presentacion;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ControladorRegistroUsuario {
    private ModelMap modelo = new ModelMap();

    public ModelAndView registrar(String email, String contrasena1, String contrasena2) {
        if (emailEstaVacio(email) || contrasenaEstaVacia(contrasena1, contrasena2) || contrasenasDiferentes(contrasena1, contrasena2)) {
            return new ModelAndView("registro", modelo);
        } else {
            return new ModelAndView("redirect:/login");
        }
    }

    private Boolean emailEstaVacio(String email) {
        if (email.isEmpty()) {
            modelo.put("error email vacio", "El campo de email es obligatorio");
            return true;
        }
        return false;
    }

    private Boolean contrasenaEstaVacia(String contrasena1, String contrasena2) {
        if (contrasena1.isEmpty() || contrasena2.isEmpty()) {
            modelo.put("error contraseña vacia", "El campo de contraseña es obligatorio");
            return true;
        }
        return false;
    }

    private boolean contrasenasDiferentes(String contrasena1, String contrasena2) {
        if (!contrasena1.equals(contrasena2)) {
            modelo.put("error contraseñas diferentes", "Las contraseñas deben ser iguales");
            return true;
        }
        return false;
    }
}