package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase que representa una entrega en el sistema SpeedFast.
 * Asocia un pedido con un repartidor y registra la fecha y hora de entrega.
 */
public class Entrega {
    private int id;
    private int idPedido;
    private int idRepartidor;
    private LocalDate fecha;
    private LocalTime hora;
    
    // Atributos adicionales para mostrar información completa en la GUI
    private String pedidoDireccion;
    private String repartidorNombre;

    /**
     * Constructor completo para crear una entrega desde la base de datos
     */
    public Entrega(int id, int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.id = id;
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    /**
     * Constructor para crear una nueva entrega (sin ID de BD)
     */
    public Entrega(int idPedido, int idRepartidor, LocalDate fecha, LocalTime hora) {
        this.idPedido = idPedido;
        this.idRepartidor = idRepartidor;
        this.fecha = fecha;
        this.hora = hora;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(int idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getPedidoDireccion() {
        return pedidoDireccion;
    }

    public void setPedidoDireccion(String pedidoDireccion) {
        this.pedidoDireccion = pedidoDireccion;
    }

    public String getRepartidorNombre() {
        return repartidorNombre;
    }

    public void setRepartidorNombre(String repartidorNombre) {
        this.repartidorNombre = repartidorNombre;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return "Entrega #" + id + " [Pedido: " + idPedido + ", Repartidor: " + idRepartidor + 
               ", Fecha: " + fecha.format(dateFormatter) + " " + hora.format(timeFormatter) + "]";
    }
}
