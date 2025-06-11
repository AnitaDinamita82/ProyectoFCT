package myProyectoDAW.gestionInstituciones.adapters.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import myProyectoDAW.gestionInstituciones.adapters.dtos.CodigoAsignaturaDTO;
import myProyectoDAW.gestionInstituciones.adapters.dtos.DniAlumnoDTO;
import myProyectoDAW.gestionInstituciones.adapters.dtos.ModuloDTO;
import myProyectoDAW.gestionInstituciones.adapters.entitys.ModuloEntity;
import myProyectoDAW.gestionInstituciones.applications.services.ModuloService;
import myProyectoDAW.gestionInstituciones.domain.models.Modulo;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Clase controladora REST para la gestión de módulos.
 * Esta clase expone los endpoints HTTP para realizar operaciones CRUD y
 * consultas relacionadas con los módulos, incluyendo la asignación de alumnos y
 * asignaturas.
 */
@RestController
@RequestMapping("{version}/modulos")
public class ModuloController {

    @Autowired
    private ModuloService moduloService;

    /* -- ENDPOINTS GET -- */

    // ENDPOINT PARA LISTAR TODOS LOS MODULOS EXISTENTES //
    @GetMapping("/listarModulos")
    public ResponseEntity<List<Modulo>> obtenerTodosLosModulos() {

        List<Modulo> modulos = moduloService.obtenerTodosLosModulos();
        return new ResponseEntity<>(modulos, HttpStatus.OK);
    }

    // ENDPOINT PARA LISTAR TODAS LAS ASIGNATURAS DE UN MODULO DADO SU CODIGO //
    @GetMapping("/listarAsignaturasModulo/{codigoModulo}")
    public ResponseEntity<?> obtenerTodasLasAsignaturasDeUnModulo(@PathVariable String codigoModulo) {
        return moduloService.obtenerTodasLasAsignaturasDeUnModulo(codigoModulo);
    }

    // ENDPOINT PARA LISTAR TODOS LOS ALUMNOS DE UN MODULO DADO SU CODIGO //
    @GetMapping("/listarAlumnosModulo/{codigoModulo}")
    public ResponseEntity<?> obtenerTodosLosAlumnosDeUnModulo(@PathVariable String codigoModulo) {
        return moduloService.obtenerTodosLosAlumnosDeUnModulo(codigoModulo);
    }

    // ENPOINT PARA BUSCAR UN MODULO EN CONCRETO DADO SU CODIGO //
    @GetMapping("/buscarModulo/{codigoModulo}")
    public ResponseEntity<?> buscarModuloDadoElCodigo(@PathVariable String codigoModulo) {
        return moduloService.buscarModuloDadoElCodigo(codigoModulo);
    }

    // ENDPOINT PARA LISTAR TODOS LOS MODULOS EN LOS SE IMPARTIRÁ UNA DETERMINADA
    // ASIGNATURA //
    @GetMapping("/listarModulosDeAsignatura/{codigoAsignatura}")
    public ResponseEntity<?> obtenerTodosLosModulosDeUnaAsignatura(@PathVariable String codigoAsignatura) {
        return moduloService.obtenerTodosLosModulosDeUnaAsignatura(codigoAsignatura);
    }

    // ENDPOINT PARA LISTAR TODOS LOS MODULOS EN LOS QUE EL ALUMNO PUEDE ESTAR
    // MATRICULADO//
    @GetMapping("/listarModulosDeAlumno/{dniAlumno}")
    public ResponseEntity<?> obtenerTodosLosModulosDeUnAlumno(@PathVariable String dniAlumno) {
        return moduloService.obtenerTodosLosModulosDeUnAlumno(dniAlumno);
    }

    /* -- ENDPOINTS POST -- */

    // ENDPOINT PARA EL ALTA DE UN NUEVO MODULO //
    @PostMapping("/altaModulo")
    public ResponseEntity<String> crearNuevoModulo(@RequestBody ModuloDTO moduloDTO) {

        ModuloEntity moduloEntity = moduloDTO.convertirDTOaEntity(); // Se convierte a modulo de datos entyties que es
                                                                     // con lo que vamos a trabajar internamente.

        return moduloService.crearNuevoModulo(moduloEntity);
    }

    // ENDPOINT PARA ASIGNAR ALUMNOS A UN MODULO EN CONCRETO //
    @PostMapping("/asignarAlumnosAModulo/{codigoModulo}")
    public ResponseEntity<String> asignarAlumnosAModulo(@PathVariable String codigoModulo,
            @RequestBody DniAlumnoDTO dniAlumnoDTO) {
        return moduloService.asignarAlumnosAModulo(codigoModulo, dniAlumnoDTO.getDni());
    }

    // ENDPOINT PARA LA ASIGNACION DE ASIGNATURAS A UN MODULO EN CONCRETO //
    @PostMapping("/asignarAsignaturasAModulo/{codigoModulo}")
    public ResponseEntity<String> asignarAsignaturasAModulo(@PathVariable String codigoModulo,
            @RequestBody CodigoAsignaturaDTO codigoAsignaturaDTO) {
        return moduloService.asignarAsignaturasAModulo(codigoModulo, codigoAsignaturaDTO.getCodigoAsignatura());
    }

    /* -- ENDPOINTS PUT -- */

    // ENDPOINT PARA ACTUALIZAR LOS DATOS DE UN MODULO //
    @PutMapping("/actualizarModulo")
    public ResponseEntity<String> actualizarDatosEnModulo(@RequestBody ModuloDTO moduloDTO) {

        ModuloEntity moduloEntity = moduloDTO.convertirDTOaEntity();

        return moduloService.actualizarDatosEnModulo(moduloEntity);
    }

    /* -- ENDPOINTS DELETE -- */

    // ENDPOINT PARA LA ELIMINACION DE UN MODULO DADO SU CODIGO //
    @DeleteMapping("/bajaModulo/{codigoModulo}")
    public ResponseEntity<String> bajaDeUnModuloDadoElCodigo(@PathVariable String codigoModulo) {

        return moduloService.bajaDeUnModuloDadoElCodigo(codigoModulo);
    }

    // ENDPOINT PARA DESASIGNAR ALUMNOS DE UN MODULO //
    @DeleteMapping("/desasignarAlumnosDeModulo/{codigoModulo}/{dniAlumno}")
    public ResponseEntity<String> desasignarAlumnoDeModulo(@PathVariable String codigoModulo,
            @PathVariable String dniAlumno) {
        return moduloService.desasignarAlumnoDeModulo(codigoModulo, dniAlumno);
    }

    // ENDPOINT PARA DESASIGNAR UNA ASIGNATURA DE UN MODULO //
    @DeleteMapping("/desasignarAsignaturasDeModulo/{codigoModulo}/{codigoAsignatura}")
    public ResponseEntity<String> desasignarAsignaturaDeModulo(@PathVariable String codigoModulo,
            @PathVariable String codigoAsignatura) {
        return moduloService.desasignarAsignaturaDeModulo(codigoModulo, codigoAsignatura);
    }

}
