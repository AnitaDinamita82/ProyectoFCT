package myProyectoDAW.gestionInstituciones.adapters.jpas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myProyectoDAW.gestionInstituciones.adapters.entitys.ModuloEntity;

/*
 * Interfaz de repositorio JPA para la entidad ModuloEntity.
 */
@Repository
public interface ModuloJpaRepository extends JpaRepository<ModuloEntity, Integer> {

    /*
     * Spring Data JPA crea automaticamente la implementacion de este metodo, los
     * nombres tienen que ir con un formato en concreto
     */
    Optional<ModuloEntity> findByCodigoModulo(String codigoModulo);

}
