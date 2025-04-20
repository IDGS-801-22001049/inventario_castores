package com.castores.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class ConfiguracionSeguridad {

    private static final Logger logger = LoggerFactory.getLogger(ConfiguracionSeguridad.class);
    private final DataSource dataSource;

    public ConfiguracionSeguridad(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        SimpleUrlAuthenticationSuccessHandler handler = new SimpleUrlAuthenticationSuccessHandler();
        handler.setDefaultTargetUrl("/productos");
        handler.setAlwaysUseDefaultTargetUrl(true);
        return handler;
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        SimpleUrlLogoutSuccessHandler handler = new SimpleUrlLogoutSuccessHandler();
        handler.setDefaultTargetUrl("/inicio-sesion?logout=true");
        return handler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/inicio-sesion", "/login", "/error/**", "/css/**",
                                "/js/**", "/img/**", "/webjars/**")
                        .permitAll().requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/inventario/agregar", "/inventario/editar/**",
                                "/inventario/eliminar/**", "/inventario/*/aumentar",
                                "/inventario/*/disminuir", "/inventario/*/cambiar-estado",
                                "/movimientos/historial")
                        .hasAnyRole("ADMIN", "INVENTARIO").requestMatchers("/movimientos/salida")
                        .hasRole("INVENTARIO").anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/inicio-sesion").loginProcessingUrl("/login")
                        .usernameParameter("username").passwordParameter("password")
                        .successHandler(successHandler()).failureUrl("/inicio-sesion?error=true")
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler()).permitAll())
                .exceptionHandling(handling -> handling.accessDeniedPage("/error/acceso-denegado"))
                .authenticationProvider(authenticationProvider());

        logger.info("Configuración de seguridad inicializada correctamente");
        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            logger.info("Buscando usuario: {}", username);

            try {
                var usuario = jdbcTemplate.queryForObject(
                        "SELECT id, username, password FROM usuarios WHERE username = ?",
                        (rs, rowNum) -> {
                            Long id = rs.getLong("id");
                            String user = rs.getString("username");
                            String pass = rs.getString("password");
                            logger.info("Usuario encontrado: {}", user);
                            return new org.springframework.security.core.userdetails.User(user,
                                    pass, obtenerRoles(jdbcTemplate, id));
                        }, username);

                if (usuario == null) {
                    throw new UsernameNotFoundException("Usuario no encontrado: " + username);
                }

                return usuario;
            } catch (EmptyResultDataAccessException e) {
                logger.error("Usuario no encontrado: {}", username);
                throw new UsernameNotFoundException("Usuario no encontrado: " + username, e);
            }
        };
    }

    private List<GrantedAuthority> obtenerRoles(JdbcTemplate jdbcTemplate, Long userId) {
        try {
            return jdbcTemplate.query("SELECT r.nombre FROM roles r "
                    + "INNER JOIN usuarios_roles ur ON r.id = ur.rol_id "
                    + "WHERE ur.usuario_id = ?", (rs, rowNum) -> {
                        String roleName = rs.getString("nombre");
                        logger.debug("Asignando rol: {} al usuario ID: {}", roleName, userId);
                        return new SimpleGrantedAuthority(roleName);
                    }, userId);
        } catch (Exception e) {
            logger.warn("No se encontraron roles para el usuario ID: {}", userId);
            return Collections.emptyList();
        }
    }
}
