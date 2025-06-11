package myProyectoDAW.gestionInstituciones.applications.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.adapters.entitys.ModuloEntity;
import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryModulo;
import myProyectoDAW.gestionInstituciones.domain.models.Modulo;

/**
 * Capa de Servicio para la gestión de Modulos. Es quien implementa la logica de
 * negocio para las operaciones relacionadas con los modulos
 * Utiliza la inyección de dependencia a través de << RepositoryModulo >> para
 * delegar las interacciones con la capa de persistencia,
 * manteniendo una clara separación de responsabilidades en la arquitectura de
 * la aplicación.
 */
@Service
public class ModuloService {

    @Autowired
    private RepositoryModulo repositoryModulo;

    /* Definiion del metodo para listar todos los módulos de la BD */
    public List<Modulo> obtenerTodosLosModulos() {
        return repositoryModulo.obtenerTodosLosModulos();
    }

    /* Definicion del metodo que añade un nuevo modulo */
    public ResponseEntity<String> crearNuevoModulo(ModuloEntity moduloEntity) {
        return repositoryModulo.crearNuevoModulo(moduloEntity);
    }

    /* Definicion del metodo que actualiza los dados de un modulo en concreto */
    public ResponseEntity<String> actualizarDatosEnModulo(ModuloEntity moduloEntity) {
        return repositoryModulo.actualizarDatosEnModulo(moduloEntity);
    }

    /* Definicion del metodo que elimina un modulo dado un su codigo */
    public ResponseEntity<String> bajaDeUnModuloDadoElCodigo(String codigoModulo) {
        return repositoryModulo.bajaDeUnModuloDadoElCodigo(codigoModulo);
    }

    /*
     * Definicion del metodo que asigna a un determinado modulo un alumno (Relacion
     * modulo - alumno)
     */
    public ResponseEntity<String> asignarAlumnosAModulo(String codigoModulo, String dniAlumno) {
        return repositoryModulo.asignarAlumnosAModulo(codigoModulo, dniAlumno);
    }

    /*
     * Definicion del metodo que asigna a un determinado modulo una asignatura
     * (Relacion Modulo - asignatura)
     */
    public ResponseEntity<String> asignarAsignaturasAModulo(String codigoModulo,
            String codigoAsignatura) {
        return repositoryModulo.asignarAsignaturasAModulo(codigoModulo, codigoAsignatura);
    }

    /*
     * Definicion del metodo por que dado un DNI de un alumno lo elimina de un
     * modulo en concreto
     */
    public ResponseEntity<String> desasignarAlumnoDeModulo(String codigoModulo, String dniAlumno) {
        return repositoryModulo.desasignarAlumnoDeModulo(codigoModulo, dniAlumno);
    }

    /*
     * Definicion del metodo por el que dado un codigo de asignatura la quita de un
     * modulo en concreto
     */
    public ResponseEntity<String> desasignarAsignaturaDeModulo(String codigoModulo, String codigoAsignatura) {
        return repositoryModulo.desasignarAsignaturaDeModulo(codigoModulo, codigoAsignatura);
    }

    /*
     * Definicion del metodo por que sb Busca un modulo dado su codigo y lo obtiene
     */
    public ResponseEntity<?> buscarModuloDadoElCodigo(String codigoModulo) {
        return repositoryModulo.buscarModuloDadoElCodigo(codigoModulo);
    }

    /*
     * Definicion del metodo por el que obtenemos todas las asignaturas de un modulo
     * en concreto
     */
    public ResponseEntity<?> obtenerTodasLasAsignaturasDeUnModulo(String codigoModulo) {
        return repositoryModulo.obtenerTodasLasAsignaturasDeUnModulo(codigoModulo);
    }

    /*
     * Definicion del metodo por el que obtenemos todos los dnis de los alumnos
     * asociados a un modulo en concreto
     */
    public ResponseEntity<?> obtenerTodosLosAlumnosDeUnModulo(String codigoModulo) {
        return repositoryModulo.obtenerTodosLosAlumnosDeUnModulo(codigoModulo);
    }

    /*
     * Definicion del metodo por que obtenemos todos los modulos de una asignatura
     * en concreto
     */
    public ResponseEntity<?> obtenerTodosLosModulosDeUnaAsignatura(String codigoAsignatura) {
        return repositoryModulo.obtenerTodosLosModulosDeUnaAsignatura(codigoAsignatura);
    }

    /*
     * Definicion del metodo por que obtenemos todos los modulos a los que esté
     * asignado un alumno
     */
    public ResponseEntity<?> obtenerTodosLosModulosDeUnAlumno(String dniAlumno) {
        return repositoryModulo.obtenerTodosLosModulosDeUnAlumno(dniAlumno);
    }

}
