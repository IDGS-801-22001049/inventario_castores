package com.castores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@SpringBootApplication
public class AplicacionInventario implements CommandLineRunner {

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(AplicacionInventario.class, args);
    }

    @Override
    public void run(String... args) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        crearOActualizarUsuario(jdbcTemplate, passwordEncoder, "admin", "admin123");
        crearOActualizarUsuario(jdbcTemplate, passwordEncoder, "inventario", "inventario1234");
    }

    private void crearOActualizarUsuario(JdbcTemplate jdbcTemplate, PasswordEncoder encoder,
            String username, String rawPassword) {
        try {
            String currentPassword = jdbcTemplate.queryForObject(
                    "SELECT password FROM usuarios WHERE username = ?", String.class, username);

            if (!encoder.matches(rawPassword, currentPassword)) {
                String passwordCodificada = encoder.encode(rawPassword);
                int updated =
                        jdbcTemplate.update("UPDATE usuarios SET password = ? WHERE username = ?",
                                passwordCodificada, username);
                System.out.println("✅ Contraseña actualizada para: " + username
                        + " (Filas afectadas: " + updated + ")");
            } else {
                System.out.println("🔒 La contraseña ya estaba actualizada para " + username + ".");
            }

        } catch (Exception e) {
            try {
                String passwordCodificada = encoder.encode(rawPassword);
                int inserted = jdbcTemplate.update(
                        "INSERT INTO usuarios (username, password) VALUES (?, ?)", username,
                        passwordCodificada);
                System.out.println(
                        "🆕 Usuario creado: " + username + " (Filas insertadas: " + inserted + ")");
            } catch (Exception insertException) {
                System.out.println("❌ Error al crear el usuario '" + username + "'");
                insertException.printStackTrace();
            }
        }
    }
}
