package domain.usuarios;

public class Encargado extends Personal {
    String area;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "Encargado{" +
                "area='" + area + '\'' +
                ", usuario=" + usuario +
                ", numeroPersonal='" + numeroPersonal + '\'' +
                ", nombre='" + nombre + '\'' +
                ", rfc='" + rfc + '\'' +
                ", correo='" + correo + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                '}';
    }

}
