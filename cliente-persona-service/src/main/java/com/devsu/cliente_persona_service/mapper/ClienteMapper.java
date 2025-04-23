package com.devsu.cliente_persona_service.mapper;

import com.devsu.cliente_persona_service.dto.ClienteDto;
import com.devsu.cliente_persona_service.dto.ClienteResponseDto;
import com.devsu.cliente_persona_service.entity.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteDto dto) {
        if (dto == null) return null;

        return Cliente.builder()
                .nombre(dto.getNombre())
                .genero(dto.getGenero())
                .edad(dto.getEdad())
                .identificacion(dto.getIdentificacion())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .contrasena(dto.getContrasena())
                .estado(dto.getEstado())
                .build();
    }

    public ClienteResponseDto toResponseDto(Cliente cliente) {
        if (cliente == null) return null;

        return ClienteResponseDto.builder()
                .id(cliente.getId())
                .nombre(cliente.getNombre())
                .genero(cliente.getGenero())
                .edad(cliente.getEdad())
                .identificacion(cliente.getIdentificacion())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .estado(cliente.getEstado())
                .build();
    }

    public ClienteDto toDto(Cliente cliente) {
        if (cliente == null) return null;

        return ClienteDto.builder()
                .nombre(cliente.getNombre())
                .genero(cliente.getGenero())
                .edad(cliente.getEdad())
                .identificacion(cliente.getIdentificacion())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .contrasena(cliente.getContrasena())
                .estado(cliente.getEstado())
                .build();
    }
}
