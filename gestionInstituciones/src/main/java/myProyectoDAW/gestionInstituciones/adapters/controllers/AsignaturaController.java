package myProyectoDAW.gestionInstituciones.adapters.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myProyectoDAW.gestionInstituciones.adapters.dtos.AsignaturaDTO;
import myProyectoDAW.gestionInstituciones.applications.services.AsignaturaService;
import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

@RestController
@RequestMapping("{version}/asignaturas")
public class AsignaturaController {

    @Autowired
    private AsignaturaService asignaturaService;

    /* GET */
    @GetMapping("/listar")
    public ResponseEntity<List<Asignatura>> listarAsignatura() {

        List<Asignatura> asignaturas = asignaturaService.listarAsignaturas();

        return new ResponseEntity<>(asignaturas, HttpStatus.OK);
    }

    /* GET */
    @GetMapping("/buscar/{codigoAsignatura}")

    public ResponseEntity<Asignatura> encontrarSiExisteAsignatura(@PathVariable String codigoAsignatura) {

        Asignatura asignatura = asignaturaService.encontrarSiExisteAsignatura(codigoAsignatura);

        if (asignatura != null) {
            return ResponseEntity.ok(asignatura);
        } else {
            return ResponseEntity.notFound().build(); // Devuelve código 404 Not Found si no se encuentra
        }
    }

    /* POST */
    @PostMapping("/alta")
    public ResponseEntity<?> altaAsignatura(@RequestBody AsignaturaDTO asignaturasDTO) {
        System.out.println("pero estoy dentro o no?");
        Asignatura asignatura = asignaturasDTO.convertirDTOAEntity();
        System.out.println("que codigo me llega " + asignatura.getCodigo());

        if (asignaturaService.existe(asignatura.getCodigo())) {
            return new ResponseEntity<>("La asignatura " + asignatura.getNombre() + " ya existe en el sistema.",
                    HttpStatus.CONFLICT);

        } else {
            asignaturaService.darDeAltaUnaAsignatura(asignatura);
            return new ResponseEntity<>(asignatura, HttpStatus.CREATED);
        }

    }

    /* DELETE */
    @DeleteMapping("/baja/{codigoAsignatura}")
    public ResponseEntity<String> bajaAsignatura(@PathVariable String codigoAsignatura) {

        Asignatura asignatura = asignaturaService.encontrarSiExisteAsignatura(codigoAsignatura);

        if (asignatura == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se puede dar de baja una asignatura que no existe.");
        }

        Boolean bajaExitosa = asignaturaService.darDeBajaUnaAsignatura(codigoAsignatura);

        if (bajaExitosa) {
            return ResponseEntity.ok("Asignatura dada de baja correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se puede dar de baja la asignatura porque tiene alumnos matriculados.");
        }

    }

    /* PUT */
    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarAsignatura(@RequestBody AsignaturaDTO asignaturaDTO) {

        Asignatura asignatura = asignaturaDTO.convertirDTOAEntity();
        return asignaturaService.actualizarAsignatura(asignatura);

    }
}
