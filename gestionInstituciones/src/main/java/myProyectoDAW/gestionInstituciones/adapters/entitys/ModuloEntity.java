package myProyectoDAW.gestionInstituciones.adapters.entitys;

import java.util.ArrayList;
import java.util.List;

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
@Table(name = "modulos", schema = "instituto")

public class ModuloEntity implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "codigoModulo", unique = true, nullable = false, length = 5)
    private String codigoModulo;

    @Column(name = "nombreModulo", nullable = false, length = 50)
    private String nombreModulo;

    @Column(name = "curso", length = 10)
    private String curso;

    @Column(name = "grupo", length = 10)
    private String grupo;

    /* Relacion many to many Modulos - Alumnos */
    @ManyToMany
    @JoinTable(name = "modulos_alumnos", schema = "instituto", joinColumns = @JoinColumn(name = "cod_modulo", referencedColumnName = "codigoModulo"), inverseJoinColumns = @JoinColumn(name = "dni_alumno", referencedColumnName = "dni"))

    private List<AlumnoEntity> alumnos = new ArrayList<>(); // Inicializar para evitar NullPointerException

    @ManyToMany
    @JoinTable(name = "modulos_asignaturas", schema = "instituto", joinColumns = @JoinColumn(name = "cod_modulo", referencedColumnName = "codigoModulo"), inverseJoinColumns = @JoinColumn(name = "cod_asignatura", referencedColumnName = "codigo"))
    private List<AsignaturaEntity> asignaturas = new ArrayList<>(); // Inicializar para evitar NullPointerException

    public ModuloEntity() {
    }

    public ModuloEntity(Integer id, String codigoModulo, String nombreModulo, String curso, String grupo) {
        this.id = id;
        this.codigoModulo = codigoModulo;
        this.nombreModulo = nombreModulo;
        this.curso = curso;
        this.grupo = grupo;
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

    /* NUEVOS getter and setter */
    public List<AlumnoEntity> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<AlumnoEntity> alumnos) {
        this.alumnos = alumnos;
    }

    public List<AsignaturaEntity> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<AsignaturaEntity> asignaturas) {
        this.asignaturas = asignaturas;
    }

    /* Otros Metodos */
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
        if (getClass() != obj.getClass())
            return false;
        ModuloEntity other = (ModuloEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ModuloEntity [id=" + id + ", codigoModulo=" + codigoModulo + ", nombreModulo=" + nombreModulo
                + ", curso=" + curso + ", grupo=" + grupo + "]";
    }

}
