package myProyectoDAW.gestionInstituciones.applications.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryAlumno;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;

@Service
public class AlumnoService {

    @Autowired
    private RepositoryAlumno repositoryAlumno;

    /* Listar todos los alumnos */
    public List<Alumno> listarAlumnos() {

        return repositoryAlumno.listarAlumnos();
    }

    /* Añadir un nuevo alumno */
    public Alumno darDeAltaUnAlumno(Alumno alumno) {

        return repositoryAlumno.altaAlumno(alumno);
    }

    /* Dar de baja un alumno dado su dni */
    public Boolean darDeBajaUnAlumno(String dniAlumno) {

        return (repositoryAlumno.eliminarAlumnoDadoDni(dniAlumno));
    }

    /* Actualizar un alumno */
    public ResponseEntity<String> actualizarAlumno(Alumno alumno) {
        return repositoryAlumno.actualizarAlumno(alumno);
    }

    /* Encontrar un alumno dado un DNI */
    public Alumno encontrarSiExisteAlumno(String dniAlumno) {
        return repositoryAlumno.encontrarSiExisteAlumno(dniAlumno);
    }

    /* Nos devuelve si el alumno ya esxiste */
    public boolean existe(String dniAlumno) {
        return repositoryAlumno.existe(dniAlumno);
    }

}
