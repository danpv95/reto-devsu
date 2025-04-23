package com.devsu.cuenta_movimiento_service.integracion;

import com.devsu.cuenta_movimiento_service.dto.CuentaDto;
import com.devsu.cuenta_movimiento_service.repository.CuentaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class CuentaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        cuentaRepository.deleteAll();

        // Mock para evitar error de conexión con el microservicio de clientes
        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenReturn(ResponseEntity.ok().build());
    }

    @Test
    void testCrearYObtenerCuenta() throws Exception {
        CuentaDto dto = CuentaDto.builder()
                .numeroCuenta("555555")
                .tipoCuenta("AHORROS")
                .saldoInicial(new BigDecimal("1200.00"))
                .estado(true)
                .clienteId(1L)
                .build();

        String response = mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/api/cuentas/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta").value("555555"));

        assertThat(cuentaRepository.findAll()).hasSize(1);
    }

    @Test
    void testEliminarCuentaNoExistente() throws Exception {
        mockMvc.perform(delete("/api/cuentas/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cuenta no encontrada con ID: 9999"));
    }

    @Test
    void testValidacionFallida() throws Exception {
        CuentaDto dto = CuentaDto.builder()
                .numeroCuenta("")
                .tipoCuenta("")
                .saldoInicial(null)
                .estado(null)
                .clienteId(null)
                .build();

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }
}
