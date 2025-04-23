package com.devsu.cliente_persona_service.Integracion;

import com.devsu.cliente_persona_service.dto.ClienteDto;
import com.devsu.cliente_persona_service.repository.ClienteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class ClienteIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();
    }

    @Test
    void testCrearYObtenerCliente() throws Exception {
        ClienteDto dto = ClienteDto.builder()
                .nombre("Carlos")
                .genero("Masculino")
                .edad(30)
                .identificacion("10101010")
                .direccion("Av Siempre Viva 123")
                .telefono("3110000000")
                .contrasena("1234")
                .estado(true)
                .build();

        String response = mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/clientes/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Carlos"));

        assertThat(clienteRepository.findAll()).hasSize(1);
    }


    @Test
    void testValidacionFallida() throws Exception {
        ClienteDto dto = ClienteDto.builder()
                .nombre("")  // nombre vacío, debería fallar
                .genero("")
                .edad(null)
                .identificacion("")
                .direccion("")
                .telefono("")
                .contrasena("")
                .estado(null)
                .build();

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").exists())
                .andExpect(jsonPath("$.estado").exists());
    }

    @Test
    void testEliminarClienteNoExistente() throws Exception {
        mockMvc.perform(delete("/api/clientes/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cliente no encontrado con id: 9999"));
    }
}
