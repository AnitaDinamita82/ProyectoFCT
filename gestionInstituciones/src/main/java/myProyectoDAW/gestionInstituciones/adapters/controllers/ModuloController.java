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

@RestController
@RequestMapping("{version}/modulos")
public class ModuloController {

    @Autowired
    private ModuloService moduloService;

    /* GET */
    @GetMapping("/listar")
    public ResponseEntity<List<Modulo>> obtenerTodosLosModulos() {

        List<Modulo> modulos = moduloService.obtenerTodosLosModulos();
        return new ResponseEntity<>(modulos, HttpStatus.OK);
    }

    @GetMapping("/listarAsignaturas/{codigoModulo}")
    public ResponseEntity<?> obtenerTodasLasAsignaturasDeUnModulo(@PathVariable String codigoModulo) {
        return moduloService.obtenerTodasLasAsignaturasDeUnModulo(codigoModulo);
    }

    @GetMapping("/buscar/{codigoModulo}")
    public ResponseEntity<?> buscarModuloDadoElCodigo(@PathVariable String codigoModulo) {
        return moduloService.buscarModuloDadoElCodigo(codigoModulo);
    }

    /* POST */
    @PostMapping("/alta")
    public ResponseEntity<String> crearNuevoModulo(@RequestBody ModuloDTO moduloDTO) {

        ModuloEntity moduloEntity = moduloDTO.convertirDTOaEntity(); // Se convierte a modulo de datos entyties que es
                                                                     // con lo que vamos a trabajar internamente.

        return moduloService.crearNuevoModulo(moduloEntity);
    }

    // -- asignarAlumnosAModulo --
    @PostMapping("/asignarAlumnos/{codigoModulo}")
    public ResponseEntity<String> asignarAlumnosAModulo(@PathVariable String codigoModulo,
            @RequestBody DniAlumnoDTO dniAlumnoDTO) {
        return moduloService.asignarAlumnosAModulo(codigoModulo, dniAlumnoDTO.getDni());
    }

    // -- asignarAsignaturasAModulo --
    @PostMapping("/asignarAsignaturas/{codigoModulo}")
    public ResponseEntity<String> asignarAsignaturasAModulo(@PathVariable String codigoModulo,
            @RequestBody CodigoAsignaturaDTO codigoAsignaturaDTO) {
        return moduloService.asignarAsignaturasAModulo(codigoModulo, codigoAsignaturaDTO.getCodigoAsignatura());
    }

    /* PUT */
    @PutMapping("/actualizar")
    public ResponseEntity<String> actualizarDatosEnModulo(@RequestBody ModuloDTO moduloDTO) {

        ModuloEntity moduloEntity = moduloDTO.convertirDTOaEntity();

        return moduloService.actualizarDatosEnModulo(moduloEntity);
    }

    /* DELETE */
    @DeleteMapping("/baja/{codigoModulo}")
    public ResponseEntity<String> bajaDeUnModuloDadoElCodigo(@PathVariable String codigoModulo) {

        return moduloService.bajaDeUnModuloDadoElCodigo(codigoModulo);
    }

    // -- DesasignarAlumnosDeModulo --
    @DeleteMapping("/desasignarAlumnos/{codigoModulo}/{dniAlumno}")
    public ResponseEntity<String> desasignarAlumnoDeModulo(@PathVariable String codigoModulo,
            @PathVariable String dniAlumno) {
        return moduloService.desasignarAlumnoDeModulo(codigoModulo, dniAlumno);
    }

    // -- DesasignarAsignaturasDeModulo --
    @DeleteMapping("/desasignarAsignaturas/{codigoModulo}/{codigoAsignatura}")
    public ResponseEntity<String> desasignarAsignaturaDeModulo(@PathVariable String codigoModulo,
            @PathVariable String codigoAsignatura) {
        return moduloService.desasignarAsignaturaDeModulo(codigoModulo, codigoAsignatura);
    }

}
