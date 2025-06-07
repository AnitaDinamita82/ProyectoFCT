package myProyectoDAW.gestionInstituciones.applications.ports;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import myProyectoDAW.gestionInstituciones.adapters.entitys.ModuloEntity;
import myProyectoDAW.gestionInstituciones.domain.models.Modulo;

@Repository
public interface RepositoryModulo {

    public List<Modulo> obtenerTodosLosModulos();

    public ResponseEntity<String> crearNuevoModulo(ModuloEntity moduloEntity);

    public ResponseEntity<String> actualizarDatosEnModulo(ModuloEntity moduloEntity);

    public ResponseEntity<String> bajaDeUnModuloDadoElCodigo(String codigoModulo);

    public ResponseEntity<String> asignarAlumnosAModulo(String codigoModulo, String dniAlumno);

    public ResponseEntity<String> asignarAsignaturasAModulo(String codigoModulo, String codigoAsignatura);

    public ResponseEntity<String> desasignarAlumnoDeModulo(String codigoModulo, String dniAlumno);

    public ResponseEntity<String> desasignarAsignaturaDeModulo(String codigoModulo, String codigoAsignatura);

    public ResponseEntity<?> buscarModuloDadoElCodigo(String codigoModulo);

    public ResponseEntity<?> obtenerTodasLasAsignaturasDeUnModulo(String codigoModulo);

    public ResponseEntity<?> obtenerTodosLosModulosDeUnaAsignatura(String codigoAsignatura);

    public ResponseEntity<?> obtenerTodosLosModulosDeUnAlumno(String dniAlumno);

}
