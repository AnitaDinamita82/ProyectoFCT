Feature: Baja de Usuario

    Scenario: Baja de un usuario existente (Usuario autenticado)
        Given un usuario administrador autenticado con login "anabelen.madrid" y contraseña "11111"
        Given un usuario existente con login "juancarlos.alonso"
        When se realiza una peticion DELETE a "/1.0/usuarios/baja/juancarlos.alonso"
        Then el codigo de respuesta debe ser 200
        And se imprimira un mensaje indicando "Usuario dado de baja correctamente."


    Scenario: Baja de un usuario no existente (Usuario autenticado)
        Given un usuario administrador autenticado con login "anabelen.madrid" y contraseña "11111"
        Given un usuario con login "clara.perez" no existente en el sistema
        When se realiza una peticion DELETE a "/1.0/usuarios/baja/clara.perez"
        Then el codigo de respuesta debe ser 404
        And se imprimira un mensaje indicando "No se puede dar de baja un usuario que no existe."