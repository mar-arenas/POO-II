import modelo.*;
import controlador.ControladorDeEnvios;

// Clase principal que simula el sistema de entregas SpeedFast
public class Main {
    public static void main(String[] args) {
        ControladorDeEnvios controlador = new ControladorDeEnvios();

        // Crear pedido de comida y asignar repartidor automaticamente
        PedidoComida pedidoComida = new PedidoComida(101, "Av. Principal 123", 5.0);
        pedidoComida.asignarRepartidor();
        pedidoComida.mostrarResumen();
        controlador.setPedido(pedidoComida);
        controlador.despachar();

        System.out.println("\n" + "=".repeat(50));

        // Crear pedido encomienda y asignar repartidor manualmente
        PedidoEncomienda pedidoEncomienda = new PedidoEncomienda(102, "Av. Santa Rosa 567", 7.0);
        pedidoEncomienda.asignarRepartidor("Daniela Tapia");
        pedidoEncomienda.mostrarResumen();
        controlador.setPedido(pedidoEncomienda);
        controlador.despachar();

        System.out.println("\n" + "=".repeat(50));

        // Crear pedido express y cancelarlo
        PedidoExpress pedidoExpress = new PedidoExpress(103, "Calle Los Pinos 890", 3.0);
        pedidoExpress.asignarRepartidor();
        pedidoExpress.mostrarResumen();
        controlador.setPedido(pedidoExpress);
        controlador.cancelar();

        System.out.println("\n" + "=".repeat(50));

        // Mostrar historial de entregas
        controlador.verHistorial();
    }
}
