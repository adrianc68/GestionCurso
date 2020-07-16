package domain;

import domain.enumeration.EstadoPlan;
import domain.usuarios.Docente;

import java.util.List;

public class Plan {
    int id;
    float avance;
    String fechaElaboracion;
    String fechaActualizacion;
    EstadoPlan estado;
    Docente docente;
    List<Tema> listaTemas;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getAvance() {
        return avance;
    }

    public void setAvance(float avance) {
        this.avance = avance;
    }

    public String getFechaElaboracion() {
        return fechaElaboracion;
    }

    public void setFechaElaboracion(String fechaElaboracion) {
        this.fechaElaboracion = fechaElaboracion;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public EstadoPlan getEstado() {
        return estado;
    }

    public void setEstado(EstadoPlan estado) {
        this.estado = estado;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public List<Tema> getListaTemas() {
        return listaTemas;
    }

    public void setListaTemas(List<Tema> listaTemas) {
        this.listaTemas = listaTemas;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", avance=" + avance +
                ", fechaElaboracion='" + fechaElaboracion + '\'' +
                ", fechaActualizacion='" + fechaActualizacion + '\'' +
                ", estado=" + estado +
                ", docente=" + docente +
                ", listaTemas=" + listaTemas +
                '}';
    }

}
