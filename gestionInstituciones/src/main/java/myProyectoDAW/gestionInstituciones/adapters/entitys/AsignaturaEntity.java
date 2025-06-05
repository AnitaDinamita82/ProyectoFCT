package myProyectoDAW.gestionInstituciones.adapters.entitys;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "asignaturas", schema = "instituto")
public class AsignaturaEntity implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "codigo", unique = true, nullable = false)
    private String codigo;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    /* Definicion de relacion FK para tabla alumnos - asignaturas */
    @ManyToMany(mappedBy = "asignaturas")
    @JsonIgnore
    private List<AlumnoEntity> alumnos;

    /* NUEVO: Definicion de relacion FK para la tabla modulos - asignaturas */
    @ManyToMany(mappedBy = "asignaturas")
    @JsonIgnore
    private List<ModuloEntity> modulos;

    public AsignaturaEntity() {
    }

    public AsignaturaEntity(Integer id, String codigo, String nombre, String descripcion) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    // Definicion de Getter and Setter
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

    public List<AlumnoEntity> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<AlumnoEntity> alumnos) {
        this.alumnos = alumnos;
    }

    /* NUEVOS getter and setter */

    public List<ModuloEntity> getModulos() {
        return modulos;
    }

    public void setModulos(List<ModuloEntity> modulos) {
        this.modulos = modulos;
    }

    // Otros Metodos

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
        if (!(obj instanceof AsignaturaEntity)) {
            return false;
        }
        AsignaturaEntity other = (AsignaturaEntity) obj;
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
        sb.append("Asignaturas{");
        sb.append("id=").append(id);
        sb.append(", codigo=").append(codigo);
        sb.append(", nombre=").append(nombre);
        sb.append(", descripcion=").append(descripcion);
        sb.append(", alumnos=").append(alumnos);
        sb.append('}');
        return sb.toString();
    }
}
