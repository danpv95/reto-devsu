package com.devsu.cuenta_movimiento_service.mapper;

import com.devsu.cuenta_movimiento_service.dto.CuentaDto;
import com.devsu.cuenta_movimiento_service.entity.Cuenta;

public class CuentaMapper {

    public static Cuenta toEntity(CuentaDto dto) {
        if (dto == null) return null;

        return Cuenta.builder()
                .id(dto.getId())
                .numeroCuenta(dto.getNumeroCuenta())
                .tipoCuenta(dto.getTipoCuenta())
                .saldoInicial(dto.getSaldoInicial())
                .estado(dto.getEstado())
                .clienteId(dto.getClienteId())
                .build();
    }

    public static CuentaDto toDto(Cuenta entity) {
        if (entity == null) return null;

        return CuentaDto.builder()
                .id(entity.getId())
                .numeroCuenta(entity.getNumeroCuenta())
                .tipoCuenta(entity.getTipoCuenta())
                .saldoInicial(entity.getSaldoInicial())
                .estado(entity.getEstado())
                .clienteId(entity.getClienteId())
                .build();
    }
}
