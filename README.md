# Sistema de Entregas SpeedFast

Sistema de gestión de pedidos que implementa POO con clases abstractas, polimorfismo e interfaces.

## Estructura del Proyecto

```
src/
├── interfaces/          # Interfaces para desacoplar responsabilidades
├── modelo/              # Clases de dominio (Pedido y subclases)
├── controlador/         # Lógica de control de envíos
└── Main.java           # Clase principal
```

## Características

- **Polimorfismo**: Sobrescritura de `asignarRepartidor()` y sobrecarga con parámetro String
- **Abstracción**: Clase abstracta `Pedido` con método abstracto `calcularTiempoEntrega()`
- **Interfaces**: `Despachable`, `Cancelable`, `Rastreable` implementadas en `ControladorDeEnvios`

## Tipos de Pedido

- **PedidoComida**: Incluye tiempo de preparación
- **PedidoEncomienda**: Cálculo estándar por distancia
- **PedidoExpress**: Entrega prioritaria y rápida
