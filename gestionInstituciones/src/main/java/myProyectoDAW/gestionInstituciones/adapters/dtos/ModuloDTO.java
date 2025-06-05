package myProyectoDAW.gestionInstituciones.adapters.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import myProyectoDAW.gestionInstituciones.adapters.entitys.AlumnoEntity;
import myProyectoDAW.gestionInstituciones.adapters.entitys.AsignaturaEntity;
import myProyectoDAW.gestionInstituciones.adapters.entitys.ModuloEntity;

public class ModuloDTO {

    private Integer id;
    private String codigoModulo;
    private String nombreModulo;
    private String curso;
    private String grupo;
    private List<String> dniAlumnos; // Ref. de los alumnos en este módulo
    private List<String> codigoAsignaturas; // Ref. de las asignaturas en este módulo

    public ModuloDTO() {

        this.dniAlumnos = new ArrayList<>();
        this.codigoAsignaturas = new ArrayList<>();
    }

    public ModuloDTO(Integer id, String codigoModulo, String nombreModulo, String curso, String grupo,
            List<String> dniAlumnos, List<String> codigoAsignaturas) {
        this.id = id;
        this.codigoModulo = codigoModulo;
        this.nombreModulo = nombreModulo;
        this.curso = curso;
        this.grupo = grupo;
        this.dniAlumnos = dniAlumnos != null ? dniAlumnos : new ArrayList<>();
        this.codigoAsignaturas = codigoAsignaturas != null ? codigoAsignaturas : new ArrayList<>();
    }

    // Getter and Setter

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigoModulo() {
        return codigoModulo;
    }

    public void setCodigoModulo(String codigoModulo) {
        this.codigoModulo = codigoModulo;
    }

    public String getNombreModulo() {
        return nombreModulo;
    }

    public void setNombreModulo(String nombreModulo) {
        this.nombreModulo = nombreModulo;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public List<String> getDniAlumnos() {
        return dniAlumnos;
    }

    public void setDniAlumnos(List<String> dniAlumnos) {
        this.dniAlumnos = dniAlumnos;
    }

    public List<String> getCodigoAsignaturas() {
        return codigoAsignaturas;
    }

    public void setCodigoAsignaturas(List<String> codigoAsignaturas) {
        this.codigoAsignaturas = codigoAsignaturas;
    }

    // Otros metodos
    @Override
    public String toString() {
        return "ModuloDTO [id=" + id + ", codigoModulo=" + codigoModulo + ", nombreModulo=" + nombreModulo + ", curso="
                + curso + ", grupo=" + grupo + ", dniAlumnos=" + dniAlumnos + ", codigoAsignaturas=" + codigoAsignaturas
                + "]";
    }

    /* METODOS DE CONVERSION */

    // DTO a Entity.

    public ModuloEntity convertirDTOaEntity() {

        ModuloEntity moduloEntity = new ModuloEntity();

        moduloEntity.setCodigoModulo(this.getCodigoModulo());
        moduloEntity.setNombreModulo(this.getNombreModulo());
        moduloEntity.setCurso(this.getCurso());
        moduloEntity.setGrupo(this.getGrupo());
        return moduloEntity;

    }

    // De Entity a DTO

    public static ModuloDTO convertirEntityADTO(ModuloEntity moduloEntity) {

        ModuloDTO moduloDTO = new ModuloDTO();

        moduloDTO.setId(moduloEntity.getId());
        moduloDTO.setCodigoModulo(moduloEntity.getCodigoModulo());
        moduloDTO.setNombreModulo(moduloEntity.getNombreModulo());
        moduloDTO.setCurso(moduloEntity.getCurso());
        moduloDTO.setGrupo(moduloEntity.getGrupo());

        // Mapear la Ref. de alumnos si la lista no es nula
        if (moduloDTO.getDniAlumnos() != null) {
            moduloDTO.setDniAlumnos(moduloEntity.getAlumnos().stream()
                    .map(AlumnoEntity::getDni)
                    .collect(Collectors.toList()));
        } else {
            moduloDTO.setDniAlumnos(new ArrayList<>()); // Asegurarse de que no sea null
        }

        // Mapear la Ref. de asignaturas si la lista no es nula
        if (moduloDTO.getCodigoAsignaturas() != null) {
            moduloDTO.setCodigoAsignaturas(moduloEntity.getAsignaturas().stream()
                    .map(AsignaturaEntity::getCodigo)
                    .collect(Collectors.toList()));
        } else {
            moduloDTO.setCodigoAsignaturas(new ArrayList<>()); // Asegurarse de que no sea null
        }
        return moduloDTO;
    }
}
