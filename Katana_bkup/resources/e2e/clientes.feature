Feature: API de clientes

  Background:
    * url karate.properties['baseUrl'] ? 'http://localhost:8081'
    * pathPrefix '/api/clientes'

  Scenario: crear un cliente
    Given request
      """
      {
        "nombre":"Jose Lema",
        "genero":"Masculino",
        "edad":43,
        "identificacion":"98254785",
        "direccion":"Otavalo sn y principal",
        "telefono":"098254785",
        "contrasena":"1234",
        "estado":true
      }
      """
    When method POST
    Then status 201
    And match $.nombre == 'Jose Lema'
    * def id = $.id

  Scenario: obtener cliente por id
    Given path id
    When method GET
    Then status 200
    And match $.id == id
