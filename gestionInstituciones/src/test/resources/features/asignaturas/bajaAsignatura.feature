 Feature: Baja de Asignatura

    Scenario: Baja de una asignatura existente con alumnos asociados (Usuario autenticado)
        Given un usuario administrador autenticado con login "anabelen.madrid" y contraseña "11111"
        Given una asignatura con codigo "QUI01" existente en el sistema
        Given la signatura con codigo "QUI01" tiene alumnos asociados
        When se realiza una peticion DELETE a "/1.0/asignaturas/baja/QUI01"
        Then el codigo de respuesta debe ser 409
        And se imprimira un mensaje indicando "No se puede dar de baja la asignatura porque tiene alumnos asociados."

    Scenario: Baja de una asignatura existente sin alumnos asociados (Usuario autenticado)
        Given un usuario administrador autenticado con login "anabelen.madrid" y contraseña "11111"
        Given una asignatura con codigo "BIO01" existente en el sistema
        Given la asignatura con codigo "BIO01" no tiene alumnos asociados
        When se realiza una peticion DELETE a "/1.0/asignaturas/baja/BIO01"
        Then el codigo de respuesta debe ser 200
        Then se realiza una peticion GET a "/1.0/asignaturas/listar"
        And se comprobara que la asignatura no existe mostrando la lista de asignaturas

    Scenario: Baja de una asignatura no existente (Usuario autenticado)
        Given un usuario administrador autenticado con login "anabelen.madrid" y contraseña "11111"
        Given una asignatura con codigo "ETI01" no existente en el sistema
        When se realiza una peticion DELETE a "/1.0/asignaturas/baja/ETI01"
        Then el codigo de respuesta debe ser 404
        And se imprimira un mensaje indicando "No se puede dar de baja una asignatura que no existe."