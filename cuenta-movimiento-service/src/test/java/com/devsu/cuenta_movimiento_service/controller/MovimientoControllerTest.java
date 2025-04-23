package com.devsu.cuenta_movimiento_service.controller;

import com.devsu.cuenta_movimiento_service.dto.MovimientoDto;
import com.devsu.cuenta_movimiento_service.entity.Movimiento;
import com.devsu.cuenta_movimiento_service.service.MovimientoService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
class MovimientoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MovimientoService movimientoService;

    @InjectMocks
    private MovimientoController movimientoController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(movimientoController).build();
    }

    @Test
    void testRegistrarMovimiento() throws Exception {
        MovimientoDto dto = new MovimientoDto();
        dto.setTipoMovimiento("DEPOSITO");
        dto.setValor(BigDecimal.valueOf(1000));
        dto.setCuentaId(1L);

        doNothing().when(movimientoService).registrarMovimiento(any(MovimientoDto.class));

        mockMvc.perform(post("/api/movimientos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("Movimiento registrado correctamente."));
    }

    @Test
    void testObtenerPorCuentaConDatos() throws Exception {
        Movimiento mov = new Movimiento();
        when(movimientoService.obtenerMovimientosPorCuenta(1L)).thenReturn(List.of(mov));

        mockMvc.perform(get("/api/movimientos/cuenta/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerPorCuentaSinDatos() throws Exception {
        when(movimientoService.obtenerMovimientosPorCuenta(1L)).thenReturn(List.of());

        mockMvc.perform(get("/api/movimientos/cuenta/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testReporteMovimientos() throws Exception {
        Movimiento mov = new Movimiento();
        when(movimientoService.obtenerMovimientosPorCuentaYFechas(1L, "2025-04-01", "2025-04-22")).thenReturn(List.of(mov));

        mockMvc.perform(get("/api/movimientos/cuenta/1/reporte")
                        .param("inicio", "2025-04-01")
                        .param("fin", "2025-04-22"))
                .andExpect(status().isOk());
    }

    @Test
    void testReporteMovimientosFechasInvalidas() throws Exception {
        mockMvc.perform(get("/api/movimientos/cuenta/1/reporte")
                        .param("inicio", "invalid")
                        .param("fin", "2025-04-22"))
                .andExpect(status().isNotFound());
    }
}
