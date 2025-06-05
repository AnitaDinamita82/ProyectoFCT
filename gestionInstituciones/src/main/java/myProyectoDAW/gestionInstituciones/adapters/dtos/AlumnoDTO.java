package myProyectoDAW.gestionInstituciones.adapters.dtos;

import java.util.List;

import myProyectoDAW.gestionInstituciones.domain.models.Alumno;

public class AlumnoDTO implements java.io.Serializable {

    private Integer id;
    private String dni;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private List<AsignaturaDTO> asignaturas;
    private List<ModuloDTO> modulos;

    public AlumnoDTO() {
    }

    public AlumnoDTO(Integer id, String dni, String nombre, String apellido1, String apellido2) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    // Definicion e implementacion de getter and Setter

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido1() {
        return apellido1;
    }

    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    public String getApellido2() {
        return apellido2;
    }

    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    public List<AsignaturaDTO> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<AsignaturaDTO> asignaturas) {
        this.asignaturas = asignaturas;
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
        if (!(obj instanceof AlumnoDTO)) {
            return false;
        }
        AlumnoDTO other = (AlumnoDTO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AlumnosDTO{");
        sb.append("id=").append(id);
        sb.append(", dni=").append(dni);
        sb.append(", nombre=").append(nombre);
        sb.append(", apellido1=").append(apellido1);
        sb.append(", apellido2=").append(apellido2);
        sb.append('}');
        return sb.toString();
    }

    /********** METODOS DE CONVERSION **********/

    // DTO to Entity

    public Alumno convertirDTOAEntity() {

        Alumno alumno = new Alumno();

        alumno.setId(this.getId());
        alumno.setDni(this.getDni());
        alumno.setNombre(this.getNombre());
        alumno.setApellido1(this.getApellido1());
        alumno.setApellido2(this.getApellido2());
        return alumno;
    }

    // DTO to Entity

    public static AlumnoDTO convertirEntityADTO(Alumno alumno) {
        return new AlumnoDTO(
                alumno.getId(),
                alumno.getDni(),
                alumno.getNombre(),
                alumno.getApellido1(),
                alumno.getApellido2());
    }
}
