Feature: API de cuentas y movimientos

  Background:
    * def clienteUrl = karate.properties['baseUrl.clientes'] ? 'http://localhost:8081'
    * def cuentasUrl  = karate.properties['baseUrl.cuentas']  ? 'http://localhost:8082'
    * url cuentasUrl
    * configure headers = { Content-Type: 'application/json' }

  Scenario: crear cuenta corriente para Jose Lema
    Given request
      """
      { "numeroCuenta":"585545", "tipoCuenta":"Corriente",
        "saldoInicial":1000, "estado":true, "clienteId": #? getClienteId(clienteUrl,'Jose Lema') }
      """
    When method POST
    Then status 201
    * def cuentaId = $.id

  Scenario: depósito de 600
    Given path '/cuentas', cuentaId, '/movimientos'
    And request { "monto": 600 }
    When method POST
    Then status 201
    And match $.saldoDisponible == 1600
