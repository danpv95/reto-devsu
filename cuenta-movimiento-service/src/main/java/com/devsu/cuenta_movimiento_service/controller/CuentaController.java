package com.devsu.cuenta_movimiento_service.controller;

import com.devsu.cuenta_movimiento_service.dto.CuentaDto;
import com.devsu.cuenta_movimiento_service.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<CuentaDto> crearCuenta(@Valid @RequestBody CuentaDto cuentaDto) {
        return ResponseEntity.ok(cuentaService.crearCuenta(cuentaDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaDto> obtenerCuenta(@PathVariable Long id) {
        return ResponseEntity.ok(cuentaService.obtenerCuentaPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<CuentaDto>> listarCuentas() {
        return ResponseEntity.ok(cuentaService.listarCuentas());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuentaDto> actualizarCuenta(@PathVariable Long id, @Valid @RequestBody CuentaDto cuentaDto) {
        return ResponseEntity.ok(cuentaService.actualizarCuenta(id, cuentaDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCuenta(@PathVariable Long id) {
        cuentaService.eliminarCuenta(id);
        return ResponseEntity.ok().body("{\"mensaje\": \"Cuenta eliminada correctamente\"}");
    }
}
