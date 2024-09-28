package com.tallerwebi.presentacion;
import com.tallerwebi.dominio.ServicioBanco;
import com.tallerwebi.dominio.Banco;
import com.tallerwebi.dominio.PaqueteDeSangre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
        PaqueteDeSangre paquete = new PaqueteDeSangre("A+", 1,"", banco);

        // Act & Assert
        mockMvc.perform(post("/agregarPaquete")
                        .param("idBanco", "1")
                        .param("tipoSangre", "A+")
                        .param("cantidad", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/BancoHome?success=Paquete+de+sangre+agregado+con+%C3%A9xito"));

    }

}
