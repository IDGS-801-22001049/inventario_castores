INSERT INTO roles (nombre) VALUES ('ADMIN');
INSERT INTO roles (nombre) VALUES ('INVENTARIO');
INSERT INTO roles (nombre) VALUES ('CONSULTA');

INSERT INTO usuarios (username, password) VALUES ('admin', '$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.');
-- Contraseña: admin123

INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES (1, 1);

INSERT INTO productos (nombre, descripcion, precio, cantidad, estado) 
VALUES ('Laptop HP', 'Laptop HP ProBook 450 G7', 1200.00, 10, 'DISPONIBLE');

INSERT INTO productos (nombre, descripcion, precio, cantidad, estado) 
VALUES ('Mouse Logitech', 'Mouse inalámbrico M185', 25.50, 50, 'DISPONIBLE');

INSERT INTO productos (nombre, descripcion, precio, cantidad, estado) 
VALUES ('Teclado Microsoft', 'Teclado inalámbrico', 45.00, 30, 'DISPONIBLE');