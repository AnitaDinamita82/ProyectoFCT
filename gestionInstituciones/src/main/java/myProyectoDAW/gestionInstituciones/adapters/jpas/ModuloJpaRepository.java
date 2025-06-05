package myProyectoDAW.gestionInstituciones.adapters.jpas;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import myProyectoDAW.gestionInstituciones.adapters.entitys.ModuloEntity;

@Repository
public interface ModuloJpaRepository extends JpaRepository<ModuloEntity, Integer> {

    Optional<ModuloEntity> findByCodigoModulo(String codigoModulo);

}
