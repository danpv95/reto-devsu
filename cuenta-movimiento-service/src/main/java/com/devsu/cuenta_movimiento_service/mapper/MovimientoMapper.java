package com.devsu.cuenta_movimiento_service.mapper;

import com.devsu.cuenta_movimiento_service.dto.MovimientoDto;
import com.devsu.cuenta_movimiento_service.entity.Cuenta;
import com.devsu.cuenta_movimiento_service.entity.Movimiento;

import java.time.LocalDate;

public class MovimientoMapper {

    public static Movimiento toEntity(MovimientoDto dto, Cuenta cuenta, java.math.BigDecimal saldoFinal) {
        if (dto == null || cuenta == null || saldoFinal == null) return null;

        return Movimiento.builder()
                .fecha(LocalDate.now())
                .tipoMovimiento(dto.getTipoMovimiento())
                .valor(dto.getValor())
                .saldo(saldoFinal)
                .cuenta(cuenta)
                .build();
    }
}
