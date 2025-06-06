package myProyectoDAW.gestionInstituciones.adapters.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myProyectoDAW.gestionInstituciones.applications.services.EstadisticasService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("{version}/estadisticas")
public class EstadisticasController {

    @Autowired
    private EstadisticasService estadisticasService;

    /* GET */
    @GetMapping("/alumnosPorAsignatura")
    public ResponseEntity<?> numeroAlumnosPorAsignatura() {
        return estadisticasService.numeroAlumnosPorAsignatura();
    }

    @GetMapping("/alumnosConMasAsignaturas")
    public ResponseEntity<?> alumnosConMasAsignaturas() {
        return estadisticasService.alumnosConMasAsignaturas();
    }
}
