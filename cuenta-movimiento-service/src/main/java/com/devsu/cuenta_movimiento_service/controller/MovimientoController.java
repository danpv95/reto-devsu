package com.devsu.cuenta_movimiento_service.controller;

import com.devsu.cuenta_movimiento_service.dto.MovimientoDto;
import com.devsu.cuenta_movimiento_service.entity.Movimiento;
import com.devsu.cuenta_movimiento_service.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<String> registrarMovimiento(@Valid @RequestBody MovimientoDto dto) {
        movimientoService.registrarMovimiento(dto);
        return ResponseEntity.ok("Movimiento registrado correctamente.");
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<?> obtenerPorCuenta(@PathVariable Long cuentaId) {
        List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorCuenta(cuentaId);

        if (movimientos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "La cuenta " + cuentaId + " no tiene movimientos registrados."));
        }
        return ResponseEntity.ok(movimientos);
    }


    @GetMapping("/cuenta/{cuentaId}/reporte")
    public ResponseEntity<?> obtenerReporte(
            @PathVariable Long cuentaId,
            @RequestParam String inicio,
            @RequestParam String fin) {

        try {
            List<Movimiento> movimientos = movimientoService.obtenerMovimientosPorCuentaYFechas(cuentaId, inicio, fin);

            if (movimientos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "No se encontraron movimientos para la cuenta " + cuentaId));
            }

            return ResponseEntity.ok(movimientos);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Formato de fecha inválido. Usa 'YYYY-MM-DD'."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
