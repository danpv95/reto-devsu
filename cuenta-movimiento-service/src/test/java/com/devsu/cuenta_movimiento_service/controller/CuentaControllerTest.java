package com.devsu.cuenta_movimiento_service.controller;

import com.devsu.cuenta_movimiento_service.dto.CuentaDto;
import com.devsu.cuenta_movimiento_service.service.CuentaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("dev")
class CuentaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private CuentaController cuentaController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cuentaController).build();
    }

    @Test
    void testCrearCuenta() throws Exception {
        CuentaDto dto = CuentaDto.builder()
                .numeroCuenta("001")
                .tipoCuenta("AHORROS")
                .saldoInicial(new BigDecimal("1000.00"))
                .estado(true)
                .clienteId(1L)
                .build();

        when(cuentaService.crearCuenta(any(CuentaDto.class))).thenReturn(dto);

        mockMvc.perform(post("/api/cuentas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta").value("001"));
    }


    @Test
    void testObtenerCuenta() throws Exception {
        CuentaDto dto = new CuentaDto();
        dto.setNumeroCuenta("001");

        when(cuentaService.obtenerCuentaPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/cuentas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta").value("001"));
    }

    @Test
    void testListarCuentas() throws Exception {
        CuentaDto dto = new CuentaDto();
        dto.setNumeroCuenta("001");

        when(cuentaService.listarCuentas()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/cuentas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroCuenta").value("001"));
    }

    @Test
    void testActualizarCuenta() throws Exception {
        CuentaDto dto = CuentaDto.builder()
                .numeroCuenta("002")
                .tipoCuenta("CORRIENTE")
                .saldoInicial(new BigDecimal("5000.00"))
                .estado(false)
                .clienteId(2L)
                .build();

        when(cuentaService.actualizarCuenta(eq(1L), any(CuentaDto.class))).thenReturn(dto);

        mockMvc.perform(put("/api/cuentas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta").value("002"));
    }


    @Test
    void testEliminarCuenta() throws Exception {
        doNothing().when(cuentaService).eliminarCuenta(1L);

        mockMvc.perform(delete("/api/cuentas/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"mensaje\": \"Cuenta eliminada correctamente\"}"));
    }
}
