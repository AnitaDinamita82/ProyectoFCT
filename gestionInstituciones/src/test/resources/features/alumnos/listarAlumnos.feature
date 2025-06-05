Feature: Listar Alumnos

    Scenario: Listar todos los alumnos (Usuario autenticado)
        Given un usuario administrador autenticado con login "anabelen.madrid" y contraseña "11111"
        When se realiza una peticion GET a "/1.0/alumnos/listar"
        Then el codigo de respuesta debe ser 200
        And la respuesta debe de ser una lista de alumnos