package myProyectoDAW.gestionInstituciones.adapters.dtos;

public class MatriculaDTO implements java.io.Serializable {

    private String dniAlumno;
    private String codigoAsignatura;

    public MatriculaDTO() {
    }

    public MatriculaDTO(String dniAlumno, String codigoAsignatura) {

        this.dniAlumno = dniAlumno;
        this.codigoAsignatura = codigoAsignatura;
    }

    // Definicion e implementacion de getter and Setter

    public String getDniAlumno() {
        return dniAlumno;
    }

    public void setDniAlumno(String dniAlumno) {
        this.dniAlumno = dniAlumno;
    }

    public String getCodigoAsignatura() {
        return codigoAsignatura;
    }

    public void setCodigoAsignatura(String codigoAsignatura) {
        this.codigoAsignatura = codigoAsignatura;
    }

    @Override
    public String toString() {
        return "MariculaDTO [ dniAlumno=" + dniAlumno + ", codigoAsignatura=" + codigoAsignatura + "]";
    }

}
