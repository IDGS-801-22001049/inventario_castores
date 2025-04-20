CREATE DATABASE inventario_castores;
USE inventario_castores;

CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    cantidad INT NOT NULL,
    estado VARCHAR(50) NOT NULL
);

CREATE TABLE movimientos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    tipo VARCHAR(20) NOT NULL,
    fecha DATETIME NOT NULL,
    observaciones VARCHAR(255),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
);

CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE usuarios_roles (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    FOREIGN KEY (rol_id) REFERENCES roles(id)
);

INSERT INTO roles (nombre) VALUES 
('ROLE_ADMIN'),
('ROLE_INVENTARIO'),
('ROLE_REGISTRO');

INSERT INTO usuarios (username, password) VALUES 
('admin', '$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.');

INSERT INTO usuarios (username, password) VALUES 
('inventario', '$2a$10$4YzQGfI7OYGzW7q7JQqY.e5XjL9N5mR8rX1JkZwZ4W7tVbQ2J3XqK');

INSERT INTO usuarios (username, password) VALUES 
('registro', '$2a$10$9mZQD5uV1z7cWq3vXJQZf.1VwLdN8rY2sK4pP7rR5tTbC1D2E3F4G');

INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (1, 1); 
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (2, 2); 
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (3, 3); 

INSERT INTO productos (nombre, descripcion, precio, cantidad, estado) 
VALUES ('Laptop HP', 'Laptop HP ProBook 450 G7', 1200.00, 10, 'DISPONIBLE');

INSERT INTO productos (nombre, descripcion, precio, cantidad, estado) 
VALUES ('Mouse Logitech', 'Mouse inalámbrico M185', 25.50, 50, 'DISPONIBLE');

INSERT INTO productos (nombre, descripcion, precio, cantidad, estado) 
VALUES ('Teclado Microsoft', 'Teclado inalámbrico', 45.00, 30, 'DISPONIBLE');

INSERT INTO movimientos (producto_id, cantidad, tipo, fecha, observaciones)
VALUES (1, 2, 'SALIDA', '2023-01-15 10:30:00', 'Venta a cliente corporativo');

INSERT INTO movimientos (producto_id, cantidad, tipo, fecha, observaciones)
VALUES (2, 5, 'SALIDA', '2023-01-16 14:45:00', 'Venta al por mayor');

SELECT id, nombre, cantidad 
FROM productos 
WHERE cantidad < 5 
ORDER BY cantidad ASC;

SELECT m.id, p.nombre AS producto, m.cantidad, m.tipo, m.fecha 
FROM movimientos m
JOIN productos p ON m.producto_id = p.id
WHERE m.fecha >= DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH)
ORDER BY m.fecha DESC;

SELECT p.nombre, SUM(m.cantidad) AS total_vendido, SUM(m.cantidad * p.precio) AS total_ventas
FROM movimientos m
JOIN productos p ON m.producto_id = p.id
WHERE m.tipo = 'SALIDA'
GROUP BY p.nombre
ORDER BY total_ventas DESC;