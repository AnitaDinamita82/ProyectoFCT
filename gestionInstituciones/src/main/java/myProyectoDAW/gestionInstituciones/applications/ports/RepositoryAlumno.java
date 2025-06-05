package myProyectoDAW.gestionInstituciones.applications.ports;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import myProyectoDAW.gestionInstituciones.domain.models.Alumno;

/* Operaciones que se pueden realizar con el modelo de dominio */
@Repository
public interface RepositoryAlumno {

        Alumno altaAlumno(Alumno alumno);

        List<Alumno> listarAlumnos();

        Boolean eliminarAlumnoDadoDni(String dniAlumno);

        ResponseEntity<String> actualizarAlumno(Alumno alumno);

        Alumno encontrarSiExisteAlumno(String dniAlumno);

        boolean existe(String dniAlumno);
}
