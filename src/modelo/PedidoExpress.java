package modelo;

// Pedido express con logica especifica de asignacion y calculo de tiempo
public class PedidoExpress extends Pedido {

    public PedidoExpress(int id, String direccion, double distancia) {
        super(id, direccion, distancia);
    }

    // Sobrescritura: asigna repartidor automaticamente segun disponibilidad
    @Override
    public void asignarRepartidor() {
        this.repartidorAsignado = "Carlos Mendez";
        System.out.println("Repartidor " + repartidorAsignado + " asignado automaticamente al pedido #" + id);
    }

    // Calcula tiempo estimado con prioridad express (mas rapido)
    @Override
    public int calcularTiempoEntrega() {
        return (int) (distancia * 3); // 3 min por km (servicio rapido)
    }
}
