Feature: Alta de Asignatura

    Scenario: Alta de una nueva asignatura (Usuario autenticado)
        Given un usuario administrador autenticado con login "anabelen.madrid" y contraseña "11111"
        When se realiza una peticion POST a "/1.0/asignaturas/alta" con los siguientes datos:
            """json
            {
                "codigo": "INF01",
                "nombre": "Informática",
                "descripcion": "Estudio de la computación y el procesamiento de información."
            }
            """
        Then el codigo de respuesta debe ser 201
        And la respuesta debe contener los datos de la asignatura creada