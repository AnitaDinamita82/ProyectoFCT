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

/**
 * Adaptador para la entidad Alumno. Clase que actua de pasarela entre la capa
 * de dominio y la de persistencia.
 * A través de la interfaz 'RepositoryAlumno', podemos hacer uso de las
 * operaciones de acceso a datos de los alumnos.
 * Y con la inyeccion de AlumnoJpaRepository podemos utilizar los metodos CRUD
 * ofrecidos por el repositorio JPA.
 */
@Component
public class AlumnoAdapter implements RepositoryAlumno {

    @Autowired
    private AlumnoJpaRepository alumnoJpaRepository;

    @Autowired
    private MatriculaService matriculaService;

    private Optional<AlumnoEntity> alumnoEntityOptional;

    /* Implementación del metodo para dar de alta un nuevo alumno */
    @Override
    public Alumno altaAlumno(Alumno alumno) {

        AlumnoEntity alumnoEntity = convertirAlumnoAEntity(alumno);
        return convertirEntityAAlumno(alumnoJpaRepository.save(alumnoEntity));

    }

    /* Implementación del metodo para listar todos los alumnos */
    @Override
    public List<Alumno> listarAlumnos() {
        return alumnoJpaRepository.findAll().stream()
                .map(this::convertirEntityAAlumno)
                .collect(Collectors.toList());
    }

    /* Método que busca a un alumno por DNI */
    public Optional<Alumno> findByDni(String dni) {
        alumnoEntityOptional = alumnoJpaRepository.findByDni(dni);
        return (alumnoEntityOptional.map(this::convertirEntityAAlumno));
    }

    /* Implementacion del metodo para eliminar a un alumno dado su DNI */
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

    /*
     * Implementacion del metodo para encontrar y devolver un alumno si exitiese por
     * su DNI
     */
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

    /*
     * Implementacion del método para verificar la existencia de un alumno dado su
     * DNI
     */
    @Override
    public boolean existe(String dniAlumno) {
        return alumnoJpaRepository.findByDni(dniAlumno).isPresent();
    }

    /* Implementación de un método para actualizar los datos de un alumno */
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

    // METODOS DE CONVERSION //
    /*
     * Se definen estos metodos de coversion dado que JPA Repository trabaja con el
     * modelo de datos entity para asegurar la persistencia de los datos
     */

    // -- De Alumno a AlumnoEntity -- //
    public AlumnoEntity convertirAlumnoAEntity(Alumno alumno) {

        AlumnoEntity alumnoEntity = new AlumnoEntity();

        alumnoEntity.setId(alumno.getId());
        alumnoEntity.setDni(alumno.getDni());
        alumnoEntity.setNombre(alumno.getNombre());
        alumnoEntity.setApellido1(alumno.getApellido1());
        alumnoEntity.setApellido2(alumno.getApellido2());
        return alumnoEntity;
    }

    // -- De AlumnoEntity a Alumno --/
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
