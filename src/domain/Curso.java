package domain;

import domain.usuarios.Docente;
import domain.usuarios.Encargado;

public class Curso {
    String nombre;
    String descripcion;
    String seccion;
    String horario;
    String claveCurso;
    float duracion;
    Docente docente;
    Plan planCurso;
    Encargado encargado;

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

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getClaveCurso() {
        return claveCurso;
    }

    public void setClaveCurso(String claveCurso) {
        this.claveCurso = claveCurso;
    }

    public float getDuracion() {
        return duracion;
    }

    public void setDuracion(float duracion) {
        this.duracion = duracion;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Plan getPlanCurso() {
        return planCurso;
    }

    public void setPlanCurso(Plan planCurso) {
        this.planCurso = planCurso;
    }

    public Encargado getEncargado() {
        return encargado;
    }

    public void setEncargado(Encargado encargado) {
        this.encargado = encargado;
    }

    @Override
    public String toString() {
        return "Curso{" +
                "nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", seccion='" + seccion + '\'' +
                ", horario='" + horario + '\'' +
                ", claveCurso='" + claveCurso + '\'' +
                ", duracion=" + duracion +
                ", docente=" + docente +
                ", planCurso=" + planCurso +
                ", encargado=" + encargado +
                '}';
    }

}
