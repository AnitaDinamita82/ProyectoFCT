Feature: Listar Usuarios

    Scenario: Listar todos los usuarios (Usuario autenticado)
        Given un usuario administrador autenticado con login "anabelen.madrid" y contraseña "11111"
        When se realiza una peticion GET a "/1.0/usuarios/listar"
        Then el codigo de respuesta debe ser 200
        And la respuesta debe de ser una lista de usuarios