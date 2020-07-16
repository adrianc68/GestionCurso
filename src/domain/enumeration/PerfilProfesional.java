package domain.enumeration;

public enum PerfilProfesional {
    PROFESIONALES("Profesionales"),
    MEDIO_SUPERIOR("Medio_Superior"),
    SUPERIOR("Superior");

    private String id;

    PerfilProfesional(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
