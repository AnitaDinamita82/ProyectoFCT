    Feature: Baja de Alumno

    Scenario: Baja de un alumno existente con asignaturas asociadas (Usuario autenticado)
        Given un usuario administrador autenticado con login "anabelen.madrid" y contraseña "11111"
        Given un alumno con DNI "23048962" existente en el sistema
        Given el alumno con DNI "23048962" tiene asignaturas asociadas
        When se realiza una peticion DELETE a "/1.0/alumnos/baja/23048962"
        Then el codigo de respuesta debe ser 409
        And se imprimira un mensaje indicando "No se puede dar de baja al alumno porque tiene asignaturas asociadas."

    Scenario: Baja de un alumno existente sin asignaturas asociadas (Usuario autenticado)
        Given un usuario administrador autenticado con login "anabelen.madrid" y contraseña "11111"
        Given un alumno con DNI "23035116" existente en el sistema
        Given el alumno con DNI "23035116" no tiene asignaturas asociadas
        When se realiza una peticion DELETE a "/1.0/alumnos/baja/23035116"
        Then el codigo de respuesta debe ser 200
        Then se realiza una peticion GET a "/1.0/alumnos/listar"
        And se comprobara que el alumno no existe mostrando la lista de alumnos

    Scenario: Baja de un alumno no existente (Usuario autenticado)
        Given un usuario administrador autenticado con login "anabelen.madrid" y contraseña "11111"
        Given un alumno con DNI "11111111" no existente en el sistema
        When se realiza una peticion DELETE a "/1.0/alumnos/baja/11111111"
        Then el codigo de respuesta debe ser 404
        And se imprimira un mensaje indicando "No se puede dar de baja un alumno que no existe."