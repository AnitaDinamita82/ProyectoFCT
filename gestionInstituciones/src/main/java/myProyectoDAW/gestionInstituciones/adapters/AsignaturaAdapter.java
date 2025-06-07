package myProyectoDAW.gestionInstituciones.adapters;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import myProyectoDAW.gestionInstituciones.adapters.entitys.AsignaturaEntity;
import myProyectoDAW.gestionInstituciones.adapters.jpas.AsignaturaJpaRepository;
import myProyectoDAW.gestionInstituciones.applications.ports.RepositoryAsignatura;
import myProyectoDAW.gestionInstituciones.applications.services.MatriculaService;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

@Component
public class AsignaturaAdapter implements RepositoryAsignatura {

    @Autowired
    private AsignaturaJpaRepository asignaturaJpaRepository;

    private Optional<AsignaturaEntity> asignaturaEntityOptional;

    @Autowired
    private MatriculaService matriculaService;

    @Override
    public Asignatura altaAsignatura(Asignatura asignatura) {

        AsignaturaEntity asignaturaEntity = convertirAsignaturaAEntity(asignatura);
        return convertirEntityAAsignatura(asignaturaJpaRepository.save(asignaturaEntity));
    }

    @Override
    public List<Asignatura> listarAsignaturas() {
        return asignaturaJpaRepository.findAll().stream()
                .map(this::convertirEntityAAsignatura)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean eliminarAsignaturaDadoCodigo(String codigoAsignatura) {

        asignaturaEntityOptional = asignaturaJpaRepository.findByCodigo(codigoAsignatura);

        if (asignaturaEntityOptional.isPresent()) {

            AsignaturaEntity asignaturaEntity = asignaturaEntityOptional.get();

            if (matriculaService.asignaturaTieneAlumnos(codigoAsignatura)) {

                System.out.println("No se puede dar de baja la asignatura porque tiene alumnos asociados.");
                return false;
            } else {
                asignaturaJpaRepository.delete(asignaturaEntity);
                return true;
            }

        } else {
            System.out.println("No se puede dar de baja una asignatura que no existe.");
            return false;
        }

    }

    @Override
    public ResponseEntity<String> actualizarAsignatura(Asignatura asignatura) {

        asignaturaEntityOptional = asignaturaJpaRepository.findByCodigo(asignatura.getCodigo());

        if (asignaturaEntityOptional.isPresent()) {

            AsignaturaEntity asignaturaEntity = asignaturaEntityOptional.get();
            asignaturaEntity.setCodigo(asignatura.getCodigo());
            asignaturaEntity.setNombre(asignatura.getNombre());
            asignaturaEntity.setDescripcion(asignatura.getDescripcion());

            convertirEntityAAsignatura(asignaturaJpaRepository.save(asignaturaEntity));
            return new ResponseEntity<>("Modificación realizada con exito.", HttpStatus.OK);

        }
        return new ResponseEntity<>("Parece que ha habido un error con la actualizacion de la asignatura.",
                HttpStatus.OK);
    }

    @Override
    public Asignatura encontrarSiExisteAsignatura(String codigo) {

        asignaturaEntityOptional = asignaturaJpaRepository.findByCodigo(codigo);

        if (asignaturaEntityOptional.isPresent()) {

            AsignaturaEntity asisgnaturaEntity = asignaturaEntityOptional.get();
            return convertirEntityAAsignatura(asisgnaturaEntity);

        } else {
            System.out.println("No se encontró ninguna asignatura con el código: " + codigo);

        }
        return null;
    }

    @Override
    public boolean existe(String codigoAsignatura) {
        System.out.println("Estoy dentro de existe ");
        System.out.println("que codigo tengo " + codigoAsignatura);
        System.out.println(asignaturaJpaRepository.findByCodigo(codigoAsignatura).isPresent());
        return asignaturaJpaRepository.findByCodigo(codigoAsignatura).isPresent();
    }
    /* Metodos de conversion */

    private AsignaturaEntity convertirAsignaturaAEntity(Asignatura asignatura) {

        AsignaturaEntity asignaturaEntity = new AsignaturaEntity();

        asignaturaEntity.setId(asignatura.getId());
        asignaturaEntity.setCodigo(asignatura.getCodigo());
        asignaturaEntity.setNombre(asignatura.getNombre());
        asignaturaEntity.setDescripcion(asignatura.getDescripcion());
        return asignaturaEntity;
    }

    private Asignatura convertirEntityAAsignatura(AsignaturaEntity asignaturaEntity) {

        Asignatura asignatura = new Asignatura();

        asignatura.setId(asignaturaEntity.getId());
        asignatura.setCodigo(asignaturaEntity.getCodigo());
        asignatura.setNombre(asignaturaEntity.getNombre());
        asignatura.setDescripcion(asignaturaEntity.getDescripcion());
        return asignatura;
    }
}
