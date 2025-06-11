package myProyectoDAW.gestionInstituciones.adapters.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myProyectoDAW.gestionInstituciones.applications.services.EstadisticasService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Clase controladora REST para la obtención de estadísticas relacionadas con la
 * gestión de instituciones.
 */
@RestController
@RequestMapping("{version}/estadisticas")
public class EstadisticasController {

    @Autowired
    private EstadisticasService estadisticasService;

    /* -- ENDPOINTS GET -- */

    // ENDPOINT PARA OBTENER EL RECUENTO DE LOS ALUMNOS MATRICULADOS POR ASIGNATURA
    // //
    @GetMapping("/alumnosPorAsignatura")
    public ResponseEntity<?> numeroAlumnosPorAsignatura() {
        return estadisticasService.numeroAlumnosPorAsignatura();
    }

    // ENDPOINT PARA OBTENER UNA LISTA DE ALUMNOS CON EL NUMERO TOTAL DE SUS
    // ASIGNATURAS MATRICULADS //
    @GetMapping("/alumnosConMasAsignaturas")
    public ResponseEntity<?> alumnosConMasAsignaturas() {
        return estadisticasService.alumnosConMasAsignaturas();
    }
}
