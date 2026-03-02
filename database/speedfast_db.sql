-- Script SQL para crear la base de datos SpeedFast

DROP DATABASE IF EXISTS speedfast_db;
CREATE DATABASE speedfast_db;
USE speedfast_db;

CREATE TABLE repartidores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL
);

CREATE TABLE pedidos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(100) NOT NULL,
    tipo ENUM('COMIDA','ENCOMIENDA','EXPRESS'),
    estado ENUM('PENDIENTE','EN_REPARTO','ENTREGADO')
);

CREATE TABLE entregas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT,
    id_repartidor INT,
    fecha DATE,
    hora TIME,
    FOREIGN KEY (id_pedido) REFERENCES pedidos(id),
    FOREIGN KEY (id_repartidor) REFERENCES repartidores(id)
);

-- Datos de prueba
INSERT INTO repartidores (nombre) VALUES 
('Juan Pérez'),
('María González'),
('Carlos Rodríguez');

INSERT INTO pedidos (direccion, tipo, estado) VALUES 
('Av. Principal 123', 'COMIDA', 'PENDIENTE'),
('Calle Las Rosas 456', 'ENCOMIENDA', 'PENDIENTE'),
('Av. Santa Rosa 789', 'EXPRESS', 'EN_REPARTO'),
('Calle Los Pinos 321', 'COMIDA', 'PENDIENTE'),
('Av. Libertador 654', 'EXPRESS', 'ENTREGADO');

INSERT INTO entregas (id_pedido, id_repartidor, fecha, hora) VALUES 
(5, 1, CURDATE(), CURTIME());
