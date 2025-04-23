# src/test/resources/e2e/health.feature
Feature: Verificar que el micro-servicio esté saludable

  Background:
    * url baseUrl
    * path 'actuator', 'health'

  Scenario: /actuator/health responde UP
    When method GET
    Then status 200
    And match response.status == 'UP'
