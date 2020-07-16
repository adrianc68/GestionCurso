package domain;

import domain.enumeration.Estado;

import java.util.List;

public class Tema {
    int id;
    String nombre;
    Estado estado;
    String fecha;
    List<Actividad> actividades;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }

    @Override
    public String toString() {
        return "Tema{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                ", fecha='" + fecha + '\'' +
                ", actividades=" + actividades +
                '}';
    }

}
