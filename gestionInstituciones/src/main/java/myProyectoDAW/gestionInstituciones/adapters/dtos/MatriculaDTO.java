package myProyectoDAW.gestionInstituciones.adapters.dtos;

public class MatriculaDTO implements java.io.Serializable {

    private String dniAlumno;
    private String codigoAsignatura;
    private String codigoModulo;

    public MatriculaDTO() {
    }

    public MatriculaDTO(String dniAlumno, String codigoAsignatura) {

        this.dniAlumno = dniAlumno;
        this.codigoAsignatura = codigoAsignatura;
    }

    public MatriculaDTO(String dniAlumno, String codigoAsignatura, String codigoModulo) {
        this.dniAlumno = dniAlumno;
        this.codigoAsignatura = codigoAsignatura;
        this.codigoModulo = codigoModulo;
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

    public String getCodigoModulo() {
        return codigoModulo;
    }

    public void setCodigoModulo(String codigoModulo) {
        this.codigoModulo = codigoModulo;
    }

}
