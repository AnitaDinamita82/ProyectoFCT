package myProyectoDAW.gestionInstituciones.adapters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import myProyectoDAW.gestionInstituciones.adapters.entitys.AlumnoEntity;
import myProyectoDAW.gestionInstituciones.adapters.entitys.AsignaturaEntity;
import myProyectoDAW.gestionInstituciones.adapters.jpas.AlumnoJpaRepository;
import myProyectoDAW.gestionInstituciones.adapters.jpas.AsignaturaJpaRepository;
import myProyectoDAW.gestionInstituciones.applications.ports.RespositoryAlumnosAsignaturas;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

@Component
public class AlumnosAsignaturasAdapter implements RespositoryAlumnosAsignaturas {

    @Autowired
    private AlumnoJpaRepository alumnoJpaRepository;

    @Autowired
    private AsignaturaJpaRepository asignaturaJpaRepository;

    /* Dado el Dni de un alumno listaremos todas sus asignaturas */
    @Override
    public List<Asignatura> listarAsignaturasDeAlumno(String dniAlumno) {

        Optional<AlumnoEntity> alumnoEntityOptional = alumnoJpaRepository.findByDni(dniAlumno);

        AlumnoEntity alumnoEntity = alumnoEntityOptional.get();
        return alumnoEntity.getAsignatura().stream()
                .map(this::convertirEntityAAsignatura)
                .collect(Collectors.toList());
    }

    /*
     * Dado el codigo de una asignatura listaremos todos los alumnos que la tengan
     */
    @Override
    public List<Alumno> obtenerAlumnosMatriculados(String codigoAsignatura) {

        Optional<AsignaturaEntity> asignaturaEntityOptional = asignaturaJpaRepository.findByCodigo(codigoAsignatura);

        AsignaturaEntity asignaturaEntity = asignaturaEntityOptional.get();

        return asignaturaEntity.getAlumnos().stream()
                .map(this::convertirEntityAAlumno)
                .collect(Collectors.toList());

    }

    @Override
    public boolean alumnoTieneAsignaturas(String dniAlumno) {

        List<Asignatura> asignaturas = listarAsignaturasDeAlumno(dniAlumno);
        return !asignaturas.isEmpty(); // Devuelve true si la lista no está vacía
    }

    @Override
    public boolean asignaturasTieneAlumnos(String codigoAsignatura) {

        List<Alumno> alumnos = obtenerAlumnosMatriculados(codigoAsignatura);
        return !alumnos.isEmpty(); // Devolverá true si la lista no está vacia.
    }

    @Override
    @Transactional
    public ResponseEntity<String> matricularAlumno(String dniAlumno, String codigoAsignatura) {

        System.out.println("Estoy dentro de matricular Alumno en el adaprter");
        /* Obtenemos al alumno en cuestion */
        AlumnoEntity alumnoEntity = alumnoJpaRepository.findByDni(dniAlumno).get();

        /* Obtenemos la asignatura en cuestion */
        AsignaturaEntity asignaturaEntity = asignaturaJpaRepository.findByCodigo(codigoAsignatura).get();

        if (alumnoEntity.getAsignatura() == null) {
            alumnoEntity.setAsignatura(new ArrayList<>());
        }

        alumnoEntity.getAsignatura().add(asignaturaEntity);
        alumnoJpaRepository.save(alumnoEntity);

        return new ResponseEntity<>(
                "Alumno matriculado en la asignatura " + codigoAsignatura + " (" + asignaturaEntity.getNombre()
                        + ") correctamente.",
                HttpStatus.OK);
    }

    @Override
    @Transactional
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumno(String dniAlumno, String codigoAsignatura) {

        System.out.println("Estoy dentro bajaAsignaturaMatriculaDeAlumno en el adaprter"); // PUNTO DE CONTROL
        /* Obtenemos al alumno en cuestion */
        AlumnoEntity alumnoEntity = alumnoJpaRepository.findByDni(dniAlumno).get();

        /* Obtenemos la asignatura en cuestion */
        AsignaturaEntity asignaturaEntity = asignaturaJpaRepository.findByCodigo(codigoAsignatura).get();
        alumnoEntity.getAsignatura().remove(asignaturaEntity);
        alumnoJpaRepository.save(alumnoEntity);

        return new ResponseEntity<>(
                "Se le ha dado de baja la asignatura " + codigoAsignatura + " (" + asignaturaEntity.getNombre()
                        + ")  de la matricula correctamente.",
                HttpStatus.OK);

    }
    /* Metodos de conversion */

    private Asignatura convertirEntityAAsignatura(AsignaturaEntity asignaturaEntity) {
        Asignatura asignatura = new Asignatura();
        asignatura.setId(asignaturaEntity.getId());
        asignatura.setCodigo(asignaturaEntity.getCodigo());
        asignatura.setNombre(asignaturaEntity.getNombre());
        asignatura.setDescripcion(asignaturaEntity.getDescripcion());
        return asignatura;
    }

    private Alumno convertirEntityAAlumno(AlumnoEntity alumnoEntity) {
        Alumno alumno = new Alumno();
        alumno.setId(alumnoEntity.getId());
        alumno.setDni(alumnoEntity.getDni());
        alumno.setNombre(alumnoEntity.getNombre());
        alumno.setApellido1(alumnoEntity.getApellido1());
        alumno.setApellido2(alumnoEntity.getApellido2());
        return alumno;
    }
}
