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

    /* -- ENDPOINTS GET -- */

    // LISTAR TODAS LAS ASIGNATURAS DE UN ALUMNO //
    @GetMapping("/listarAsignaturasDeAlumno/{dniAlumno}")
    public List<Asignatura> listarAsignaturasDeAlumno(@PathVariable String dniAlumno) {

        return alumnoAsignaturaService.listarAsignaturasDeAlumno(dniAlumno);
    }

    // OBTENER TODOS LOS ALUMNOS MATRICULADOS DE UNA ASIGNATURA DE MANERA GENERAL //
    @GetMapping("/obtenerAlumnosMatriculados/{codigoAsignatura}")
    public List<Alumno> obtenerAlumnosMatriculados(@PathVariable String codigoAsignatura) {
        return alumnoAsignaturaService.obtenerAlumnosMatriculados(codigoAsignatura);
    }

    // CONTAR TODOS LOS ALUMNOS MATRICULADOS DE UNA ASIGNATURA POR MODULO //
    @GetMapping("/alumnosMatriculadosPorModulo/{codigoAsignatura}/{codigoModulo}")
    public ResponseEntity<?> alumnosMatriculadosEnAsignaturaPorModulos(
            @PathVariable String codigoAsignatura,
            @PathVariable String codigoModulo) {
        return alumnoAsignaturaService.alumnosMatriculadosEnAsignaturaPorModulos(codigoAsignatura, codigoModulo);
    }
    /* -- ENDPOINTS POST -- */

    // MATRICULAR A UN ALUMNO //
    @PostMapping("/matricularAlumno")
    public ResponseEntity<String> matricularAlumno(@RequestBody MatriculaDTO matriculaDTO) {

        return alumnoAsignaturaService.matricularAlumno(matriculaDTO.getDniAlumno(),
                matriculaDTO.getCodigoAsignatura());
    }

    /* -- ENDPOINTS DELETE -- */

    // ELIMINAR LA MATRICULA DE UN ALUMNO A NIVEL ASIGNATURA //
    @DeleteMapping("/desmatricularAlumno")
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumno(@RequestBody MatriculaDTO matriculaDTO) {
        return alumnoAsignaturaService.bajaAsignaturaDeMatriculaDeAlumno(matriculaDTO.getDniAlumno(),
                matriculaDTO.getCodigoAsignatura());
    }

    // ELIMINAR TODAS LA MATRICULAS //
    @DeleteMapping("desmatricularTodasLasAsignaturasDeAlumno/{dniAlumno}")
    public ResponseEntity<String> desmatricularTodasLasAsignaturasDeUnAlumno(@PathVariable String dniAlumno) {
        return alumnoAsignaturaService.desmatricularTodasLasAsignaturasDeUnAlumno(dniAlumno);
    }

    // ELIMINA LA ASIGNATURA QUE SE PASA COMO PARAMETRO PERO CONTROLAMOS SI TENEMOS
    // MAS DEL MISMO MODULO //

    @DeleteMapping("/desmatricularAlumnoModulo")
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumnoConGestionDeModulo(
            @RequestBody MatriculaDTO matriculaDTO) {
        return alumnoAsignaturaService.bajaAsignaturaDeMatriculaDeAlumnoConGestionDeModulo(matriculaDTO.getDniAlumno(),
                matriculaDTO.getCodigoAsignatura(), matriculaDTO.getCodigoModulo());
    }

}
