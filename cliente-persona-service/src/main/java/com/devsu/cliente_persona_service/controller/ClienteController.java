package com.devsu.cliente_persona_service.controller;

import com.devsu.cliente_persona_service.dto.ClienteDto;
import com.devsu.cliente_persona_service.dto.ClienteResponseDto;
import com.devsu.cliente_persona_service.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    public ResponseEntity<ClienteResponseDto> crearCliente(@Valid @RequestBody ClienteDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.crearCliente(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> obtenerCliente(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.obtenerClientePorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDto>> listarClientes() {
        return ResponseEntity.ok(clienteService.listarClientes());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDto> actualizar(@PathVariable Long id, @Valid @RequestBody ClienteDto dto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clienteService.actualizarCliente(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> eliminar(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", "Cliente eliminado exitosamente");
        return ResponseEntity.ok(response);
    }

}
