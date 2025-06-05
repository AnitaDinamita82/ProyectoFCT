package myProyectoDAW.gestionInstituciones.adapters.entitys;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "alumnos", schema = "instituto")
public class AlumnoEntity implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "dni", unique = true, nullable = false, length = 50)
    private String dni;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "apellido1", length = 100)
    private String apellido1;

    @Column(name = "apellido2", length = 100)
    private String apellido2;

    public AlumnoEntity() {
    }

    public AlumnoEntity(Integer id, String dni, String nombre, String apellido1, String apellido2) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    // Definicion de Getter and Setter

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

    // Relacion FK tabla modulos - alumnos

    @ManyToMany(mappedBy = "alumnos") // "alumnos" es el nombre del atributo en ModuloEntity
    @JsonIgnore
    private List<ModuloEntity> modulos;

    public List<ModuloEntity> getModulos() {
        return modulos;
    }

    public void setModulos(List<ModuloEntity> modulos) {
        this.modulos = modulos;
    }

    // Relacion FK tabla alumnos - asignaturas
    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "alumnos_asignaturas", schema = "instituto", joinColumns = @JoinColumn(name = "dni_alu", referencedColumnName = "dni"), inverseJoinColumns = @JoinColumn(name = "codigo_asig", referencedColumnName = "codigo")

    )

    private List<AsignaturaEntity> asignaturas; // Indica que la relación está gestionada por el atributo asignaturas en
                                                // la entidad Alumno

    public List<AsignaturaEntity> getAsignatura() {
        return asignaturas;
    }

    public void setAsignatura(List<AsignaturaEntity> asignaturas) {
        this.asignaturas = asignaturas;
    }

    // Otros Metodos

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AlumnoEntity)) {
            return false;
        }
        final AlumnoEntity other = (AlumnoEntity) obj;
        return this.getId().equals(other.getId());
    }

    @Override
    public String toString() {
        return "Alumnos [id=" + id + ", dni=" + dni + ", nombre=" + nombre + ", apellido1=" + apellido1 + ", apellido2="
                + apellido2 + "]";
    }

}
