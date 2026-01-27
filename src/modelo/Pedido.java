package modelo;

// Clase abstracta base que define los atributos y comportamientos comunes de los pedidos
public abstract class Pedido {
    protected int id;
    protected String direccion;
    protected double distancia;
    protected String repartidorAsignado;

    // Constructor
    public Pedido(int id, String direccion, double distancia) {
        this.id = id;
        this.direccion = direccion;
        this.distancia = distancia;
        this.repartidorAsignado = null;
    }

    // Metodo implementado que muestra el resumen del pedido
    public void mostrarResumen() {
        System.out.println("\n[" + this.getClass().getSimpleName() + "]");
        System.out.println("Pedido #" + id);
        System.out.println("Direccion: " + direccion);
        System.out.println("Distancia: " + distancia + " km");
        System.out.println("Repartidor asignado: " + 
            (repartidorAsignado != null ? repartidorAsignado : "No asignado"));
        System.out.println("Tiempo estimado: " + calcularTiempoEntrega() + " minutos");
    }

    // Metodo abstracto que cada subclase debe implementar con su logica especifica
    public abstract int calcularTiempoEntrega();

    // Metodo para asignacion automatica de repartidor
    public abstract void asignarRepartidor();

    // Sobrecarga: asignacion manual de repartidor por nombre
    public void asignarRepartidor(String nombre) {
        this.repartidorAsignado = nombre;
        System.out.println("Repartidor " + nombre + " asignado manualmente al pedido #" + id);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getRepartidorAsignado() {
        return repartidorAsignado;
    }

    public String getDireccion() {
        return direccion;
    }

    public double getDistancia() {
        return distancia;
    }
}
