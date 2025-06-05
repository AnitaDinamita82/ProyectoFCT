package myProyectoDAW.gestionInstituciones.adapters.dtos;

/*DTO para las estadisticas de el Numero de Alumnos por asignatura (<--) */
public class AsignaturaAlumnosDTO {

    private String codigoAsignatura;
    private String nombreAsignatura;
    private Long numeroAlumnos;

    public AsignaturaAlumnosDTO() {
    }

    public AsignaturaAlumnosDTO(String codigoAsignatura, String nombreAsignatura, Long numeroAlumnos) {
        this.codigoAsignatura = codigoAsignatura;
        this.nombreAsignatura = nombreAsignatura;
        this.numeroAlumnos = numeroAlumnos;
    }

    /* Getter and Setter */

    public String getNombreAsignatura() {
        return nombreAsignatura;
    }

    public void setNombreAsignatura(String nombreAsignatura) {
        this.nombreAsignatura = nombreAsignatura;
    }

    public Long getNumeroAlumnos() {
        return numeroAlumnos;
    }

    public void setNumeroAlumnos(Long numeroAlumnos) {
        this.numeroAlumnos = numeroAlumnos;
    }

    public String getCodigoAsignatura() {
        return codigoAsignatura;
    }

    public void setCodigoAsignatura(String codigoAsignatura) {
        this.codigoAsignatura = codigoAsignatura;
    }

}
