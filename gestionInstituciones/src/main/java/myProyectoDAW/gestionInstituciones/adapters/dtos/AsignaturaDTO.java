package myProyectoDAW.gestionInstituciones.adapters.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import myProyectoDAW.gestionInstituciones.domain.models.Asignatura;

public class AsignaturaDTO implements java.io.Serializable {

    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private List<AlumnoDTO> alumnosMatriculados;
    private List<ModuloDTO> modulos; // NUEVO

    public AsignaturaDTO() {
        this.modulos = new ArrayList<>();
    }

    public AsignaturaDTO(Integer id, String codigo, String descripcion, String nombre) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.id = id;
        this.nombre = nombre;
    }

    public AsignaturaDTO(Integer id, String codigo, String nombre, String descripcion,
            List<AlumnoDTO> alumnosMatriculados) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.alumnosMatriculados = alumnosMatriculados;
    }

    // Definicion e implementacion de getter and Setter

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<AlumnoDTO> getAlumnosMatriculados() {
        return alumnosMatriculados;
    }

    public void setAlumnosMatriculados(List<AlumnoDTO> alumnosMatriculados) {
        this.alumnosMatriculados = alumnosMatriculados;
    }

    /* NUEVOS Getter and Setter */

    public List<ModuloDTO> getModulos() {
        return modulos;
    }

    public void setModulos(List<ModuloDTO> modulos) {
        this.modulos = modulos;
    }

    // Otros metodos
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AsignaturasDTO{");
        sb.append("id=").append(id);
        sb.append(", codigo=").append(codigo);
        sb.append(", nombre=").append(nombre);
        sb.append(", descripcion=").append(descripcion);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AsignaturaDTO)) {
            return false;
        }
        AsignaturaDTO other = (AsignaturaDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    /********** METODOS DE CONVERSION **********/

    // DTO to Entity

    public Asignatura convertirDTOAEntity() {

        Asignatura asignatura = new Asignatura();
        asignatura.setId(this.getId());
        asignatura.setCodigo(this.getCodigo());
        asignatura.setNombre(this.getNombre());
        asignatura.setDescripcion(this.getDescripcion());
        return asignatura;
    }

    // DTO to Entity

    public static AsignaturaDTO convertirEntityADTO(Asignatura asignatura) {
        return new AsignaturaDTO(
                asignatura.getId(),
                asignatura.getCodigo(),
                asignatura.getNombre(),
                asignatura.getDescripcion(),
                asignatura.getAlumnos().stream()
                        .map(AlumnoDTO::convertirEntityADTO)
                        .collect(Collectors.toList()));
    }
}
