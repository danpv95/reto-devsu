package com.devsu.cuenta_movimiento_service.service;

import com.devsu.cuenta_movimiento_service.dto.CuentaDto;
import com.devsu.cuenta_movimiento_service.entity.Cuenta;
import com.devsu.cuenta_movimiento_service.exception.ResourceNotFoundException;
import com.devsu.cuenta_movimiento_service.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class CuentaServiceImplTest {

    private CuentaRepository cuentaRepository;
    private RestTemplate restTemplate;
    private CuentaServiceImpl cuentaService;

    @BeforeEach
    void setup() {
        cuentaRepository = mock(CuentaRepository.class);
        restTemplate = mock(RestTemplate.class);
        cuentaService = new CuentaServiceImpl(cuentaRepository, restTemplate);
    }

    @Test
    void testCrearCuenta() {
        CuentaDto dto = new CuentaDto();
        dto.setNumeroCuenta("123");
        dto.setClienteId(1L);

        Cuenta saved = new Cuenta();
        saved.setId(1L);

        ReflectionTestUtils.setField(cuentaService, "clienteServiceUrl", "http://localhost:8081/api/clientes");

        // Simula que el cliente existe
        when(restTemplate.getForEntity("http://localhost:8081/api/clientes/1", Object.class))
                .thenReturn(ResponseEntity.ok().build());

        when(cuentaRepository.findByNumeroCuenta(dto.getNumeroCuenta())).thenReturn(Optional.empty());
        when(cuentaRepository.save(any())).thenReturn(saved);

        var result = cuentaService.crearCuenta(dto);
        assertNotNull(result);
        verify(cuentaRepository).save(any());
    }


    @Test
    void testCrearCuentaDuplicada() {
        CuentaDto dto = CuentaDto.builder().numeroCuenta("123").clienteId(1L).build();

        when(restTemplate.getForEntity(anyString(), eq(Object.class))).thenReturn(ResponseEntity.ok().build());
        when(cuentaRepository.findByNumeroCuenta("123")).thenReturn(Optional.of(new Cuenta()));

        assertThrows(IllegalArgumentException.class, () -> cuentaService.crearCuenta(dto));
    }

    @Test
    void testValidarClienteNotFound() {
        CuentaDto dto = new CuentaDto();
        dto.setClienteId(123L);

        ReflectionTestUtils.setField(cuentaService, "clienteServiceUrl", "http://localhost:8081/api/clientes");

        when(restTemplate.getForEntity("http://localhost:8081/api/clientes/123", Object.class))
                .thenThrow(HttpClientErrorException.NotFound.class);

        assertThrows(IllegalArgumentException.class, () -> cuentaService.crearCuenta(dto));
    }

    @Test
    void testObtenerCuentaPorIdExistente() {
        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        var dto = cuentaService.obtenerCuentaPorId(1L);
        assertEquals(1L, dto.getId());
    }

    @Test
    void testObtenerCuentaPorIdNoExistente() {
        when(cuentaRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> cuentaService.obtenerCuentaPorId(2L));
    }

    @Test
    void testListarCuentas() {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("123");
        when(cuentaRepository.findAll()).thenReturn(List.of(cuenta));
        var lista = cuentaService.listarCuentas();
        assertEquals(1, lista.size());
    }

    @Test
    void testActualizarCuenta() {
        CuentaDto dto = new CuentaDto();
        dto.setTipoCuenta("CORRIENTE");
        dto.setEstado(true);
        dto.setClienteId(5L);
        Cuenta cuenta = new Cuenta();

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(cuenta)).thenReturn(cuenta);
        var result = cuentaService.actualizarCuenta(1L, dto);
        assertNotNull(result);
    }

    @Test
    void testEliminarCuentaExistente() {
        Cuenta cuenta = new Cuenta();
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        doNothing().when(cuentaRepository).delete(cuenta);
        cuentaService.eliminarCuenta(1L);
        verify(cuentaRepository).delete(cuenta);
    }

    @Test
    void testEliminarCuentaNoExistente() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> cuentaService.eliminarCuenta(1L));
    }
}
