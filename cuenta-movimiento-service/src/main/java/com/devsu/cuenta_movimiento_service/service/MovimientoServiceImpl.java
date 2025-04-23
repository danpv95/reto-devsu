package com.devsu.cuenta_movimiento_service.service.impl;

import com.devsu.cuenta_movimiento_service.dto.MovimientoDto;
import com.devsu.cuenta_movimiento_service.entity.Cuenta;
import com.devsu.cuenta_movimiento_service.entity.Movimiento;
import com.devsu.cuenta_movimiento_service.exception.ResourceNotFoundException;
import com.devsu.cuenta_movimiento_service.mapper.MovimientoMapper;
import com.devsu.cuenta_movimiento_service.repository.CuentaRepository;
import com.devsu.cuenta_movimiento_service.repository.MovimientoRepository;
import com.devsu.cuenta_movimiento_service.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    @Override
    public void registrarMovimiento(MovimientoDto dto) {
        Cuenta cuenta = cuentaRepository.findById(dto.getCuentaId())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + dto.getCuentaId()));

        BigDecimal saldoActual = cuenta.getSaldoInicial();
        BigDecimal nuevoSaldo;

        if ("RETIRO".equalsIgnoreCase(dto.getTipoMovimiento())) {
            if (saldoActual.compareTo(dto.getValor()) < 0) {
                throw new IllegalArgumentException("Saldo insuficiente para realizar el retiro.");
            }
            nuevoSaldo = saldoActual.subtract(dto.getValor());
        } else {
            nuevoSaldo = saldoActual.add(dto.getValor());
        }

        cuenta.setSaldoInicial(nuevoSaldo);
        cuentaRepository.save(cuenta);

        Movimiento movimiento = MovimientoMapper.toEntity(dto, cuenta, nuevoSaldo);
        movimientoRepository.save(movimiento);
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorCuenta(Long cuentaId) {
        cuentaRepository.findById(cuentaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + cuentaId));

        return movimientoRepository.findByCuentaId(cuentaId);
    }

    @Override
    public List<Movimiento> obtenerMovimientosPorCuentaYFechas(Long cuentaId, String inicioStr, String finStr) {
        if (cuentaId == null) {
            throw new IllegalArgumentException("El ID de cuenta no puede ser null.");
        }

        LocalDate inicio = LocalDate.parse(inicioStr);
        LocalDate fin = LocalDate.parse(finStr);

        if (fin.isBefore(inicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }

        return movimientoRepository.buscarPorCuentaYFechas(cuentaId, inicio, fin);
    }
}
