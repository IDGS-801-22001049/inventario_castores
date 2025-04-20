package com.castores.servicios;

import com.castores.modelos.Usuario;
import com.castores.repositorios.UsuarioRepositorio;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.DisabledException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ServicioAutenticacion implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(ServicioAutenticacion.class);
    private final UsuarioRepositorio usuarioRepositorio;

    public ServicioAutenticacion(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Intentando autenticar usuario: {}", username);

        Usuario usuario = usuarioRepositorio.findByUsername(username).orElseThrow(() -> {
            logger.error("Usuario no encontrado: {}", username);
            return new UsernameNotFoundException("Usuario no encontrado: " + username);
        });

        if (!usuario.isActivo()) {
            logger.warn("Intento de login para usuario desactivado: {}", username);
            throw new DisabledException("Usuario desactivado");
        }

        List<GrantedAuthority> authorities = usuario.getRoles().stream().map(rol -> {
            logger.debug("Asignando rol: {} al usuario: {}", rol.getNombre(), username);
            return new SimpleGrantedAuthority(rol.getNombre());
        }).collect(Collectors.toList());

        logger.info("Autenticación exitosa para usuario: {}", username);
        return new User(usuario.getUsername(), usuario.getPassword(), authorities);
    }
}
