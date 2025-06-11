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

/**
 * Clase controladora REST para la gestión de matrículas (relación *
 * Alumno-Asignatura).
 * Expone los endpoints HTTP para realizar operaciones relacionadas con la
 * matriculación y desmatriculación de alumnos en asignaturas.
 */
@RestController
@RequestMapping("{version}/matricula")
public class MatriculaController {

    @Autowired
    private MatriculaService alumnoAsignaturaService;

    /* -- ENDPOINTS GET -- */

    // ENDPOINT PARA LISTAR TODAS LAS ASIGNATURAS DE MANERA GENERAL EN LAS QUE UN
    // ALUMNO ESTA MATRICULADO //
    @GetMapping("/listarAsignaturasDeAlumno/{dniAlumno}")
    public List<Asignatura> listarAsignaturasDeAlumno(@PathVariable String dniAlumno) {

        return alumnoAsignaturaService.listarAsignaturasDeAlumno(dniAlumno);
    }

    // ENDPOINT PARA OBTENER TODOS LOS ALUMNOS MATRICULADOS DE UNA ASIGNATURA DE
    // MANERA GENERAL //
    @GetMapping("/obtenerAlumnosMatriculados/{codigoAsignatura}")
    public List<Alumno> obtenerAlumnosMatriculados(@PathVariable String codigoAsignatura) {
        return alumnoAsignaturaService.obtenerAlumnosMatriculados(codigoAsignatura);
    }

    // ENDPINT PARA CONTAR TODOS LOS ALUMNOS MATRICULADOS DE UNA ASIGNATURA POR
    // MODULO //
    @GetMapping("/alumnosMatriculadosPorModulo/{codigoAsignatura}/{codigoModulo}")
    public ResponseEntity<?> alumnosMatriculadosEnAsignaturaPorModulos(
            @PathVariable String codigoAsignatura,
            @PathVariable String codigoModulo) {
        return alumnoAsignaturaService.alumnosMatriculadosEnAsignaturaPorModulos(codigoAsignatura, codigoModulo);
    }

    /* -- ENDPOINTS POST -- */

    // ENDPOINT PARA MATRICULAR A UN ALUMNO //
    @PostMapping("/matricularAlumno")
    public ResponseEntity<String> matricularAlumno(@RequestBody MatriculaDTO matriculaDTO) {

        return alumnoAsignaturaService.matricularAlumno(matriculaDTO.getDniAlumno(),
                matriculaDTO.getCodigoAsignatura());
    }

    /* -- ENDPOINTS DELETE -- */

    // ENDPOINT PARA ELIMINAR UNA ASIGNATURA EN CONCRETO DE SU MATRICULA //
    @DeleteMapping("/desmatricularAlumno")
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumno(@RequestBody MatriculaDTO matriculaDTO) {
        return alumnoAsignaturaService.bajaAsignaturaDeMatriculaDeAlumno(matriculaDTO.getDniAlumno(),
                matriculaDTO.getCodigoAsignatura());
    }

    // ENDPOINT PARA ELIMINAR TODA LA MATRICULA COMPLETA//
    @DeleteMapping("desmatricularTodasLasAsignaturasDeAlumno/{dniAlumno}")
    public ResponseEntity<String> desmatricularTodasLasAsignaturasDeUnAlumno(@PathVariable String dniAlumno) {
        return alumnoAsignaturaService.desmatricularTodasLasAsignaturasDeUnAlumno(dniAlumno);
    }

    // ENDPOINT PARA DESMATRICULAR A UN ALUMNO DE UNA ASIGNATURA, CON LOGICA
    // ADICIONAL PARA GESTIONAR SI TODAVIA TIENE MAS ASIGNATURAS DEL MISMO MODULO //
    @DeleteMapping("/desmatricularAlumnoModulo")
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumnoConGestionDeModulo(
            @RequestBody MatriculaDTO matriculaDTO) {
        return alumnoAsignaturaService.bajaAsignaturaDeMatriculaDeAlumnoConGestionDeModulo(matriculaDTO.getDniAlumno(),
                matriculaDTO.getCodigoAsignatura(), matriculaDTO.getCodigoModulo());
    }

}
