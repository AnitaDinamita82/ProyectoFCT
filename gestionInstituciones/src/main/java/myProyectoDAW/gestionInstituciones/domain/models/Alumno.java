package myProyectoDAW.gestionInstituciones.domain.models;

import java.util.List;

public class Alumno {

    private Integer id;
    private String dni;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private List<Asignatura> asignaturas;
    private List<Modulo> modulos; // ¡NUEVO! Lista de módulos a los que pertenece el alumno

    public Alumno() {
    }

    public Alumno(Integer id, String dni, String nombre, String apellido1, String apellido2,
            List<Asignatura> asignaturas, List<Modulo> modulos) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.asignaturas = asignaturas;
        this.modulos = modulos;
    }

    /* GETTER AND SETTER */

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

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

    public List<Modulo> getModulos() {
        return modulos;
    }

    public void setModulos(List<Modulo> modulos) {
        this.modulos = modulos;
    }

}
