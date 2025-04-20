package com.castores.repositorios;

import com.castores.modelos.Usuario;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Long> {

    @EntityGraph(attributePaths = "roles")
    Optional<Usuario> findByUsername(String username);

    @EntityGraph(attributePaths = "roles")
    Optional<Usuario> findWithRolesById(Long id);

    @Query("SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.roles")
    List<Usuario> findAllWithRoles();
}
