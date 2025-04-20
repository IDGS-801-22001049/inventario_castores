package com.castores.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AutenticacionController {

    @GetMapping("/inicio-sesion")
    public String mostrarInicioSesion() {
        return "autenticacion/inicio-sesion";
    }
}
