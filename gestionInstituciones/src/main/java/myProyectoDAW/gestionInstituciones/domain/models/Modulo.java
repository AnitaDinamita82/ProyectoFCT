package myProyectoDAW.gestionInstituciones.domain.models;

import java.util.List;

public class Modulo {

    private Integer id;
    private String codigoModulo;
    private String nombreModulo;
    private String curso; // Primero o segundo
    private String grupo; // Presencial u Online
    private List<Alumno> alumnos; // Lista de alumnos en este módulo
    private List<Asignatura> asignaturas; // Lista de asignaturas en este módulo

    public Modulo() {
    }

    // Getter ann Setter

    public Modulo(Integer id, String codigoModulo, String nombreModulo, String curso, String grupo,
            List<Alumno> alumnos, List<Asignatura> asignaturas) {
        this.id = id;
        this.codigoModulo = codigoModulo;
        this.nombreModulo = nombreModulo;
        this.curso = curso;
        this.grupo = grupo;
        this.alumnos = alumnos;
        this.asignaturas = asignaturas;
    }

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

    /* Relacion Many to Many */
    public List<Alumno> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<Alumno> alumnos) {
        this.alumnos = alumnos;
    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }

}
