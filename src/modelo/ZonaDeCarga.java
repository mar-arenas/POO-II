package modelo;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ZonaDeCarga {
    private BlockingQueue<Pedido> pedidosPendientes;

    public ZonaDeCarga() {
        this.pedidosPendientes = new LinkedBlockingQueue<>();
    }

    public synchronized void agregarPedido(Pedido p) {
        pedidosPendientes.offer(p);
        System.out.println("Pedido #" + p.getId() + " agregado a la zona de carga");
    }

    public synchronized Pedido retirarPedido() {
        return pedidosPendientes.poll();
    }

    public synchronized boolean hayPedidos() {
        return !pedidosPendientes.isEmpty();
    }
}
