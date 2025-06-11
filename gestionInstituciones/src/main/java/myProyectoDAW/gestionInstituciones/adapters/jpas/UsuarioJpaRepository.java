package myProyectoDAW.gestionInstituciones.adapters.jpas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myProyectoDAW.gestionInstituciones.adapters.entitys.UsuarioEntity;

/*
 * Interfaz de repositorio JPA para la entidad UsuarioEntity.
 */
@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Integer> {

   /*
    * Spring Data JPA crea automaticamente la implementacion de estos metodos, los
    * nombres tienen que ir con un formato en concreto
    */
   Optional<UsuarioEntity> findByLogin(String login);

   Optional<UsuarioEntity> findByDni(String dniUsuario);
}
