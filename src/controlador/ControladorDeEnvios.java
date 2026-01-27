package controlador;

import interfaces.Despachable;
import interfaces.Cancelable;
import interfaces.Rastreable;
import modelo.Pedido;
import java.util.ArrayList;

// Controlador que gestiona las operaciones de envio implementando las interfaces
public class ControladorDeEnvios implements Despachable, Cancelable, Rastreable {
    private ArrayList<String> historial;
    private Pedido pedidoActual;

    public ControladorDeEnvios() {
        this.historial = new ArrayList<>();
    }

    // Establece el pedido a gestionar
    public void setPedido(Pedido pedido) {
        this.pedidoActual = pedido;
    }

    // Implementacion de Despachable: despacha el pedido actual
    @Override
    public void despachar() {
        if (pedidoActual != null && pedidoActual.getRepartidorAsignado() != null) {
            String registro = pedidoActual.getClass().getSimpleName() + " #" +
                pedidoActual.getId() + " - entregado por " +
                pedidoActual.getRepartidorAsignado();
            historial.add(registro);
            System.out.println("Pedido despachado correctamente.");
        } else {
            System.out.println("No se puede despachar: pedido sin repartidor asignado.");
        }
    }

    // Implementacion de Cancelable: cancela el pedido actual
    @Override
    public void cancelar() {
        if (pedidoActual != null) {
            System.out.println("\nCancelando Pedido Express #" + pedidoActual.getId() + "...");
            System.out.println("-> Pedido cancelado exitosamente.");
            pedidoActual = null;
        } else {
            System.out.println("No hay pedido activo para cancelar.");
        }
    }

    // Implementacion de Rastreable: muestra el historial de entregas
    @Override
    public void verHistorial() {
        System.out.println("\nHistorial:");
        if (historial.isEmpty()) {
            System.out.println("No hay entregas registradas.");
        } else {
            for (String registro : historial) {
                System.out.println("- " + registro);
            }
        }
    }
}
