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

    /* ADD */
    public ResponseEntity<String> crearNuevoModulo(ModuloEntity moduloEntity) {
        return repositoryModulo.crearNuevoModulo(moduloEntity);
    }

    /* PUT */
    public ResponseEntity<String> actualizarDatosEnModulo(ModuloEntity moduloEntity) {
        return repositoryModulo.actualizarDatosEnModulo(moduloEntity);
    }

    /* DELETE/CodigoModulo */
    public ResponseEntity<String> bajaDeUnModuloDadoElCodigo(String codigoModulo) {
        return repositoryModulo.bajaDeUnModuloDadoElCodigo(codigoModulo);
    }

    /* AsignarAlumnosAModulo/codigoModulo - dniAlumno */
    public ResponseEntity<String> asignarAlumnosAModulo(String codigoModulo, String dniAlumno) {
        return repositoryModulo.asignarAlumnosAModulo(codigoModulo, dniAlumno);
    }

    /* AsignarAsignaturasAModulo/codigoModulo - codigoAsignatura */
    public ResponseEntity<String> asignarAsignaturasAModulo(String codigoModulo,
            String codigoAsignatura) {
        return repositoryModulo.asignarAsignaturasAModulo(codigoModulo, codigoAsignatura);
    }

    /* DesasignarAlumnosDeModulo/codigoModulo - dniAlumno */
    public ResponseEntity<String> desasignarAlumnoDeModulo(String codigoModulo, String dniAlumno) {
        return repositoryModulo.desasignarAlumnoDeModulo(codigoModulo, dniAlumno);
    }

    /* DesasignarAsignaturasDeModulo/codigoModulo - codigoAsignatura */
    public ResponseEntity<String> desasignarAsignaturaDeModulo(String codigoModulo, String codigoAsignatura) {
        return repositoryModulo.desasignarAsignaturaDeModulo(codigoModulo, codigoAsignatura);
    }

    public ResponseEntity<?> buscarModuloDadoElCodigo(String codigoModulo) {
        return repositoryModulo.buscarModuloDadoElCodigo(codigoModulo);
    }

    public ResponseEntity<?> obtenerTodasLasAsignaturasDeUnModulo(String codigoModulo) {
        return repositoryModulo.obtenerTodasLasAsignaturasDeUnModulo(codigoModulo);
    }

    public ResponseEntity<?> obtenerTodosLosModulosDeUnaAsignatura(String codigoAsignatura) {
        return repositoryModulo.obtenerTodosLosModulosDeUnaAsignatura(codigoAsignatura);
    }

    public ResponseEntity<?> obtenerTodosLosModulosDeUnAlumno(String dniAlumno) {
        return repositoryModulo.obtenerTodosLosModulosDeUnAlumno(dniAlumno);
    }

}
