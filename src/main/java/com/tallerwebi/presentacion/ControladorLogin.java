package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.DuenoMascota;
import com.tallerwebi.dominio.entidad.Veterinario;
import com.tallerwebi.dominio.servicio.ServicioImagenes;
import com.tallerwebi.dominio.servicio.ServicioLogin;
import com.tallerwebi.dominio.entidad.Usuario;
import com.tallerwebi.dominio.excepcion.UsuarioExistente;
import com.tallerwebi.dominio.servicio.ServicioPDFFile;
import com.tallerwebi.presentacion.DTO.DatosLoginDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class ControladorLogin {

    private final ServicioLogin servicioLogin;
    final private ModelMap modelo = new ModelMap();
    private final ServicioImagenes servicioImagenes;
    private final ServicioPDFFile servicioPDFFile;

    @Autowired
    public ControladorLogin(ServicioLogin servicioLogin,ServicioImagenes servicioImagenes,ServicioPDFFile servicioPDFFile) {
        this.servicioLogin = servicioLogin;
        this.servicioImagenes= servicioImagenes;
        this.servicioPDFFile=servicioPDFFile;

    }

    @RequestMapping("/login")
    public ModelAndView irALogin() {

        ModelMap modelo = new ModelMap();
        modelo.put("datosLogin", new DatosLoginDTO());
        return new ModelAndView("login", modelo);
    }

    @RequestMapping(path = "/validar-login", method = RequestMethod.POST)
    public ModelAndView validarLogin(@ModelAttribute("datosLogin") DatosLoginDTO datosLogin, HttpServletRequest request) {

        Usuario usuarioBuscado = servicioLogin.consultarUsuario(datosLogin.getEmail(), datosLogin.getPassword());

        if(usuarioBuscado!=null && usuarioBuscado.getRol().equals("administrador")){
            request.getSession().setAttribute("ROL", usuarioBuscado.getRol());
            request.getSession().setAttribute("administrador", usuarioBuscado);
            return new ModelAndView("redirect:/administrador");
        }

        if(usuarioBuscado!=null && usuarioBuscado.getRol().equals("veterinario") && !usuarioBuscado.getEstado().equals("activo")){
            modelo.put("error", "Usuario no verificado");

            return new ModelAndView("login", modelo);
        }

        if (usuarioBuscado != null) {
            request.getSession().setAttribute("ROL", usuarioBuscado.getRol());
            request.getSession().setAttribute("usuarioEnSesion", usuarioBuscado);
            request.getSession().setAttribute("usuarioId",usuarioBuscado.getId());
            return new ModelAndView("redirect:/home");
        }

        Banco banco = servicioLogin.ConsultarBanco(datosLogin.getEmail(), datosLogin.getPassword());

        if(banco!= null) {
            request.getSession().setAttribute("idBanco",banco.getId());
            request.getSession().setAttribute("ROL","banco");
            return new ModelAndView("redirect:/bancoHome");
        }

        modelo.put("error", "Usuario o clave incorrecta");

        return new ModelAndView("login", modelo);
    }

    @RequestMapping(path = "/registrarme", method = RequestMethod.POST)
    public ModelAndView registrarme(@ModelAttribute("usuario") Usuario usuario,
                                    @RequestParam("confirmPassword") String confirmPassword,
                                    @RequestParam("matricula") String matricula,
                                    @RequestParam(value = "imagenes", required = false) MultipartFile[] imagenes
                                   ,@RequestParam(value = "direccion", required = false) String direccion,
                                    @RequestParam(value = "ciudad", required = false) String ciudad,
                                    @RequestParam(value = "pais", required = false) String pais,
                                    @RequestParam(value = "telefono", required = false) String telefono,
                                    @RequestParam(value = "horario", required = false) String horario,
                                    RedirectAttributes redirectAttributes,
                                    @RequestParam (value="file",required = false) MultipartFile file) {
        modelo.clear();

        Usuario nuevoUsuario;




        if(usuario.getRol().equals("veterinario")){
            nuevoUsuario = new Veterinario();
            ((Veterinario) nuevoUsuario).setMatricula(matricula);
        } else {
            nuevoUsuario = new DuenoMascota();
        }

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

        setearUsuario(nuevoUsuario,usuario);



        if(usuario.getRol().equals("banco")){

            Banco banco = new Banco(usuario.getNombre(),direccion,ciudad,pais,telefono, usuario.getEmail(), usuario.getPassword(), horario);
            servicioLogin.RegistrarBanco(banco);
            redirectAttributes.addFlashAttribute("mensajeExito", "Usuario registrado con éxito espere que su cuenta sea validada");
            return new ModelAndView("redirect:/login",modelo);
        }



        try {
            servicioLogin.registrar(nuevoUsuario);

        } catch (UsuarioExistente e) {
            modelo.put("error", "El usuario ya existe");
            return new ModelAndView("nuevo-usuario", modelo);
        } catch (Exception e) {
            modelo.put("error", "Error al registrar el nuevo usuario");
            return new ModelAndView("nuevo-usuario", modelo);
        }

        try {
            servicioPDFFile.guardarPdf(file, "v_" + nuevoUsuario.getId());
        } catch (IllegalArgumentException e) {
            modelo.put("error","Error: " + e.getMessage());
        } catch (IOException e) {
            modelo.put("error","Error al guardar el archivo.");
        }

        try {
            servicioImagenes.guardarFotoDePerfilUsuario(imagenes, nuevoUsuario.getId());
        } catch (IOException e) {
            modelo.put("error","ocurrio un error al cargar la imagen");
        }

        return new ModelAndView("redirect:/login",modelo);
    }

    private void setearUsuario(Usuario nuevoUsuario, Usuario usuario) {

        nuevoUsuario.setNombre(usuario.getNombre());
        nuevoUsuario.setApellido(usuario.getApellido());
        nuevoUsuario.setEmail(usuario.getEmail());
        nuevoUsuario.setRol(usuario.getRol());
        nuevoUsuario.setPassword(usuario.getPassword());

    }

    @RequestMapping(path = "/nuevo-usuario", method = RequestMethod.GET)
    public ModelAndView nuevoUsuario() {
        ModelMap model = new ModelMap();
        model.put("usuario", new Usuario());
        return new ModelAndView("nuevo-usuario", model);
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