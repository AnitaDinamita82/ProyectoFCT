package myProyectoDAW.gestionInstituciones.applications.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.adapters.entitys.ModuloEntity;
import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryModulo;
import myProyectoDAW.gestionInstituciones.domain.models.Modulo;

@Service
public class ModuloService {

    @Autowired
    private RepositoryModulo repositoryModulo;

    /* Listar todos los módulos de la BD */
    public List<Modulo> obtenerTodosLosModulos() {
        return repositoryModulo.obtenerTodosLosModulos();
    }

    /* Añade un nuevo modulo */
    public ResponseEntity<String> crearNuevoModulo(ModuloEntity moduloEntity) {
        return repositoryModulo.crearNuevoModulo(moduloEntity);
    }

    /* Actualiza los dados de un modulo en concreto */
    public ResponseEntity<String> actualizarDatosEnModulo(ModuloEntity moduloEntity) {
        return repositoryModulo.actualizarDatosEnModulo(moduloEntity);
    }

    /* Elimina un modulo dado un su codigo */
    public ResponseEntity<String> bajaDeUnModuloDadoElCodigo(String codigoModulo) {
        return repositoryModulo.bajaDeUnModuloDadoElCodigo(codigoModulo);
    }

    /* Asigna a un determinado modulo un alumno (Relacion modulo - alumno) */
    public ResponseEntity<String> asignarAlumnosAModulo(String codigoModulo, String dniAlumno) {
        return repositoryModulo.asignarAlumnosAModulo(codigoModulo, dniAlumno);
    }

    /*
     * Asigna a un determinado modulo una asignatura (Relacion nodulo - asignatura)
     */
    public ResponseEntity<String> asignarAsignaturasAModulo(String codigoModulo,
            String codigoAsignatura) {
        return repositoryModulo.asignarAsignaturasAModulo(codigoModulo, codigoAsignatura);
    }

    /* Dado un DNI de un alumno lo quita de un modulo en concreto */
    public ResponseEntity<String> desasignarAlumnoDeModulo(String codigoModulo, String dniAlumno) {
        return repositoryModulo.desasignarAlumnoDeModulo(codigoModulo, dniAlumno);
    }

    /* Dado un codigo de asignatura la quita de un modulo en concreto */
    public ResponseEntity<String> desasignarAsignaturaDeModulo(String codigoModulo, String codigoAsignatura) {
        return repositoryModulo.desasignarAsignaturaDeModulo(codigoModulo, codigoAsignatura);
    }

    /* Busca un modulo dado su codigo y lo obtiene */
    public ResponseEntity<?> buscarModuloDadoElCodigo(String codigoModulo) {
        return repositoryModulo.buscarModuloDadoElCodigo(codigoModulo);
    }

    /* Obtenemos todas las asignaturas de un modulo en concreto */
    public ResponseEntity<?> obtenerTodasLasAsignaturasDeUnModulo(String codigoModulo) {
        return repositoryModulo.obtenerTodasLasAsignaturasDeUnModulo(codigoModulo);
    }

    /* Obtenemos todos los dnis de los alumnos asociados a un modulo en concreto */
    public ResponseEntity<?> obtenerTodosLosAlumnosDeUnModulo(String codigoModulo) {
        return repositoryModulo.obtenerTodosLosAlumnosDeUnModulo(codigoModulo);
    }

    /* Obtenemos todos los modulos de una asignatura en concreto */
    public ResponseEntity<?> obtenerTodosLosModulosDeUnaAsignatura(String codigoAsignatura) {
        return repositoryModulo.obtenerTodosLosModulosDeUnaAsignatura(codigoAsignatura);
    }

    /* Obtenemos todos los modulos a los que esté asignado un alumno */
    public ResponseEntity<?> obtenerTodosLosModulosDeUnAlumno(String dniAlumno) {
        return repositoryModulo.obtenerTodosLosModulosDeUnAlumno(dniAlumno);
    }

}
