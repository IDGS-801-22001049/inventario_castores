package com.castores.repositorios;

import com.castores.modelos.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepositorio extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByTipoOrderByFechaDesc(String tipo);

    List<Movimiento> findByProductoIdOrderByFechaDesc(Long productoId);
}
