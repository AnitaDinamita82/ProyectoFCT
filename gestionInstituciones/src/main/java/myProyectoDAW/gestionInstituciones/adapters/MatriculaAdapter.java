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
import myProyectoDAW.gestionInstituciones.adapters.entitys.ModuloEntity;
import myProyectoDAW.gestionInstituciones.adapters.jpas.AlumnoJpaRepository;
import myProyectoDAW.gestionInstituciones.adapters.jpas.AsignaturaJpaRepository;
import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryMatricula;
import myProyectoDAW.gestionInstituciones.applications.services.ModuloService;
import myProyectoDAW.gestionInstituciones.domain.models.Alumno;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

/* Adaptador para la gestión de matrículas (relación Alumno-Asignatura) */

@Component
public class MatriculaAdapter implements RepositoryMatricula {

    @Autowired
    private AlumnoJpaRepository alumnoJpaRepository;

    @Autowired
    private AsignaturaJpaRepository asignaturaJpaRepository;

    @Autowired
    private ModuloService moduloService;

    /*
     * Implementación del metodo que lista todas las asignaturas en las que un
     * alumno está matriculado
     */
    @Override
    public List<Asignatura> listarAsignaturasDeAlumno(String dniAlumno) {

        Optional<AlumnoEntity> alumnoEntityOptional = alumnoJpaRepository.findByDni(dniAlumno);

        AlumnoEntity alumnoEntity = alumnoEntityOptional.get();
        return alumnoEntity.getAsignatura().stream()
                .map(this::convertirEntityAAsignatura)
                .collect(Collectors.toList());
    }

    /*
     * Implementación del metodo que lista todos los alumnos matriculados de una
     * asignatura especifica
     */
    @Override
    public List<Alumno> obtenerAlumnosMatriculados(String codigoAsignatura) {

        Optional<AsignaturaEntity> asignaturaEntityOptional = asignaturaJpaRepository.findByCodigo(codigoAsignatura);

        AsignaturaEntity asignaturaEntity = asignaturaEntityOptional.get();

        return asignaturaEntity.getAlumnos().stream()
                .map(this::convertirEntityAAlumno)
                .collect(Collectors.toList());

    }

    /*
     * Implementacion de metodo que verifica si aun alumno tiene asignaturas
     * matriculadas
     */
    @Override
    public boolean alumnoTieneAsignaturas(String dniAlumno) {

        List<Asignatura> asignaturas = listarAsignaturasDeAlumno(dniAlumno);
        return !asignaturas.isEmpty(); // Devuelve true si la lista no está vacía
    }

    /*
     * Implementacion de método que verifica su una asignatura tiene alumnos
     * matriculados
     */
    @Override
    public boolean asignaturasTieneAlumnos(String codigoAsignatura) {

        List<Alumno> alumnos = obtenerAlumnosMatriculados(codigoAsignatura);
        return !alumnos.isEmpty(); // Devolverá true si la lista no está vacia.
    }

