Feature: Alta de Alumno

    Scenario: Alta de un nuevo alumno (Usuario autenticado)
        Given un usuario administrador autenticado con login "anabelen.madrid" y contraseña "11111"
        When se realiza una peticion POST a "/1.0/alumnos/alta" con los siguientes datos:
                """json
                {
                    "dni": "23037852",
                    "nombre": "Laura",
                    "apellido1": "Madrid",
                    "apellido2": "Garcia"
                }
                """
        Then el codigo de respuesta debe ser 201
        And la respuesta debe contener los datos del alumno creado