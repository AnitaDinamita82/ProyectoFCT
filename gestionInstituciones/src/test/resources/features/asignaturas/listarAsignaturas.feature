Feature: Listar Asignaturas

    Scenario: Listar todas las asignaturas (Usuario autenticado)
        Given un usuario administrador autenticado con login "anabelen.madrid" y contraseña "11111"
        When se realiza una peticion GET a "/1.0/asignaturas/listar"
        Then el codigo de respuesta debe ser 200
        And la respuesta debe de ser una lista de asignaturas