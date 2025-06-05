package myProyectoDAW.gestionInstituciones.adapters.dtos;

/* DTO para las estadisticas de Alumnos con mas asignaturas en orden (-->) */
public class AlumnoAsignaturasDTO {

    private String dni;
    private String nombreAlumno;
    private String apellido1;
    private String apellido2;
    private Long numeroAsignaturas;

    public AlumnoAsignaturasDTO() {
    }

    public AlumnoAsignaturasDTO(String dni, String nombreAlumno, String apellido1, String apellido2,
            Long numeroAsignaturas) {
        this.dni = dni;
        this.nombreAlumno = nombreAlumno;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
        this.numeroAsignaturas = numeroAsignaturas;
    }

    /* Getter and Setter */

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public Long getNumeroAsignaturas() {
        return numeroAsignaturas;
    }

    public void setNumeroAsignaturas(Long numeroAsignaturas) {
        this.numeroAsignaturas = numeroAsignaturas;
    }

}
