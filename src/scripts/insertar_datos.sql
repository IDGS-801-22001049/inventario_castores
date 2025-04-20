INSERT INTO roles (id, nombre) VALUES 
('ROLE_ADMIN'),
('ROLE_INVENTARIO'),
('ROLE_REGISTRO');

INSERT INTO usuarios (username, password, activo, nombre, email) VALUES 
('admin', '$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.', true, 'Administrador', 'admin@inventario.com');

INSERT INTO usuarios (username, password, activo, nombre, email) VALUES 
('inventario', '$2a$10$4YzQGfI7OYGzW7q7JQqY.e5XjL9N5mR8rX1JkZwZ4W7tVbQ2J3XqK', true, 'Responsable Inventario', 'inventario@empresa.com');

INSERT INTO usuarios (username, password, activo, nombre, email) VALUES 
('registro', '$2a$10$9mZQD5uV1z7cWq3vXJQZf.1VwLdN8rY2sK4pP7rR5tTbC1D2E3F4G', true, 'Registrador', 'registro@empresa.com');

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