package com.castores.servicios;

import com.castores.modelos.Producto;
import com.castores.repositorios.ProductoRepositorio;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ServicioProducto {

    private final ProductoRepositorio productoRepositorio;

    @Autowired
    public ServicioProducto(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    public List<Producto> obtenerTodosProductos() {
        return productoRepositorio.findAll();
    }

    public List<Producto> obtenerProductosPorEstado(String estado) {
        return productoRepositorio.findByEstado(estado);
    }

    public List<Producto> obtenerProductosDisponibles() {
        return productoRepositorio.findByEstadoAndCantidadGreaterThan("DISPONIBLE", 0);
    }

    public Producto obtenerProductoPorId(Long id) {
        return productoRepositorio.findById(id).orElse(null);
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepositorio.save(producto);
    }

    public void eliminarProducto(Long id) {
        productoRepositorio.deleteById(id);
    }

    @Transactional
    public void aumentarCantidadProducto(Long id, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad a aumentar debe ser positiva");
        }

        Producto producto = productoRepositorio.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Producto con ID " + id + " no encontrado"));

        producto.setCantidad(producto.getCantidad() + cantidad);
        productoRepositorio.save(producto);
    }

    @Transactional
    public void disminuirCantidad(Long id, int cantidad) {
        Producto producto = productoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getCantidad() < cantidad) {
            throw new RuntimeException("No hay suficiente stock disponible");
        }

        producto.setCantidad(producto.getCantidad() - cantidad);
        productoRepositorio.save(producto);
    }
}
