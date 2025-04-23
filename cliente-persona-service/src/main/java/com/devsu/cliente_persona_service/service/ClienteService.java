package com.devsu.cliente_persona_service.service;

import com.devsu.cliente_persona_service.dto.ClienteDto;
import com.devsu.cliente_persona_service.dto.ClienteResponseDto;

import java.util.List;

public interface ClienteService {
    ClienteResponseDto crearCliente(ClienteDto dto);

    ClienteResponseDto obtenerClientePorId(Long id);

    List<ClienteResponseDto> listarClientes();

    ClienteResponseDto actualizarCliente(Long id, ClienteDto dto);

    void eliminarCliente(Long id);
}
