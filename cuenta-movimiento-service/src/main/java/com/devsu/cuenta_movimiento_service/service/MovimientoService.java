package com.devsu.cuenta_movimiento_service.service;

import com.devsu.cuenta_movimiento_service.dto.MovimientoDto;
import com.devsu.cuenta_movimiento_service.entity.Movimiento;

import java.util.List;

public interface MovimientoService {

    void registrarMovimiento(MovimientoDto movimientoDto);

    List<Movimiento> obtenerMovimientosPorCuenta(Long cuentaId);

    List<Movimiento> obtenerMovimientosPorCuentaYFechas(Long cuentaId, String inicio, String fin);

}
