package com.devsu.cuenta_movimiento_service.mapper;

import com.devsu.cuenta_movimiento_service.dto.CuentaDto;
import com.devsu.cuenta_movimiento_service.entity.Cuenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class CuentaMapperTest {

    private CuentaMapper cuentaMapper;

    @BeforeEach
    void setUp() {
        cuentaMapper = new CuentaMapper();
    }

    @Test
    void testToEntity() {
        CuentaDto dto = CuentaDto.builder()
                .id(1L)
                .numeroCuenta("0011223344")
                .tipoCuenta("AHORROS")
                .saldoInicial(new BigDecimal("1000.0"))
                .estado(true)
                .clienteId(99L)
                .build();

        Cuenta entity = cuentaMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getNumeroCuenta(), entity.getNumeroCuenta());
        assertEquals(dto.getTipoCuenta(), entity.getTipoCuenta());
        assertEquals(dto.getSaldoInicial(), entity.getSaldoInicial());
        assertEquals(dto.getEstado(), entity.getEstado());
        assertEquals(dto.getClienteId(), entity.getClienteId());
    }

    @Test
    void testToEntity_NullInput() {
        Cuenta entity = cuentaMapper.toEntity(null);
        assertNull(entity);
    }


    @Test
    void testToDto() {
        Cuenta cuenta = Cuenta.builder()
                .id(2L)
                .numeroCuenta("9988776655")
                .tipoCuenta("CORRIENTE")
                .saldoInicial(new BigDecimal("2000.0"))
                .estado(false)
                .clienteId(77L)
                .build();

        CuentaDto dto = cuentaMapper.toDto(cuenta);

        assertNotNull(dto);
        assertEquals(cuenta.getId(), dto.getId());
        assertEquals(cuenta.getNumeroCuenta(), dto.getNumeroCuenta());
        assertEquals(cuenta.getTipoCuenta(), dto.getTipoCuenta());
        assertEquals(cuenta.getSaldoInicial(), dto.getSaldoInicial());
        assertEquals(cuenta.getEstado(), dto.getEstado());
        assertEquals(cuenta.getClienteId(), dto.getClienteId());
    }

    @Test
    void testToDto_NullInput() {
        CuentaDto dto = cuentaMapper.toDto(null);
        assertNull(dto);
    }
}
