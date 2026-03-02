package modelo;

/**
 * Clase que representa un pedido en el sistema SpeedFast.
 * Incluye información sobre dirección, tipo y estado del pedido.
 */
public class Pedido {
    private int id;
    private String direccionEntrega;
    private String tipo; // COMIDA, ENCOMIENDA, EXPRESS
    private EstadoPedido estado;

    /**
     * Constructor completo para crear un pedido desde la base de datos
     */
    public Pedido(int id, String direccionEntrega, String tipo, EstadoPedido estado) {
        this.id = id;
        this.direccionEntrega = direccionEntrega;
        this.tipo = tipo;
        this.estado = estado;
    }

    /**
     * Constructor para crear un nuevo pedido (sin ID de BD)
     */
    public Pedido(String direccionEntrega, String tipo) {
        this.direccionEntrega = direccionEntrega;
        this.tipo = tipo;
        this.estado = EstadoPedido.PENDIENTE;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido nuevoEstado) {
        this.estado = nuevoEstado;
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " [Tipo: " + tipo + ", Dirección: " + direccionEntrega + ", Estado: " + estado + "]";
    }
}
