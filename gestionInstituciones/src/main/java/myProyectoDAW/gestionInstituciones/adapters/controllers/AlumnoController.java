package myProyectoDAW.gestionInstituciones.adapters.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myProyectoDAW.gestionInstituciones.adapters.dtos.AlumnoDTO;
import myProyectoDAW.gestionInstituciones.applications.services.AlumnoService;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;

@RestController
@RequestMapping("{version}/alumnos")
public class AlumnoController {

    @Autowired
    private AlumnoService alumnoService;

    /* -- ENDPOINTS GET -- */

    // LISTAR ALUMNOS //
    @GetMapping("/listar")
    public ResponseEntity<List<Alumno>> listarAlumnos() {

        List<Alumno> alumnos = alumnoService.listarAlumnos();

        return new ResponseEntity<>(alumnos, HttpStatus.OK);
    }

    // BUSCAR A UN ALUMNO POR DNI //
    @GetMapping("/buscar/{dniAlumno}")
    public ResponseEntity<Alumno> encontrarSiExisteAlumno(@PathVariable String dniAlumno) {

        Alumno alumno = alumnoService.encontrarSiExisteAlumno(dniAlumno);

        if (alumno != null) {
            return ResponseEntity.ok(alumno); // Devuelve el alumno con código 200 OK
        } else {
            return ResponseEntity.notFound().build(); // Devuelve código 404 Not Found si no se encuentra
        }
    }

    /* -- ENDPOINTS POST -- */

    // ALTA DE UN ALUMNO //
    @PostMapping("/alta")
    public ResponseEntity<?> altaAlumno(@RequestBody AlumnoDTO alumnoDTO) {

        Alumno alumno = alumnoDTO.convertirDTOAEntity();

        if (alumnoService.existe(alumno.getDni())) {
            return new ResponseEntity<>("El alumno con DNI: " + alumno.getDni() + " ya existe en el sistema.",
                    HttpStatus.CONFLICT);
        } else {
            alumnoService.darDeAltaUnAlumno(alumno);
            return new ResponseEntity<>(alumno, HttpStatus.CREATED);
        }
    }

    /* -- ENDPOINTS DELETE -- */

    // ELIMINAR A UN ALUMNO DADO SU DNI //
    @DeleteMapping("/baja/{dniAlumno}")
    public ResponseEntity<String> bajaAlumno(@PathVariable String dniAlumno) {

        Alumno alumno = alumnoService.encontrarSiExisteAlumno(dniAlumno);

        if (alumno == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se puede dar de baja un alumno que no existe.");
        }

        Boolean bajaExitosa = alumnoService.darDeBajaUnAlumno(dniAlumno);

        if (bajaExitosa) {
            return ResponseEntity.ok("Alumno dado de baja correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se puede dar de baja al alumno porque esta matriculado en alguna asignatura.");
        }
    }

    /* -- ENDPOINTS PUT -- */

    // ACTUALIZAR DATOS DE UN ALUMNO //
    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarAlumno(@RequestBody AlumnoDTO alumnoDTO) {

        Alumno alumno = alumnoDTO.convertirDTOAEntity();
        return alumnoService.actualizarAlumno(alumno);
    }
}
