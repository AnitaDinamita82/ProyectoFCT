package myProyectoDAW.gestionInstituciones.applications.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryAsignatura;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

/**
 * Capa de Servicio para la gestión de Asignaturas. Es quien implementa la
 * logica de
 * negocio para las operaciones relacionadas con las asignaturas
 * Utiliza la inyección de dependencia a través de << RepositoryAsgnatura >>
 * para
 * delegar las interacciones con la capa de persistencia,
 * manteniendo una clara separación de responsabilidades en la arquitectura de
 * la aplicación.
 */
@Service
public class AsignaturaService {

    @Autowired
    private RepositoryAsignatura repositoryAsignatura;

    /* Definicion del metodo para listar todas las asignaturas */
    public List<Asignatura> listarAsignaturas() {
        return repositoryAsignatura.listarAsignaturas();
    }

    /* Definición del método para añadir una nueva asignatura */
    public Asignatura darDeAltaUnaAsignatura(Asignatura asignatura) {
        return repositoryAsignatura.altaAsignatura(asignatura);
    }

    /* Defiicion del metodo para elimiar una asignatura dado un codigo */
    public Boolean darDeBajaUnaAsignatura(String codigoAsignatura) {
        return repositoryAsignatura.eliminarAsignaturaDadoCodigo(codigoAsignatura);
    }

    /* Definicion del metodo para actualizar una asignatura */
    public ResponseEntity<String> actualizarAsignatura(Asignatura asignatura) {
        return repositoryAsignatura.actualizarAsignatura(asignatura);
    }

    /*
     * Definicion del metodo para encontrar una asignatura dado un codigo y
     * devolverla
     */
    public Asignatura encontrarSiExisteAsignatura(String codigoAsignatura) {
        return repositoryAsignatura.encontrarSiExisteAsignatura(codigoAsignatura);
    }

    /* Definición del metodo para comprobar si la asignatura ya existe */
    public boolean existe(String codigoAsignatura) {
        return repositoryAsignatura.existe(codigoAsignatura);
    }
}
