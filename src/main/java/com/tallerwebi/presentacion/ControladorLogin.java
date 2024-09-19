package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.ServicioLogin;
import com.tallerwebi.dominio.Usuario;
import com.tallerwebi.dominio.Banco;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ControladorLogin {

    private ServicioLogin servicioLogin;
    private ModelMap modelo = new ModelMap();

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin) {
        this.servicioLogin = servicioLogin;
    }

    @RequestMapping("/login")
    public ModelAndView irALogin() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLogin());
        return new ModelAndView("login", modelo);
    }

    @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
    public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLogin datosLogin, HttpServletRequest request) {


        Usuario usuarioBuscado = servicioLogin.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword());
        if (usuarioBuscado != null) {
            request.getSession().setAttribute("ROL", usuarioBuscado.getRol());
            return new ModelAndView("redirect:/home");
        } else {
            modelo.put("error", "Usuario o clave incorrecta");
        }
        return new ModelAndView("login", modelo);
    }

    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("usuario") Usuario usuario,
                                    @RequestParam("confirmPassword") String confirmPassword) {
        modelo.clear();

        if (nombreEstaVacio(usuario.getNombre())) {
            modelo.put("usuario", usuario);
            return new ModelAndView("nuevo-usuario", modelo);

        }

        if (apellidoEstaVacio(usuario.getApellido())) {
            modelo.put("usuario", usuario);
            return new ModelAndView("nuevo-usuario", modelo);
        }

        if (emailEstaVacio(usuario.getEmail())) {
            modelo.put("usuario", usuario);
            return new ModelAndView("nuevo-usuario", modelo);
        }

        if (rolEstaVacio(usuario.getRol())) {
            modelo.put("usuario", usuario);
            return new ModelAndView("nuevo-usuario", modelo);
        }

        if (passwordEstaVacia(usuario.getPassword(), confirmPassword)) {
            modelo.put("usuario", usuario);
            return new ModelAndView("nuevo-usuario", modelo);
        }

        if (passwordsDiferentes(usuario.getPassword(), confirmPassword)) {
            modelo.put("usuario", usuario);
            return new ModelAndView("nuevo-usuario", modelo);
        }

        try {
            servicioLogin.registrar(usuario);
        } catch (UsuarioExistente e) {
            modelo.put("error", "El usuario ya existe");
            return new ModelAndView("nuevo-usuario", modelo);
        } catch (Exception e) {
            modelo.put("error", "Error al registrar el nuevo usuario");
            return new ModelAndView("nuevo-usuario", modelo);
        }

        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(path = "/nuevo-usuario", method = RequestMethod.GET)
    public ModelAndView nuevoUsuario() {
        ModelMap model = new ModelMap();
        model.put("usuario", new Usuario());
        return new ModelAndView("nuevo-usuario", model);
    }

    @RequestMapping("/registroBanco")
    public ModelAndView RegistroBanco() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosBanco", new Banco());
        return new ModelAndView("registroBanco", modelo);
    }

    @RequestMapping(path = "/registrarBanco", method = RequestMethod.POST)
    public ModelAndView registrarBanco(@ModelAttribute("banco") Banco banco) {
        ModelMap model = new ModelMap();
        try {
            servicioLogin.registrarBanco(banco);  // Cambiar a servicio que maneje bancos

        } catch (Exception e) {
            model.put("error", "Error al registrar el banco");
            return new ModelAndView("registroBanco", model);  // Volver a la vista de registro si hay error
        }
        return new ModelAndView("redirect:/login");  // Redirigir a la página de login u otra según necesidad
    }


    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public ModelAndView irAHome() {
        return new ModelAndView("home");
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public ModelAndView inicio() {
        return new ModelAndView("redirect:/login");
    }

    private Boolean apellidoEstaVacio(String apellido) {
        if (apellido.isEmpty()) {
            modelo.put("errorApellido", "El campo apellido es obligatorio");
            return true;
        }
        return false;
    }

    private Boolean nombreEstaVacio(String nombre) {
        if (nombre.isEmpty()) {
            modelo.put("errorNombre", "El campo nombre es obligatorio");
            return true;
        }
        return false;
    }

    private Boolean emailEstaVacio(String email) {
        if (email == null || email.isEmpty()) {
            modelo.put("errorEmail", "El campo email es obligatorio");
            return true;
        }
        return false;
    }

    private Boolean passwordEstaVacia(String contrasena1, String contrasena2) {
        if (contrasena1.isEmpty() || contrasena2.isEmpty()) {
            modelo.put("errorPassword", "El campo contraseña es obligatorio");
            return true;
        }
        return false;
    }

    private Boolean passwordsDiferentes(String password1, String password2) {
        if (!password1.equals(password2)) {
            modelo.put("errorPasswordsDistintas", "Las contraseñas deben ser iguales");
            return true;
        }
        return false;
    }

    private Boolean rolEstaVacio(String rol) {
        if (rol == null || rol.isEmpty()) {
            modelo.put("errorRol", "Debe ingresar un rol");
            return true;
        }
        return false;
    }
}