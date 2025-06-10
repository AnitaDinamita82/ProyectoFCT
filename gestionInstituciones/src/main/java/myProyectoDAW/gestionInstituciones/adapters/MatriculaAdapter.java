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

@Component
public class MatriculaAdapter implements RepositoryMatricula {

    @Autowired
    private AlumnoJpaRepository alumnoJpaRepository;

    @Autowired
    private AsignaturaJpaRepository asignaturaJpaRepository;

    @Autowired
    private ModuloService moduloService;

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

    @Override
    @Transactional
    public ResponseEntity<String> bajaAsignaturaDeMatriculaDeAlumnoConGestionDeModulo(String dniAlumno,
            String codigoAsignatura, String codigoModulo) {

        // Asumiendo que ahora recibes codigoModulo directamente

        System.out.println("Estoy dentro bajaAsignaturaMatriculaDeAlumnoGestionDeModulo en el adapter"); // PUNTO DE
                                                                                                         // CONTROL
        System.out.println("DEBUG: DNI Alumno: " + dniAlumno + ", Asignatura: " + codigoAsignatura
                + ", Módulo contextual: " + codigoModulo); // PUNTO DE CONTROL

        // 1. Obtener el alumno
        Optional<AlumnoEntity> optionalAlumnoEntity = alumnoJpaRepository.findByDni(dniAlumno);

        if (!optionalAlumnoEntity.isPresent()) {
            return new ResponseEntity<>("Alumno con DNI " + dniAlumno + " no encontrado.", HttpStatus.NOT_FOUND);
        }

        AlumnoEntity alumnoEntity = optionalAlumnoEntity.get(); // Pasamos a entidad.

        // 2. Obtener la asignatura a desmatricular
        Optional<AsignaturaEntity> optionalAsignaturaEntity = asignaturaJpaRepository.findByCodigo(codigoAsignatura);

        if (!optionalAsignaturaEntity.isPresent()) {
            return new ResponseEntity<>(
                    "Asignatura con código " + optionalAsignaturaEntity.get().getNombre() + " no encontrada.",
                    HttpStatus.NOT_FOUND);
        }
        AsignaturaEntity asignaturaEntity = optionalAsignaturaEntity.get(); // Pasamos aentidad

        // 3. Eliminar la asignatura de la matrícula del alumno (relación
        // alumno-asignatura)

        if (!alumnoEntity.getAsignatura().remove(asignaturaEntity)) {
            return new ResponseEntity<>("El alumno con DNI " + dniAlumno + " no estaba matriculado en la asignatura "
                    + codigoAsignatura + ".", HttpStatus.NOT_FOUND);
        }

        alumnoJpaRepository.save(alumnoEntity); // Guardar los cambios de la matrícula

        System.out.println("DEBUG: Asignatura " + codigoAsignatura + " desmatriculada del alumno " + dniAlumno); // PUNTO
                                                                                                                 // DE
                                                                                                                 // CONTROL

        // 4. Verificar si el alumno tiene más asignaturas el el mismo modulo
        // (codigoModulo)
        // Antes se recarga el alumno para asegurar que la lista de asignaturas está
        // actualizada.
        alumnoEntity = alumnoJpaRepository.findByDni(dniAlumno).get();
        List<AsignaturaEntity> asignaturasRestantesDelAlumno = alumnoEntity.getAsignatura();

        boolean alumnoTieneMasAsignaturasEnEsteModulo = asignaturasRestantesDelAlumno.stream()
                .anyMatch(a -> {
                    return a.getModulos() != null && a.getModulos().stream()
                            .anyMatch(m -> m.getCodigoModulo().equals(codigoModulo));
                });

        System.out.println("DEBUG: ¿Alumno " + dniAlumno + " tiene más asignaturas en el módulo " + codigoModulo + "? "
                + alumnoTieneMasAsignaturasEnEsteModulo); // PUNTO DE CONTROL

        // 5. Si el alumno no tiene más asignaturas en el módulo contextual,
        // desvincularlo del módulo.
        if (!alumnoTieneMasAsignaturasEnEsteModulo) {
            // Eliminar la relación alumno-módulo
            moduloService.desasignarAlumnoDeModulo(codigoModulo, dniAlumno);
            // moduloAlumnoJpaRepository.deleteByDniAlumnoAndCodigoModulo(dniAlumno,
            // codigoModulo);
            System.out.println("DEBUG: Alumno " + dniAlumno + " desvinculado del módulo " + codigoModulo
                    + " (no quedan más asignaturas en él).");
            return new ResponseEntity<>(
                    "Se ha dado de baja la asignatura " + asignaturaEntity.getNombre()
                            + " y el alumno ha sido desvinculado del módulo " + codigoModulo + ".",
                    HttpStatus.OK);
        } else {
            // Si el alumno aún tiene asignaturas en este módulo, no tocamos la relación
            // módulo-alumno
            return new ResponseEntity<>(
                    "Se ha dado de baja la asignatura " + asignaturaEntity.getNombre()
                            + ". El alumno sigue matriculado en otras asignaturas del módulo " + codigoModulo + ".",
                    HttpStatus.OK);
        }
    }

    @SuppressWarnings("null")
    @Override
    @Transactional
    public ResponseEntity<?> alumnosMatriculadosEnAsignaturaPorModulos(String codigoAsignatura,
            String codigoModulo) {

        System.out.println("OYEEEEEEE que estoy dentro......");
        System.out.println("Modulo: " + codigoModulo);
        System.out.println("Aasignatura " + codigoAsignatura);

        // 1. Obtener la asignatura
        Optional<AsignaturaEntity> optionalAsignaturaEntity = asignaturaJpaRepository.findByCodigo(codigoAsignatura);

        System.out.println("Nombre Aasignatura " + optionalAsignaturaEntity.get().getNombre());

        if (!optionalAsignaturaEntity.isPresent()) {
            System.out.println("me meti en el primero");
            return new ResponseEntity<>(
                    "La asignatura " + optionalAsignaturaEntity.get().getNombre() + " no está asignada a este modulo",
                    HttpStatus.NOT_FOUND);

        }

        System.out.println("OYEEEEE que sigo dentro");

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

        System.out.println("OYEEEEE que voy a pasar al bucle for....");

        for (AlumnoEntity alumno : alumnosMatriculadosEnAsignatura) {
            System.out.println("DEBUG: Procesando alumno: " + alumno.getDni()); // PUNTO DE CONTROL

            ResponseEntity<?> modulosResponse = moduloService.obtenerTodosLosModulosDeUnAlumno(alumno.getDni());

            if (modulosResponse.getStatusCode().is2xxSuccessful() && modulosResponse.getBody() != null) {

                if (modulosResponse.getBody() instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<ModuloEntity> modulosDelAlumno = (List<ModuloEntity>) modulosResponse.getBody();

                    // Comprobar si el módulo está en la lista de módulos del alumno
                    boolean estaEnElModulo = modulosDelAlumno.stream()
                            .anyMatch(modulo -> modulo.getCodigoModulo().equals(codigoModulo));

                    if (estaEnElModulo) {
                        alumnosQueCumplenCondicion.add(alumno);

                    }
                }
            }
        }
        return new ResponseEntity<>(alumnosQueCumplenCondicion, HttpStatus.OK);
    }

}
