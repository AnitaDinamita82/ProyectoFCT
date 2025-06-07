package myProyectoDAW.gestionInstituciones.adapters;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import myProyectoDAW.gestionInstituciones.adapters.entitys.AlumnoEntity;
import myProyectoDAW.gestionInstituciones.adapters.jpas.AlumnoJpaRepository;
import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryAlumno;
import myProyectoDAW.gestionInstituciones.applications.services.MatriculaService;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;

@Component
public class AlumnoAdapter implements RepositoryAlumno {

    @Autowired
    private AlumnoJpaRepository alumnoJpaRepository;

    @Autowired
    private MatriculaService matriculaService;

    private Optional<AlumnoEntity> alumnoEntityOptional;

    @Override
    public Alumno altaAlumno(Alumno alumno) {

        AlumnoEntity alumnoEntity = convertirAlumnoAEntity(alumno);
        return convertirEntityAAlumno(alumnoJpaRepository.save(alumnoEntity));

    }

    @Override
    public List<Alumno> listarAlumnos() {
        return alumnoJpaRepository.findAll().stream()
                .map(this::convertirEntityAAlumno)
                .collect(Collectors.toList());
    }

    public Optional<Alumno> findByDni(String dni) {

        alumnoEntityOptional = alumnoJpaRepository.findByDni(dni);
        return (alumnoEntityOptional.map(this::convertirEntityAAlumno));
    }

    @Override
    public Boolean eliminarAlumnoDadoDni(String dniAlumno) {

        alumnoEntityOptional = alumnoJpaRepository.findByDni(dniAlumno);

        if (alumnoEntityOptional.isPresent()) {

            AlumnoEntity alumnoEntity = alumnoEntityOptional.get();

            if (matriculaService.alumnoTieneAsignaturas(dniAlumno)) {
                System.out.println("No se puede dar de baja al alumno porque tiene asignaturas asociadas.");
                return false;
            } else {
                alumnoJpaRepository.delete(alumnoEntity);
                return true;
            }
        } else {
            System.out.println("No se puede dar de baja un alumno que no existe.");
            return false;

        }
    }

    @Override
    public Alumno encontrarSiExisteAlumno(String dniAlumno) {

        alumnoEntityOptional = alumnoJpaRepository.findByDni(dniAlumno);

        if (alumnoEntityOptional.isPresent()) {

            AlumnoEntity alumnoEntity = alumnoEntityOptional.get();
            return convertirEntityAAlumno(alumnoEntity);

        } else {
            System.out.println("No se encontró ningún alumno con el DNI: " + dniAlumno);

        }
        return null;
    }

    @Override
    public boolean existe(String dniAlumno) {

        return alumnoJpaRepository.findByDni(dniAlumno).isPresent();
    }

    @Override
    public ResponseEntity<String> actualizarAlumno(Alumno alumno) {

        alumnoEntityOptional = alumnoJpaRepository.findByDni(alumno.getDni());

        if (alumnoEntityOptional.isPresent()) {
            AlumnoEntity alumnoEntity = alumnoEntityOptional.get();
            alumnoEntity.setNombre(alumno.getNombre());
            alumnoEntity.setApellido1(alumno.getApellido1());
            alumnoEntity.setApellido2(alumno.getApellido2());
            alumnoJpaRepository.save(alumnoEntity);
            convertirEntityAAlumno(alumnoEntity);
            return new ResponseEntity<>("Modificación realizada con exito.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Parece que ha habido un error con la actualizacion del usuario.", HttpStatus.OK);
    }
    // Metodos de Conversion

    public AlumnoEntity convertirAlumnoAEntity(Alumno alumno) {

        AlumnoEntity alumnoEntity = new AlumnoEntity();

        alumnoEntity.setId(alumno.getId());
        alumnoEntity.setDni(alumno.getDni());
        alumnoEntity.setNombre(alumno.getNombre());
        alumnoEntity.setApellido1(alumno.getApellido1());
        alumnoEntity.setApellido2(alumno.getApellido2());
        return alumnoEntity;
    }

    public Alumno convertirEntityAAlumno(AlumnoEntity alumnoEntity) {
        Alumno alumno = new Alumno();
        alumno.setId(alumnoEntity.getId());
        alumno.setDni(alumnoEntity.getDni());
        alumno.setNombre(alumnoEntity.getNombre());
        alumno.setApellido1(alumnoEntity.getApellido1());
        alumno.setApellido2(alumnoEntity.getApellido2());
        return alumno;
    }
}
