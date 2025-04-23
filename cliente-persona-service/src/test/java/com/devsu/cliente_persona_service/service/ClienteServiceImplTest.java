package com.devsu.cliente_persona_service.service;

import com.devsu.cliente_persona_service.dto.ClienteDto;
import com.devsu.cliente_persona_service.dto.ClienteResponseDto;
import com.devsu.cliente_persona_service.entity.Cliente;
import com.devsu.cliente_persona_service.exception.ResourceNotFoundException;
import com.devsu.cliente_persona_service.mapper.ClienteMapper;
import com.devsu.cliente_persona_service.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    private ClienteRepository clienteRepository;
    private ClienteMapper clienteMapper;
    private ClienteServiceImpl clienteService;

    @BeforeEach
    void setup() {
        clienteRepository = mock(ClienteRepository.class);
        clienteMapper = mock(ClienteMapper.class);
        clienteService = new ClienteServiceImpl(clienteRepository, clienteMapper);
    }

    @Test
    void testCrearCliente() {
        ClienteDto dto = new ClienteDto();
        Cliente cliente = new Cliente();
        Cliente saved = new Cliente();
        ClienteResponseDto response = new ClienteResponseDto();

        when(clienteMapper.toEntity(dto)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(saved);
        when(clienteMapper.toResponseDto(saved)).thenReturn(response);

        ClienteResponseDto result = clienteService.crearCliente(dto);

        assertNotNull(result);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void testObtenerClientePorId_Existente() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        ClienteResponseDto response = new ClienteResponseDto();
        response.setId(1L);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toResponseDto(cliente)).thenReturn(response);

        ClienteResponseDto result = clienteService.obtenerClientePorId(1L);
        assertEquals(1L, result.getId());
    }

    @Test
    void testObtenerClientePorId_NoExistente() {
        when(clienteRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> clienteService.obtenerClientePorId(2L));
    }

    @Test
    void testListarClientes() {
        Cliente cliente = new Cliente();
        ClienteResponseDto dto = new ClienteResponseDto();

        when(clienteRepository.findAll()).thenReturn(List.of(cliente));
        when(clienteMapper.toResponseDto(cliente)).thenReturn(dto);

        List<ClienteResponseDto> lista = clienteService.listarClientes();
        assertEquals(1, lista.size());
    }

    @Test
    void testActualizarCliente() {
        ClienteDto dto = new ClienteDto();
        dto.setNombre("Juan");
        dto.setEstado(true);
        Cliente cliente = new Cliente();
        ClienteResponseDto response = new ClienteResponseDto();

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toResponseDto(cliente)).thenReturn(response);

        ClienteResponseDto result = clienteService.actualizarCliente(1L, dto);
        assertNotNull(result);
    }

    @Test
    void testEliminarCliente() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        clienteService.eliminarCliente(1L);

        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void testEliminarClienteNoExistente() {
        when(clienteRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> clienteService.eliminarCliente(1L));
    }
}
