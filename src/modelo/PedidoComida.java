package modelo;

// Pedido de comida con logica especifica de asignacion y calculo de tiempo
public class PedidoComida extends Pedido {

    public PedidoComida(int id, String direccion, double distancia) {
        super(id, direccion, distancia);
    }

    // Sobrescritura: asigna repartidor automaticamente segun disponibilidad
    @Override
    public void asignarRepartidor() {
        this.repartidorAsignado = "Luis Diaz";
        System.out.println("Repartidor " + repartidorAsignado + " asignado automaticamente al pedido #" + id);
    }

    // Calcula tiempo estimado considerando la distancia y tiempo de preparacion
    @Override
    public int calcularTiempoEntrega() {
        return (int) (distancia * 5) + 15; // 5 min por km + 15 min de preparacion
    }
}
