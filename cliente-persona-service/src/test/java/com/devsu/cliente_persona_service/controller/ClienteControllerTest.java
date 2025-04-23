package com.devsu.cliente_persona_service.controller;

import com.devsu.cliente_persona_service.dto.ClienteDto;
import com.devsu.cliente_persona_service.dto.ClienteResponseDto;
import com.devsu.cliente_persona_service.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(clienteController).build();
    }

    @Test
    void testCrearCliente() throws Exception {
        ClienteDto dto = ClienteDto.builder()
                .nombre("Test")
                .genero("Masculino")
                .edad(30)
                .identificacion("123456")
                .direccion("Calle falsa")
                .telefono("123456789")
                .contrasena("clave")
                .estado(true)
                .build();

        ClienteResponseDto response = ClienteResponseDto.builder()
                .id(1L)
                .nombre(dto.getNombre())
                .genero(dto.getGenero())
                .edad(dto.getEdad())
                .identificacion(dto.getIdentificacion())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .estado(dto.getEstado())
                .build();

        when(clienteService.crearCliente(any(ClienteDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testObtenerCliente() throws Exception {
        ClienteResponseDto response = ClienteResponseDto.builder()
                .id(1L)
                .nombre("Juan")
                .genero("Masculino")
                .edad(40)
                .identificacion("789456")
                .direccion("Avenida 1")
                .telefono("987654321")
                .estado(true)
                .build();

        when(clienteService.obtenerClientePorId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan"));
    }

    @Test
    void testListarClientes() throws Exception {
        ClienteResponseDto cliente = ClienteResponseDto.builder()
                .id(1L)
                .nombre("Ana")
                .genero("Femenino")
                .edad(28)
                .identificacion("1234")
                .direccion("Calle 10")
                .telefono("88888")
                .estado(true)
                .build();

        when(clienteService.listarClientes()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Ana"));
    }

    @Test
    void testActualizarCliente() throws Exception {
        ClienteDto dto = ClienteDto.builder()
                .nombre("Carlos")
                .genero("Masculino")
                .edad(33)
                .identificacion("555555")
                .direccion("Nueva dirección")
                .telefono("555123456")
                .contrasena("nuevaClave")
                .estado(true)
                .build();

        ClienteResponseDto response = ClienteResponseDto.builder()
                .id(1L)
                .nombre("Carlos")
                .genero("Masculino")
                .edad(33)
                .identificacion("555555")
                .direccion("Nueva dirección")
                .telefono("555123456")
                .estado(true)
                .build();

        when(clienteService.actualizarCliente(eq(1L), any(ClienteDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Carlos"));
    }

    @Test
    void testEliminarCliente() throws Exception {
        doNothing().when(clienteService).eliminarCliente(1L);

        mockMvc.perform(delete("/api/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Cliente eliminado exitosamente"));
    }
}
