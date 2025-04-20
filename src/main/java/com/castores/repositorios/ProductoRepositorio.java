package com.castores.repositorios;

import com.castores.modelos.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductoRepositorio extends JpaRepository<Producto, Long> {
    List<Producto> findByEstado(String estado);

    List<Producto> findByEstadoAndCantidadGreaterThan(String estado, int cantidad);
}
