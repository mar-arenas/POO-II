package modelo;

/**
 * Clase que representa un repartidor en el sistema SpeedFast.
 * Entidad para gestión con base de datos.
 */
public class Repartidor {
    private int id;
    private String nombre;

    /**
     * Constructor completo para crear un repartidor desde la base de datos
     */
    public Repartidor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Constructor para crear un nuevo repartidor (sin ID de BD)
     */
    public Repartidor(String nombre) {
        this.nombre = nombre;
    }

    // Getters y Setters
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

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