    /*
     * Implementación del método que matricula a un alumno
     * Esta operación es transaccional para asegurar que los cambios se guarden de
     * forma atómica.
     */
    @Override
    @Transactional
    public ResponseEntity<String> matricularAlumno(String dniAlumno, String codigoAsignatura) {

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

    /*
     * Implementacion del metodo para dar de baja una asignatura de la matricula de
     * un alumno
     */
    @Override
    @Transactional
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumno(String dniAlumno, String codigoAsignatura) {

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

    /*
     * Desmatricula a un alumno de todas las asignaturas en las que esté actualmente
     * matriculado.
     */
    @Override
    public ResponseEntity<String> desmatricularTodasLasAsignaturasDeUnAlumno(String dniAlumno) {

        /* Obtenemos al alumno en cuestion */
        AlumnoEntity alumnoEntity = alumnoJpaRepository.findByDni(dniAlumno).get();

        if (!alumnoEntity.getAsignatura().isEmpty()) {
            alumnoEntity.getAsignatura().clear();
            alumnoJpaRepository.save(alumnoEntity);
        }
        return new ResponseEntity<>("Se le ha dado al alumno" + alumnoEntity.getNombre() + alumnoEntity.getApellido1()
                + " de baja en todas sus asignaturas, o no estaba matricuado.", HttpStatus.OK);
    }

    /*
     * Implementación del método que dará de baja a un alumno de una asignatura
     * específica, con lógica adicional para gestionar la desvinculación del alumno
     * de un módulo si ya no
     * tiene más asignaturas en él
     */
    @Override
    @Transactional
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumnoConGestionDeModulo(String dniAlumno,
            String codigoAsignatura, String codigoModulo) {

        // 1. Obtenemos el alumno

        Optional<AlumnoEntity> optionalAlumnoEntity = alumnoJpaRepository.findByDni(dniAlumno);

        if (!optionalAlumnoEntity.isPresent()) {
            return new ResponseEntity<>("Alumno con DNI " + dniAlumno + " no encontrado.", HttpStatus.NOT_FOUND);
        }

        AlumnoEntity alumnoEntity = optionalAlumnoEntity.get(); // Pasamos a entidad.

        // 2. Obtenemos la asignatura a desmatricular
        Optional<AsignaturaEntity> optionalAsignaturaEntity = asignaturaJpaRepository.findByCodigo(codigoAsignatura);

        if (!optionalAsignaturaEntity.isPresent()) {
            return new ResponseEntity<>(
                    "Asignatura con código " + optionalAsignaturaEntity.get().getNombre() + " no encontrada.",
                    HttpStatus.NOT_FOUND);
        }
        AsignaturaEntity asignaturaEntity = optionalAsignaturaEntity.get(); // Pasamos a entidad

        // 3. Se elimina la asignatura de la matrícula del alumno (relación
        // alumno-asignatura)

        if (!alumnoEntity.getAsignatura().remove(asignaturaEntity)) {
            return new ResponseEntity<>("El alumno con DNI " + dniAlumno + " no estaba matriculado en la asignatura "
                    + codigoAsignatura + ".", HttpStatus.NOT_FOUND);
        }

        alumnoJpaRepository.save(alumnoEntity); // Guardar los cambios de la matrícula

        // 4. Verificamos si el alumno tiene más asignaturas el el mismo modulo
        // (codigoModulo)

        alumnoEntity = alumnoJpaRepository.findByDni(dniAlumno).get(); // Nos seguramos que los datos estén actualizados
        List<AsignaturaEntity> asignaturasRestantesDelAlumno = alumnoEntity.getAsignatura();

        boolean alumnoTieneMasAsignaturasEnEsteModulo = asignaturasRestantesDelAlumno.stream()
                .anyMatch(a -> {
                    return a.getModulos() != null && a.getModulos().stream()
                            .anyMatch(m -> m.getCodigoModulo().equals(codigoModulo));
                });

        System.out.println("DEBUG: ¿Alumno " + dniAlumno + " tiene más asignaturas en el módulo " + codigoModulo + "? "
                + alumnoTieneMasAsignaturasEnEsteModulo); // PUNTO DE CONTROL

        // 5. Si el alumno no tiene más asignaturas en el módulo lo desvinculamos
        // tambien del módulo.
        if (!alumnoTieneMasAsignaturasEnEsteModulo) {

            // Eliminar la relación alumno-módulo
            moduloService.desasignarAlumnoDeModulo(codigoModulo, dniAlumno);

            return new ResponseEntity<>(
                    "Se ha dado de baja la asignatura " + asignaturaEntity.getNombre()
                            + " y el alumno ha sido desvinculado del módulo " + codigoModulo + ".",
                    HttpStatus.OK);
        } else {
            // Si el alumno aún tiene asignaturas en este módulo, no se toca la relacion
            // módulo-alumno
            return new ResponseEntity<>(
                    "Se ha dado de baja la asignatura " + asignaturaEntity.getNombre(), HttpStatus.OK);
        }
    }

    /*
     * Implementación del método que obtiene el número de alumnos matriculados en
     * una asignatura específica que pertenecen a un módulo dado.
     */
    @SuppressWarnings("null") // Suprime advertencias de `NullPointerException` en ciertas operaciones de
                              // .get().
    @Override
    @Transactional
    public ResponseEntity<?> alumnosMatriculadosEnAsignaturaPorModulos(String codigoAsignatura,
            String codigoModulo) {

        // 1. Obtenemos la asignatura
        Optional<AsignaturaEntity> optionalAsignaturaEntity = asignaturaJpaRepository.findByCodigo(codigoAsignatura);

        System.out.println("Nombre Asignatura " + optionalAsignaturaEntity.get().getNombre()); // PUNTO DE CONTROL

        if (!optionalAsignaturaEntity.isPresent()) {
            return new ResponseEntity<>(
                    "La asignatura " + optionalAsignaturaEntity.get().getNombre() + " no está asignada a este modulo",
                    HttpStatus.NOT_FOUND);

        }

        List<AlumnoEntity> alumnosMatriculadosEnAsignatura = optionalAsignaturaEntity.get().getAlumnos();

        // Lista para guardar los alumnos que cumplen ambas condiciones
        List<AlumnoEntity> alumnosQueCumplenCondicion = new ArrayList<>();

        if (alumnosMatriculadosEnAsignatura.isEmpty()) { // La asignatura no tiene alumnos
            return new ResponseEntity<>(
                    alumnosMatriculadosEnAsignatura,
                    HttpStatus.OK);
        }
        // Tengo alumnos pero hay que comporbar si estan asignados al modulo que se ha
        // pasado por parametro

        for (AlumnoEntity alumno : alumnosMatriculadosEnAsignatura) {

            System.out.println("DEBUG: Procesando alumno: " + alumno.getDni()); // PUNTO DE CONTROL

            // Obtenemos todos los modulos a los que pertenezca el alumno
            ResponseEntity<?> modulosResponse = moduloService.obtenerTodosLosModulosDeUnAlumno(alumno.getDni());

            // Verifica si la respuesta del servicio de módulos fue exitosa y tiene cuerpo.
            if (modulosResponse.getStatusCode().is2xxSuccessful() && modulosResponse.getBody() != null) {

                // Pregunto si el cuerpo es una Lista
                if (modulosResponse.getBody() instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<ModuloEntity> modulosDelAlumno = (List<ModuloEntity>) modulosResponse.getBody();

                    // Compruebo si el módulo está en la lista de módulos del alumno
                    boolean estaEnElModulo = modulosDelAlumno.stream()
                            .anyMatch(modulo -> modulo.getCodigoModulo().equals(codigoModulo));

                    // Y si está en el módulo, lo añado a la lista de resultados.
                    if (estaEnElModulo) {
                        alumnosQueCumplenCondicion.add(alumno);

                    }
                }
            }
        }
        return new ResponseEntity<>(alumnosQueCumplenCondicion, HttpStatus.OK);
    }

    // METODOS DE CONVERSION //

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
