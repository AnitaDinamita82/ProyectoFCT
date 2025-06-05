Feature: Alta usuario

    Scenario: Alta de un usuario nuevo 
    Given un usuario con login "anabelen.madrid" y contraseña "11111"
    When se realiza una peticion POST a "/auth/signup" con los siguientes datos:
        """json
        {
            "login": "juancarlos.alonso",
            "password": "22222",
            "dni": "23035116",
            "rol": "USER"
        }
        """
    Then el codigo de respuesta debe ser 201
    And la respuesta debe contener los datos del nuevo usuario creado
