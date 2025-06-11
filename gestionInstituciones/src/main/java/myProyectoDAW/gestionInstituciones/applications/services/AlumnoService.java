package myProyectoDAW.gestionInstituciones.applications.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryAlumno;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;

/**
 * Capa de Servicio para la gestión de Alumnos. Es quien implementa la logica de
 * negocio para las operaciones relacionadas con los alumnos
 * Utiliza la inyección de dependencia a través de << RepositoryAlumno >> para
 * delegar las interacciones con la capa de persistencia,
 * manteniendo una clara separación de responsabilidades en la arquitectura de
 * la aplicación.
 */

@Service
public class AlumnoService {

    @Autowired
    private RepositoryAlumno repositoryAlumno;

    /* Definición del metodo para listar todos los alumnos */
    public List<Alumno> listarAlumnos() {

        return repositoryAlumno.listarAlumnos();
    }

    /* Definición del metodo para dar de alta un nuevo alumno */
    public Alumno darDeAltaUnAlumno(Alumno alumno) {

        return repositoryAlumno.altaAlumno(alumno);
    }

    /* Definición del metodo para eliminar a un alumno dado su DNI */
    public Boolean darDeBajaUnAlumno(String dniAlumno) {

        return (repositoryAlumno.eliminarAlumnoDadoDni(dniAlumno));
    }

    /* Definicion del metodo para actualizar los datos de un alumno */
    public ResponseEntity<String> actualizarAlumno(Alumno alumno) {
        return repositoryAlumno.actualizarAlumno(alumno);
    }

    /* Definición del metodo para encontra si existe un alumno y devovlerlo */
    public Alumno encontrarSiExisteAlumno(String dniAlumno) {
        return repositoryAlumno.encontrarSiExisteAlumno(dniAlumno);
    }

    /* Definicion del metodo que nos devuelve si un usuario existe o no */
    public boolean existe(String dniAlumno) {
        return repositoryAlumno.existe(dniAlumno);
    }

}
