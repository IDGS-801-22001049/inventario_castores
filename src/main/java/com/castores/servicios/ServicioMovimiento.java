package com.castores.servicios;

import com.castores.modelos.Movimiento;
import com.castores.repositorios.MovimientoRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ServicioMovimiento {

    @Autowired
    private MovimientoRepositorio movimientoRepositorio;

    public List<Movimiento> obtenerTodosMovimientos() {
        return movimientoRepositorio.findAll();
    }

    public Movimiento registrarMovimiento(Movimiento movimiento) {
        return movimientoRepositorio.save(movimiento);
    }

    public List<Movimiento> obtenerMovimientosPorTipo(String tipo) {
        return movimientoRepositorio.findByTipoOrderByFechaDesc(tipo);
    }

    public List<Movimiento> obtenerMovimientosPorProducto(Long productoId) {
        return movimientoRepositorio.findByProductoIdOrderByFechaDesc(productoId);
    }
}
