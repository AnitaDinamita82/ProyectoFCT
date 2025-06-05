package myProyectoDAW.gestionInstituciones.domain.models;

import java.util.List;

public class Asignatura {

    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private List<Alumno> alumnos;
    private List<Modulo> modulos; // Lista de módulos a los que pertenece la asignatura

    public Asignatura() {
    }

    public Asignatura(Integer id, String codigo, String nombre, String descripcion, List<Alumno> alumnos,
            List<Modulo> modulos) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.alumnos = alumnos;
        this.modulos = modulos;
    }

    /* GETTER AND SETTER */

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

    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }

}
