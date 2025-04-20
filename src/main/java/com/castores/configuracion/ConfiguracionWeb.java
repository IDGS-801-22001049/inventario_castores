package com.castores.configuracion;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ConfiguracionWeb implements WebMvcConfigurer {

    @Override
    public void addViewControllers(@NonNull ViewControllerRegistry registry) {
        registry.addViewController("/inicio-sesion").setViewName("autenticacion/inicio-sesion");
        registry.addViewController("/error/acceso-denegado").setViewName("error/acceso-denegado");
        registry.addViewController("/").setViewName("redirect:/productos");
    }
}
