package modelo;

public class Repartidor implements Runnable {
    private String nombre;
    private ZonaDeCarga zonaDeCarga;

    public Repartidor(String nombre, ZonaDeCarga zonaDeCarga) {
        this.nombre = nombre;
        this.zonaDeCarga = zonaDeCarga;
    }

    @Override
    public void run() {
        while (zonaDeCarga.hayPedidos()) {
            Pedido pedido = zonaDeCarga.retirarPedido();
            
            if (pedido != null) {
                pedido.setEstado(EstadoPedido.EN_REPARTO);
                System.out.println(nombre + " retiró " + pedido.toString());
                
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                
                pedido.setEstado(EstadoPedido.ENTREGADO);
                System.out.println(nombre + " entregó " + pedido.toString());
            }
        }
    }
}
