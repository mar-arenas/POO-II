import modelo.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        System.out.println("[Zona de carga inicializada]\n");
        
        ZonaDeCarga zonaDeCarga = new ZonaDeCarga();

        zonaDeCarga.agregarPedido(new Pedido(1, "Av. Principal 123"));
        zonaDeCarga.agregarPedido(new Pedido(2, "Calle Las Rosas 456"));
        zonaDeCarga.agregarPedido(new Pedido(3, "Av. Santa Rosa 789"));
        zonaDeCarga.agregarPedido(new Pedido(4, "Calle Los Pinos 321"));
        zonaDeCarga.agregarPedido(new Pedido(5, "Av. Libertador 654"));

        System.out.println();

        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.execute(new Repartidor("Repartidor A", zonaDeCarga));
        executor.execute(new Repartidor("Repartidor B", zonaDeCarga));
        executor.execute(new Repartidor("Repartidor C", zonaDeCarga));

        executor.shutdown();

        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nTodos los pedidos han sido entregados correctamente");
    }
}

