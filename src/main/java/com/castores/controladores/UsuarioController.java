package com.castores.controladores;

import com.castores.modelos.Usuario;
import com.castores.servicios.ServicioUsuario;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UsuarioController {

    private final ServicioUsuario servicioUsuario;

    public UsuarioController(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", servicioUsuario.obtenerTodosUsuarios());
        return "admin/usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", servicioUsuario.obtenerTodosRoles());
        return "admin/usuarios/formulario";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario,
            @RequestParam(required = false) List<Long> rolesIds,
            RedirectAttributes redirectAttributes) {
        servicioUsuario.guardarUsuario(usuario, rolesIds);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario guardado exitosamente");
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", servicioUsuario.obtenerUsuarioPorId(id));
        model.addAttribute("roles", servicioUsuario.obtenerTodosRoles());
        return "admin/usuarios/formulario";
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        servicioUsuario.eliminarUsuario(id);
        redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
        return "redirect:/admin/usuarios";
    }
}
