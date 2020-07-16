package domain.usuarios;

public class Directivo extends Personal{
    String sindicato;

    public String getSindicato() {
        return sindicato;
    }

    public void setSindicato(String sindicato) {
        this.sindicato = sindicato;
    }

    @Override
    public String toString() {
        return "Directivo{" +
                "sindicato='" + sindicato + '\'' +
                ", usuario=" + usuario +
                ", numeroPersonal='" + numeroPersonal + '\'' +
                ", nombre='" + nombre + '\'' +
                ", rfc='" + rfc + '\'' +
                ", correo='" + correo + '\'' +
                ", fechaNacimiento='" + fechaNacimiento + '\'' +
                '}';
    }

}
