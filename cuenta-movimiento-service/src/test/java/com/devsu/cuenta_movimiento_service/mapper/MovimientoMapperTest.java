package com.devsu.cuenta_movimiento_service.mapper;

import com.devsu.cuenta_movimiento_service.dto.MovimientoDto;
import com.devsu.cuenta_movimiento_service.entity.Cuenta;
import com.devsu.cuenta_movimiento_service.entity.Movimiento;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class MovimientoMapperTest {

    @Test
    void testToEntity_successfulMapping() {
        MovimientoDto dto = MovimientoDto.builder()
                .tipoMovimiento("RETIRO")
                .valor(new BigDecimal("150.00"))
                .build();

        Cuenta cuenta = Cuenta.builder()
                .id(1L)
                .numeroCuenta("12345678")
                .tipoCuenta("AHORROS")
                .saldoInicial(new BigDecimal("1000.00"))
                .estado(true)
                .clienteId(1L)
                .build();

        BigDecimal saldoFinal = new BigDecimal("850.00");

        Movimiento movimiento = MovimientoMapper.toEntity(dto, cuenta, saldoFinal);

        assertNotNull(movimiento);
        assertEquals(dto.getTipoMovimiento(), movimiento.getTipoMovimiento());
        assertEquals(dto.getValor(), movimiento.getValor());
        assertEquals(saldoFinal, movimiento.getSaldo());
        assertEquals(cuenta, movimiento.getCuenta());
        assertNotNull(movimiento.getFecha());
    }

    @Test
    void testToEntity_nullDto() {
        Cuenta cuenta = Cuenta.builder().id(1L).build();
        BigDecimal saldoFinal = new BigDecimal("500.00");

        Movimiento movimiento = MovimientoMapper.toEntity(null, cuenta, saldoFinal);
        assertNull(movimiento);
    }

    @Test
    void testToEntity_nullCuenta() {
        MovimientoDto dto = MovimientoDto.builder()
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("200.00"))
                .build();

        Movimiento movimiento = MovimientoMapper.toEntity(dto, null, new BigDecimal("1200.00"));
        assertNull(movimiento);
    }

    @Test
    void testToEntity_nullSaldoFinal() {
        MovimientoDto dto = MovimientoDto.builder()
                .tipoMovimiento("DEPOSITO")
                .valor(new BigDecimal("200.00"))
                .build();

        Cuenta cuenta = Cuenta.builder().id(1L).build();

        Movimiento movimiento = MovimientoMapper.toEntity(dto, cuenta, null);
        assertNull(movimiento);
    }
}
