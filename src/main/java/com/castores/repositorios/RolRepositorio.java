package com.castores.repositorios;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.castores.modelos.Rol;

public interface RolRepositorio extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}
