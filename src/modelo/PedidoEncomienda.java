package modelo;

// Pedido de encomienda con logica especifica de asignacion y calculo de tiempo
public class PedidoEncomienda extends Pedido {

    public PedidoEncomienda(int id, String direccion, double distancia) {
        super(id, direccion, distancia);
    }

    // Sobrescritura: asigna repartidor automaticamente segun disponibilidad
    @Override
    public void asignarRepartidor() {
        this.repartidorAsignado = "Daniela Tapia";
        System.out.println("Repartidor " + repartidorAsignado + " asignado automaticamente al pedido #" + id);
    }

    // Calcula tiempo estimado considerando la distancia
    @Override
    public int calcularTiempoEntrega() {
        return (int) (distancia * 4); // 4 min por km
    }
}
