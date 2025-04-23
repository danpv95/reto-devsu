package com.devsu.cuenta_movimiento_service.service;

import com.devsu.cuenta_movimiento_service.dto.CuentaDto;
import com.devsu.cuenta_movimiento_service.entity.Cuenta;
import com.devsu.cuenta_movimiento_service.exception.ResourceNotFoundException;
import com.devsu.cuenta_movimiento_service.mapper.CuentaMapper;
import com.devsu.cuenta_movimiento_service.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {

    private final CuentaRepository cuentaRepository;
    private final RestTemplate restTemplate;

    @Value("${cliente.service.url}")
    private String clienteServiceUrl;

    @Override
    public CuentaDto crearCuenta(CuentaDto cuentaDto) {
        validarCliente(cuentaDto.getClienteId());

        cuentaRepository.findByNumeroCuenta(cuentaDto.getNumeroCuenta())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Ya existe una cuenta con ese número");
                });

        Cuenta cuenta = CuentaMapper.toEntity(cuentaDto);
        return CuentaMapper.toDto(cuentaRepository.save(cuenta));
    }

    @Override
    public CuentaDto obtenerCuentaPorId(Long id) {
        return cuentaRepository.findById(id)
                .map(CuentaMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + id));
    }

    @Override
    public List<CuentaDto> listarCuentas() {
        return cuentaRepository.findAll()
                .stream()
                .map(CuentaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CuentaDto actualizarCuenta(Long id, CuentaDto cuentaDto) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + id));

        cuenta.setTipoCuenta(cuentaDto.getTipoCuenta());
        cuenta.setEstado(cuentaDto.getEstado());
        cuenta.setClienteId(cuentaDto.getClienteId());

        return CuentaMapper.toDto(cuentaRepository.save(cuenta));
    }

    @Override
    public void eliminarCuenta(Long id) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con ID: " + id));
        cuentaRepository.delete(cuenta);
    }

    private void validarCliente(Long clienteId) {
        try {
            ResponseEntity<?> response = restTemplate.getForEntity(
                    clienteServiceUrl + "/" + clienteId,
                    Object.class
            );

            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new IllegalArgumentException("El cliente no existe.");
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new IllegalArgumentException("El cliente no existe.");
        } catch (Exception e) {
            throw new RuntimeException("Error al validar el cliente: " + e.getMessage());
        }
    }
}
