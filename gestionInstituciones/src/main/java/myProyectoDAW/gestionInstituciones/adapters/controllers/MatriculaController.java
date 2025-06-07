package myProyectoDAW.gestionInstituciones.adapters.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myProyectoDAW.gestionInstituciones.adapters.dtos.MatriculaDTO;
import myProyectoDAW.gestionInstituciones.applications.services.MatriculaService;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("{version}/matricula")
public class MatriculaController {

    @Autowired
    private MatriculaService alumnoAsignaturaService;

    /* GET */
    @GetMapping("/listarAsignaturasDeAlumno/{dniAlumno}")
    public List<Asignatura> listarAsignaturasDeAlumno(@PathVariable String dniAlumno) {

        return alumnoAsignaturaService.listarAsignaturasDeAlumno(dniAlumno);
    }

    /* GET */
    @GetMapping("/obtenerAlumnosMatriculados/{codigoAsignatura}")
    public List<Alumno> obtenerAlumnosMatriculados(@PathVariable String codigoAsignatura) {
        return alumnoAsignaturaService.obtenerAlumnosMatriculados(codigoAsignatura);
    }

    /* POST */

    @PostMapping("/matricularAlumno")
    public ResponseEntity<String> matricularAlumno(@RequestBody MatriculaDTO matriculaDTO) {

        return alumnoAsignaturaService.matricularAlumno(matriculaDTO.getDniAlumno(),
                matriculaDTO.getCodigoAsignatura());
    }

    /* DELETE */
    @DeleteMapping("/desmatricularAlumno")
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumno(@RequestBody MatriculaDTO matriculaDTO) {
        return alumnoAsignaturaService.bajaAsignaturaDeMatriculaDeAlumno(matriculaDTO.getDniAlumno(),
                matriculaDTO.getCodigoAsignatura());
    }

    @DeleteMapping("desmatricularTodasLasAsignaturasDeAlumno/{dniAlumno}")
    public ResponseEntity<String> desmatricularTodasLasAsignaturasDeUnAlumno(@PathVariable String dniAlumno) {
        return alumnoAsignaturaService.desmatricularTodasLasAsignaturasDeUnAlumno(dniAlumno);
    }
}
