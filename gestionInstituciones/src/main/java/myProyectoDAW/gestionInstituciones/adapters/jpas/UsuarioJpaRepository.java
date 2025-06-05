package myProyectoDAW.gestionInstituciones.adapters.jpas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myProyectoDAW.gestionInstituciones.adapters.entitys.UsuarioEntity;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Integer> {

   Optional<UsuarioEntity> findByLogin(String login);

   Optional<UsuarioEntity> findByDni(String dniUsuario);
}
