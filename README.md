# SpeedFast - Sistema de Gestión de Entregas

## Descripción

SpeedFast es un sistema completo de gestión de entregas desarrollado en Java con interfaz gráfica Swing y persistencia en base de datos MySQL. Este proyecto implementa una solución integral para administrar el proceso de entregas en una empresa de logística.

El sistema permite gestionar tres componentes principales:

- **Repartidores**: Registro y administración del personal encargado de las entregas, incluyendo información de identificación y disponibilidad.

- **Pedidos**: Gestión completa de pedidos con soporte para diferentes tipos de servicio (COMIDA, ENCOMIENDA, EXPRESS) y seguimiento de estados (PENDIENTE, EN_REPARTO, ENTREGADO). Cada pedido incluye información detallada como dirección de entrega, descripción y cliente.

- **Entregas**: Registro del proceso de entrega que asocia pedidos específicos con repartidores asignados, incluyendo fecha, hora y observaciones relevantes para el seguimiento.

## Características Principales

- CRUD completo para Repartidores, Pedidos y Entregas
- Interfaz gráfica intuitiva desarrollada con Java Swing
- Persistencia de datos con MySQL utilizando JDBC
- Validación de datos de entrada en todos los formularios
- Manejo robusto de errores y excepciones SQL
- Sistema de filtros para consultas de pedidos por tipo y estado
- Arquitectura en capas clara (Modelo, DAO, Vista)
- Patrón Singleton para gestión de conexiones a base de datos

## Tecnologías Utilizadas

- **Java 17+**
- **MySQL 8.0+**
- **JDBC (MySQL Connector/J)**
- **Java Swing** para interfaz gráfica

## Estructura del Proyecto

```
POO-II/
├── database/
│   └── speedfast_db.sql          # Script SQL para crear la BD
├── lib/
│   └── mysql-connector-java.jar  # Conector JDBC (descargar)
├── src/
│   ├── conexion/
│   │   └── ConexionDB.java       # Gestión de conexión a BD
│   ├── dao/
│   │   ├── RepartidorDAO.java    # CRUD de Repartidores
│   │   ├── PedidoDAO.java        # CRUD de Pedidos
│   │   └── EntregaDAO.java       # CRUD de Entregas
│   ├── modelo/
│   │   ├── Repartidor.java       # Modelo Repartidor
│   │   ├── Pedido.java           # Modelo Pedido
│   │   ├── Entrega.java          # Modelo Entrega
│   │   └── EstadoPedido.java     # Enum de estados
│   ├── vista/
│   │   ├── VentanaPrincipal.java # Ventana principal
│   │   ├── PanelRepartidores.java
│   │   ├── PanelPedidos.java
│   │   └── PanelEntregas.java
│   └── Main.java                  # Clase principal
├── .gitignore
├── README.md
└── README_INSTALACION.md          # Instrucciones detalladas
```

## Arquitectura del Sistema

El proyecto sigue una arquitectura de tres capas:

### Capa de Modelo
Define las entidades del dominio:
- `Repartidor.java`: Representa un repartidor con id, nombre y disponibilidad
- `Pedido.java`: Modelo de pedido con dirección, descripción, tipo y estado
- `Entrega.java`: Relación entre pedido y repartidor con fecha y observaciones
- `EstadoPedido.java`: Enumeración de estados posibles (PENDIENTE, EN_REPARTO, ENTREGADO)

### Capa DAO (Data Access Object)
Gestiona la persistencia y acceso a datos:
- `RepartidorDAO.java`: Operaciones CRUD sobre repartidores
- `PedidoDAO.java`: Operaciones CRUD sobre pedidos con filtros por tipo y estado
- `EntregaDAO.java`: Operaciones CRUD sobre entregas

### Capa de Vista
Interfaz gráfica de usuario con Swing:
- `VentanaPrincipal.java`: Ventana principal con pestañas
- `PanelRepartidores.java`: Panel de gestión de repartidores
- `PanelPedidos.java`: Panel de gestión de pedidos
- `PanelEntregas.java`: Panel de gestión de entregas

### Conexión a Base de Datos
- `ConexionDB.java`: Clase singleton que gestiona la conexión a MySQL

## Uso del Sistema

### Ventana Principal
Al ejecutar la aplicación, se despliega una ventana principal con tres pestañas que permiten acceder a las diferentes funcionalidades:

1. **Repartidores**
   - Agregar nuevo repartidor con validación de nombre
   - Editar información de repartidor seleccionado
   - Eliminar repartidor del sistema
   - Visualizar lista completa en tabla interactiva

2. **Pedidos**
   - Registrar nuevo pedido con dirección, descripción, tipo y estado
   - Editar información de pedidos existentes
   - Eliminar pedidos del sistema
   - Filtrar pedidos por estado (PENDIENTE, EN_REPARTO, ENTREGADO)
   - Filtrar pedidos por tipo (COMIDA, ENCOMIENDA, EXPRESS)
   - Visualizar todos los pedidos en tabla

3. **Entregas**
   - Registrar nueva entrega seleccionando pedido y repartidor disponible
   - Establecer fecha y hora de entrega
   - Agregar observaciones relevantes sobre la entrega
   - Editar información de entregas registradas
   - Eliminar entregas del historial
   - Visualizar historial completo de entregas

### Validaciones Implementadas
El sistema incluye validaciones robustas para garantizar la integridad de los datos:
- Campos obligatorios no pueden quedar vacíos
- Longitud mínima de caracteres en campos de texto
- Formato correcto de fecha y hora
- Verificación de existencia de pedidos y repartidores al crear entregas
- Confirmación antes de eliminar registros

## Contexto Académico

**Asignatura:** Desarrollo Orientado a Objetos II
**Institución:** Duoc UC
**Estudiante:** Mariana Arenas Vergara
