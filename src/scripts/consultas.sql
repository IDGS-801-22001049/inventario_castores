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