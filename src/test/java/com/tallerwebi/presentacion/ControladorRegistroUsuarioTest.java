package com.tallerwebi.presentacion;

import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;

public class ControladorRegistroUsuarioTest {
    ControladorRegistroUsuario controladorRegistro = new ControladorRegistroUsuario();

    @Test
    public void siExisteMailYContrasenaElRegistroEsExitoso() {
        //preparación -> given
        givenNoExisteUsuario();
        //ejecución -> when
        ModelAndView mav = whenRegistroUsuario("lala@gmail.com", "1234", "1234");
        //comprobación -> then
        thenRegistroExitoso(mav);
    }

    @Test
    public void siElEmailEstaVacioElRegistroFalla() {
        //preparación -> given
        givenNoExisteUsuario();
        //ejecución -> when
        ModelAndView mav = whenRegistroUsuario("", "1234", "1234");
        //comprobación -> then
        thenRegistroFalla(mav, "error email vacio", "El campo de email es obligatorio");
    }

    @Test
    public void siLaContrasenaEstaVaciaElRegistroFalla() {
        //preparación -> given
        givenNoExisteUsuario();
        //ejecución -> when
        ModelAndView mav = whenRegistroUsuario("lala@gmail.com", "", "");
        //comprobación -> then
        thenRegistroFalla(mav, "error contraseña vacia", "El campo de contraseña es obligatorio");
    }

    @Test
    public void siLasDosContrasenasNoSonIgualesFalla() {
        //preparación -> given
        givenNoExisteUsuario();
        //ejecución -> when
        ModelAndView mav = whenRegistroUsuario("lala@gmail.com", "1234", "abc");
        //comprobación -> then
        thenRegistroFalla(mav, "error contraseñas diferentes",
                "Las contraseñas deben ser iguales");

    }

    private void givenNoExisteUsuario() {
    }

    private ModelAndView whenRegistroUsuario(String email, String contrasena1, String contrasena2) {
        ModelAndView mav = controladorRegistro.registrar(email, contrasena1, contrasena2);
        return mav;
    }

    private void thenRegistroExitoso(ModelAndView mav) {
        assertThat(mav.getViewName(), equalToIgnoringCase("redirect:/login"));
    }

    private void thenRegistroFalla(ModelAndView mav, String errorKey, String mensajeError) {
        assertThat(mav.getViewName(), equalToIgnoringCase("registro"));
        assertThat(mav.getModel().get(errorKey).toString(), equalToIgnoringCase(mensajeError));
    }
}
