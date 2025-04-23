package com.devsu.cliente_persona_service.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("dev")
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleResourceNotFoundException() {
        var ex = new ResourceNotFoundException("No encontrado");
        var response = handler.handleResourceNotFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("No encontrado"));
    }

    @Test
    void testHandleDataIntegrityViolation() {
        var response = handler.handleDataIntegrity(new DataIntegrityViolationException("duplicado"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Ya existe un cliente con los mismos datos únicos (identificación, etc.)", response.getBody().get("mensaje"));
    }

    @Test
    void testHandleGeneral() {
        var response = handler.handleGeneral(new RuntimeException("fallo"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody().get("mensaje"));
    }

    @Test
    void testHandleMethodNotSupported() {
        var response = handler.handleMethodNotSupported(new HttpRequestMethodNotSupportedException("PATCH"));
        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertEquals("Método HTTP no permitido para esta ruta", response.getBody().get("mensaje"));
    }
}
