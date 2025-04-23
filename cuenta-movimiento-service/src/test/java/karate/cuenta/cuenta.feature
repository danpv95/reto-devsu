Feature: Validar funcionalidades de Cuenta

  Background:
    * url 'http://localhost:8082/api/cuentas'
    * header Content-Type = 'application/json'

  Scenario: Crear cuenta válida
    Given request { "numeroCuenta": "88888", "tipoCuenta": "AHORROS", "saldoInicial": 1000.00, "estado": true, "clienteId": 1 }
    When method post
    Then status 200
    And match response.numeroCuenta == "88888"

  Scenario: Obtener lista de cuentas
    Given url 'http://localhost:8082/api/cuentas'
    When method get
    Then status 200
    And match response contains any { numeroCuenta: '#string' }
