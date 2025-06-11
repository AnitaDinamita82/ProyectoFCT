package myProyectoDAW.gestionInstituciones.adapters.jpas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import myProyectoDAW.gestionInstituciones.adapters.dtos.AlumnoAsignaturasDTO;
import myProyectoDAW.gestionInstituciones.adapters.entitys.AlumnoEntity;

/*
 * Interfaz de repositorio JPA para la entidad AlumnoEntity.
 */
@Repository
public interface AlumnoJpaRepository extends JpaRepository<AlumnoEntity, Integer> {

        /*
         * Spring Data JPA crea automaticamente la implementacion de este metodo, los
         * nombres tienen que ir con un formato en concreto
         */
        Optional<AlumnoEntity> findByDni(String dni);

        /*
         * Consuta Query a la base de datos para poder obtener los alumnos con mas
         * asignaturas
         */
        @Query("SELECT new myProyectoDAW.gestionInstituciones.adapters.dtos.AlumnoAsignaturasDTO(" +
                        " alum.dni, alum.nombre, alum.apellido1, alum.apellido2, COUNT(asig.codigo)) " +
                        "FROM AlumnoEntity alum JOIN alum.asignaturas asig GROUP BY alum.dni, alum.nombre, alum.apellido1, alum.apellido2 "
                        +
                        " ORDER BY COUNT(asig.codigo) DESC")
        List<AlumnoAsignaturasDTO> countAsignaturasByAlumnoOrdered();
}
