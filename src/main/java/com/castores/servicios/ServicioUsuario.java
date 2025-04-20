package com.castores.servicios;

import com.castores.modelos.Rol;
import com.castores.modelos.Usuario;
import com.castores.repositorios.RolRepositorio;
import com.castores.repositorios.UsuarioRepositorio;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ServicioUsuario {

    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepositorio rolRepositorio;
    private final PasswordEncoder passwordEncoder;

    public ServicioUsuario(UsuarioRepositorio usuarioRepositorio, RolRepositorio rolRepositorio,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.rolRepositorio = rolRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodosUsuarios() {
        return usuarioRepositorio.findAllWithRoles();
    }

    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepositorio.findWithRolesById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Rol> obtenerTodosRoles() {
        return rolRepositorio.findAll();
    }

    @Transactional
    public void guardarUsuario(Usuario usuario, List<Long> rolesIds) {
        if (usuario.getId() == null) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        } else {
            Usuario usuarioExistente = usuarioRepositorio.findById(usuario.getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
                usuario.setPassword(usuarioExistente.getPassword());
            } else {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            }
        }

        Set<Rol> roles = rolesIds.stream()
                .map(rolId -> rolRepositorio.findById(rolId)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado")))
                .collect(Collectors.toSet());

        usuario.setRoles(roles);
        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        usuarioRepositorio.deleteById(id);
    }
}
