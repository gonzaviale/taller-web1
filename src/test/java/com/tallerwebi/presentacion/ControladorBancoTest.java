package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.ServicioBanco;
import com.tallerwebi.dominio.Banco;
import com.tallerwebi.dominio.PaqueteDeSangre;
import com.tallerwebi.dominio.excepcion.BancoNoEncontrado;
import com.tallerwebi.presentacion.ControladorBanco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ControladorBancoTest {

    @InjectMocks
    private ControladorBanco controladorBanco;

    @Mock
    private ServicioBanco servicioBanco;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controladorBanco).build();
    }

    @Test
    void agregarPaqueteDeSangre_Success() throws Exception {
        // Arrange
        Banco banco = new Banco(); // Suponiendo que este banco tiene el ID correcto.
        PaqueteDeSangre paquete = new PaqueteDeSangre("A+", 1, banco);

        // Act & Assert
        mockMvc.perform(post("/agregarPaquete")
                        .param("idBanco", "1")
                        .param("tipoSangre", "A+")
                        .param("cantidad", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/BancoHome?success=Paquete+de+sangre+agregado+con+%C3%A9xito"));

        verify(servicioBanco, times(1)).agregarPaqueteDeSangre(anyLong(), any(PaqueteDeSangre.class));
    }

    @Test
    void agregarPaqueteDeSangre_Error() throws Exception {
        // Arrange
        when(servicioBanco.agregarPaqueteDeSangre(anyLong(), any(PaqueteDeSangre.class)))
                .thenThrow(new BancoNoEncontrado("BancoHome?error=Banco+no+Registrado+inicia+session"));

        // Act & Assert
        mockMvc.perform(post("/agregarPaquete")
                        .param("idBanco", "1")
                        .param("tipoSangre", "A+")
                        .param("cantidad", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/BancoHome?error=Banco+no+Registrado+inicia+session")); // Ajusta el mensaje esperado aquí

        verify(servicioBanco, times(1)).agregarPaqueteDeSangre(anyLong(), any(PaqueteDeSangre.class));
    }
}
