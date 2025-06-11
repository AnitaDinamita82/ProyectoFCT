package myProyectoDAW.gestionInstituciones.adapters.jpas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import myProyectoDAW.gestionInstituciones.adapters.dtos.AsignaturaAlumnosDTO;
import myProyectoDAW.gestionInstituciones.adapters.entitys.AsignaturaEntity;

/*
 * Interfaz de repositorio JPA para la entidad AsignaturaEntity.
 */
@Repository
public interface AsignaturaJpaRepository extends JpaRepository<AsignaturaEntity, Integer> {

        /*
         * Spring Data JPA crea automaticamente la implementacion de este metodo, los
         * nombres tienen que ir con un formato en concreto
         */
        Optional<AsignaturaEntity> findByCodigo(String codigo);

        /*
         * Consuta Query a la base de datos para poder obtener el total de las
         * asignaturas por alumno
         */
        @Query("SELECT new myProyectoDAW.gestionInstituciones.adapters.dtos.AsignaturaAlumnosDTO(" +
                        " asig.codigo, asig.nombre, COUNT(DISTINCT alum.dni)) "
                        +
                        "FROM AsignaturaEntity asig JOIN asig.alumnos alum GROUP BY asig.codigo, asig.nombre")

        List<AsignaturaAlumnosDTO> countAlumnosByAsignatura();
}
