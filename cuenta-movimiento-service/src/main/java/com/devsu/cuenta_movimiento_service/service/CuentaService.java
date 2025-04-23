package com.devsu.cuenta_movimiento_service.service;

import com.devsu.cuenta_movimiento_service.dto.CuentaDto;

import java.util.List;

public interface CuentaService {

    CuentaDto crearCuenta(CuentaDto cuentaDto);

    CuentaDto obtenerCuentaPorId(Long id);

    List<CuentaDto> listarCuentas();

    CuentaDto actualizarCuenta(Long id, CuentaDto cuentaDto);

    void eliminarCuenta(Long id);
}
