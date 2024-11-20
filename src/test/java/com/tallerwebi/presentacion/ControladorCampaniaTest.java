package com.tallerwebi.presentacion;

import com.tallerwebi.dominio.entidad.Banco;
import com.tallerwebi.dominio.entidad.Campana;
import com.tallerwebi.dominio.servicio.ServicioBanco;
import com.tallerwebi.dominio.servicio.ServicioCampania;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;



    public class ControladorCampaniaTest {

        private final ServicioCampania servicioCampaniaMock = mock(ServicioCampania.class);
        private final ServicioBanco servicioBancoMock = mock(ServicioBanco.class);
        private final ControladorCampanias controladorCampanias = new ControladorCampanias(servicioCampaniaMock, servicioBancoMock);
        private final HttpSession sessionMock = mock(HttpSession.class);
        private final RedirectAttributes redirectAttributesMock = mock(RedirectAttributes.class);

        @Test
        public void deberiaRedirigirALoginSiNoHaySesionParaMostrarFormulario() {
            when(sessionMock.getAttribute("idBanco")).thenReturn(null);

            ModelAndView modelAndView = controladorCampanias.mostrarFormularioCrearCampania(sessionMock, null);

            assertThat(modelAndView.getViewName(), is(equalTo("redirect:/login")));
        }

        @Test
        public void deberiaMostrarFormularioCrearCampaniaSiHaySesion() {
            // Configurar mocks
            when(sessionMock.getAttribute("idBanco")).thenReturn(1L);  // Simula la sesión con el idBanco
            Banco bancoMock = new Banco();  // Crear un objeto Banco mockeado
            bancoMock.setDireccion("Ubicación de prueba");  // Configurar la dirección mockeada
            when(servicioBancoMock.BuscarBancoId(1L)).thenReturn(bancoMock);  // Simula la llamada al servicio

            // Ejecutar la acción
            ModelAndView modelAndView = controladorCampanias.mostrarFormularioCrearCampania(sessionMock, null);

            // Verificar el resultado
            assertThat(modelAndView.getViewName(), is(equalTo("crearCampania")));  // Verifica la vista
            assertThat(modelAndView.getModel().get("ubicacion"), is(equalTo("Ubicación de prueba")));  // Verifica la ubicación
            assertThat(modelAndView.getModel().get("error"), is(nullValue()));  // Verifica que no haya error
        }
        

        @Test
        public void deberiaCrearCampaniaYRedirigirABancoHome() {
            // Preparación
            Long idBanco = 1L;
            when(sessionMock.getAttribute("idBanco")).thenReturn(idBanco);
            Banco bancoMock = new Banco();
            when(servicioBancoMock.BuscarBancoId(idBanco)).thenReturn(bancoMock);

            // Ejecución
            String result = controladorCampanias.crearCampania(sessionMock, "Campaña Test", LocalDate.now(), LocalDate.now().plusDays(5),
                    "Ubicación Test", "Descripción Test", redirectAttributesMock);

            // Validación
            assertThat(result, is(equalTo("redirect:/bancoHome")));
            verify(servicioCampaniaMock).guardarCampania(any(Campana.class), eq(bancoMock));
            verify(redirectAttributesMock).addFlashAttribute("success", "Campaña creada con éxito");
        }



        @Test
        public void deberiaRedirigirALoginSiNoHaySesionAlMostrarMisCampanias() {
            when(sessionMock.getAttribute("idBanco")).thenReturn(null);

            ModelAndView modelAndView = controladorCampanias.mostrarMisCampanias(sessionMock);

            assertThat(modelAndView.getViewName(), is(equalTo("redirect:/login")));
        }

        @Test
        public void deberiaMostrarMisCampaniasSiHaySesion() {
            // Preparación
            Long idBanco = 1L;
            List<Campana> campaniasMock = new ArrayList<>();
            campaniasMock.add(new Campana());
            when(sessionMock.getAttribute("idBanco")).thenReturn(idBanco);
            when(servicioCampaniaMock.obtenerCampaniasPorBanco(idBanco)).thenReturn(campaniasMock);

            // Ejecución
            ModelAndView modelAndView = controladorCampanias.mostrarMisCampanias(sessionMock);

            // Validación
            assertThat(modelAndView.getViewName(), is(equalTo("bancoMisCampanias")));
            assertThat(modelAndView.getModel().get("campanias"), is(equalTo(campaniasMock)));
        }
    }



