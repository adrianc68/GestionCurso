package domain.usuarios;

import domain.Curso;
import domain.Plan;
import domain.enumeration.PerfilProfesional;
import java.util.List;

public class Docente extends Personal {
    int anosExperiencia;
    PerfilProfesional perfilProfesional;
    Encargado encargado;
    List<Plan> listaPlanCurso;
    List<Curso> listaCursos;

    public int getAnosExperiencia() {
        return anosExperiencia;
    }

    public void setAnosExperiencia(int anosExperiencia) {
        this.anosExperiencia = anosExperiencia;
    }

    public PerfilProfesional getPerfilProfesional() {
        return perfilProfesional;
    }

    public void setPerfilProfesional(PerfilProfesional perfilProfesional) {
        this.perfilProfesional = perfilProfesional;
    }

    public Encargado getEncargado() {
        return encargado;
    }

    public void setEncargado(Encargado encargado) {
        this.encargado = encargado;
    }

    public List<Plan> getListaPlanCurso() {
        return listaPlanCurso;
    }

    public void setListaPlanCurso(List<Plan> listaPlanCurso) {
        this.listaPlanCurso = listaPlanCurso;
    }

    public List<Curso> getListaCursos() {
        return listaCursos;
    }

    public void setListaCursos(List<Curso> listaCursos) {
        this.listaCursos = listaCursos;
    }

    @Override
    public String toString() {
        return "Docente{" +
                "anosExperiencia=" + anosExperiencia +
                ", perfilProfesional=" + perfilProfesional +
                ", encargado=" + encargado +
                ", listaPlanCurso=" + listaPlanCurso +
                ", listaCursos=" + listaCursos +
                ", usuario=" + usuario +
                ", numeroPersonal='" + numeroPersonal + '\'' +
                ", nombre='" + nombre + '\'' +
                ", rfc='" + rfc + '\'' +
                ", correo='" + correo + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                '}';
    }

}
