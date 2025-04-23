package com.devsu.cliente_persona_service.mapper;

import com.devsu.cliente_persona_service.dto.ClienteDto;
import com.devsu.cliente_persona_service.dto.ClienteResponseDto;
import com.devsu.cliente_persona_service.entity.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class ClienteMapperTest {

    private ClienteMapper clienteMapper;

    @BeforeEach
    void setUp() {
        clienteMapper = new ClienteMapper();
    }

    @Test
    void testToEntity() {
        ClienteDto dto = ClienteDto.builder()
                .nombre("Daniel")
                .genero("Masculino")
                .edad(30)
                .identificacion("12345678")
                .direccion("Calle 123")
                .telefono("3210000000")
                .contrasena("clave")
                .estado(true)
                .build();

        Cliente cliente = clienteMapper.toEntity(dto);

        assertNotNull(cliente);
        assertEquals(dto.getNombre(), cliente.getNombre());
        assertEquals(dto.getGenero(), cliente.getGenero());
        assertEquals(dto.getEdad(), cliente.getEdad());
        assertEquals(dto.getIdentificacion(), cliente.getIdentificacion());
        assertEquals(dto.getDireccion(), cliente.getDireccion());
        assertEquals(dto.getTelefono(), cliente.getTelefono());
        assertEquals(dto.getContrasena(), cliente.getContrasena());
        assertEquals(dto.getEstado(), cliente.getEstado());
    }

    @Test
    void testToResponseDto() {
        Cliente cliente = Cliente.builder()
                .id(1L)
                .nombre("Daniel")
                .genero("Masculino")
                .edad(30)
                .identificacion("12345678")
                .direccion("Calle 123")
                .telefono("3210000000")
                .contrasena("clave")
                .estado(true)
                .build();

        ClienteResponseDto responseDto = clienteMapper.toResponseDto(cliente);

        assertNotNull(responseDto);
        assertEquals(cliente.getId(), responseDto.getId());
        assertEquals(cliente.getNombre(), responseDto.getNombre());
        assertEquals(cliente.getEstado(), responseDto.getEstado());
    }

    @Test
    void testToDto() {
        Cliente cliente = Cliente.builder()
                .id(2L)
                .nombre("Camila")
                .genero("Femenino")
                .edad(28)
                .identificacion("987654")
                .direccion("Carrera 7")
                .telefono("123123123")
                .contrasena("secret")
                .estado(false)
                .build();

        ClienteDto dto = clienteMapper.toDto(cliente);

        assertNotNull(dto);
        assertEquals(cliente.getNombre(), dto.getNombre());
        assertEquals(cliente.getEstado(), dto.getEstado());
        assertEquals(cliente.getContrasena(), dto.getContrasena());
    }
}
