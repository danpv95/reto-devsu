package com.devsu.cuenta_movimiento_service.service;

import com.devsu.cuenta_movimiento_service.dto.MovimientoDto;
import com.devsu.cuenta_movimiento_service.entity.Cuenta;
import com.devsu.cuenta_movimiento_service.entity.Movimiento;
import com.devsu.cuenta_movimiento_service.exception.ResourceNotFoundException;
import com.devsu.cuenta_movimiento_service.repository.CuentaRepository;
import com.devsu.cuenta_movimiento_service.repository.MovimientoRepository;
import com.devsu.cuenta_movimiento_service.service.impl.MovimientoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceImplTest {

    private MovimientoRepository movimientoRepository;
    private CuentaRepository cuentaRepository;
    private MovimientoServiceImpl movimientoService;

    @BeforeEach
    void setUp() {
        movimientoRepository = mock(MovimientoRepository.class);
        cuentaRepository = mock(CuentaRepository.class);
        movimientoService = new MovimientoServiceImpl(movimientoRepository, cuentaRepository);
    }

    @Test
    void testRegistrarMovimiento_Deposito() {
        MovimientoDto dto = new MovimientoDto();
        dto.setCuentaId(1L);
        dto.setTipoMovimiento("DEPOSITO");
        dto.setValor(BigDecimal.valueOf(100));

        Cuenta cuenta = new Cuenta();
        cuenta.setSaldoInicial(BigDecimal.valueOf(500));

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(new Movimiento());
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        movimientoService.registrarMovimiento(dto);

        assertEquals(BigDecimal.valueOf(600), cuenta.getSaldoInicial());
        verify(movimientoRepository).save(any(Movimiento.class));
    }

    @Test
    void testRegistrarMovimiento_RetiroConFondos() {
        MovimientoDto dto = new MovimientoDto();
        dto.setCuentaId(1L);
        dto.setTipoMovimiento("RETIRO");
        dto.setValor(BigDecimal.valueOf(100));

        Cuenta cuenta = new Cuenta();
        cuenta.setSaldoInicial(BigDecimal.valueOf(500));

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(new Movimiento());
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        movimientoService.registrarMovimiento(dto);

        assertEquals(BigDecimal.valueOf(400), cuenta.getSaldoInicial());
    }

    @Test
    void testRegistrarMovimiento_RetiroSinFondos() {
        MovimientoDto dto = new MovimientoDto();
        dto.setCuentaId(1L);
        dto.setTipoMovimiento("RETIRO");
        dto.setValor(BigDecimal.valueOf(600));

        Cuenta cuenta = new Cuenta();
        cuenta.setSaldoInicial(BigDecimal.valueOf(500));

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        assertThrows(IllegalArgumentException.class, () -> movimientoService.registrarMovimiento(dto));
    }

    @Test
    void testObtenerMovimientosPorCuenta_Existente() {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findByCuentaId(1L)).thenReturn(Collections.singletonList(new Movimiento()));

        List<Movimiento> result = movimientoService.obtenerMovimientosPorCuenta(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testObtenerMovimientosPorCuenta_NoExistente() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> movimientoService.obtenerMovimientosPorCuenta(1L));
    }

    @Test
    void testObtenerMovimientosPorFechas_Correcto() {
        when(movimientoRepository.buscarPorCuentaYFechas(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(List.of(new Movimiento()));

        List<Movimiento> result = movimientoService.obtenerMovimientosPorCuentaYFechas(
                1L, "2024-01-01", "2024-01-10");

        assertEquals(1, result.size());
    }

    @Test
    void testObtenerMovimientosPorFechas_CuentaNull() {
        assertThrows(IllegalArgumentException.class, () ->
                movimientoService.obtenerMovimientosPorCuentaYFechas(null, "2024-01-01", "2024-01-10"));
    }

    @Test
    void testObtenerMovimientosPorFechas_FechasInvalidas() {
        assertThrows(IllegalArgumentException.class, () ->
                movimientoService.obtenerMovimientosPorCuentaYFechas(1L, "2024-02-01", "2024-01-01"));
    }
}
