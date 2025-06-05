Feature: Autenticar usuario

   
    Scenario: Autenticacion exitosa

        Given un usuario con login "anabelen.madrid" y contraseña "11111"
        When el usuario envia una solicitud POST a "/auth/login" con dichas credenciales
        Then el codigo de respuesta debe ser 200
        And la respuesta debe contener un token JWT valido

    Scenario: Autenticacion fallida por credenciales invalidas

        Given un usuario con login "juancarlos.alonso" y contraseña "33333"
        When el usuario envia una solicitud POST a "/auth/login" con dichas credenciales
        Then el codigo de respuesta debe ser 403
        And se imprimira un mensaje indicando "Autenticacion fallida."

    Scenario: Autenticacion fallida por usuario inexistente

        Given un usuario con login "pepito.jimenez" y contraseña "44444"
        When el usuario envia una solicitud POST a "/auth/login" con dichas credenciales
        Then el codigo de respuesta debe ser 403
        And se imprimira un mensaje indicando "Autenticacion fallida."