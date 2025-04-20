package com.castores.controladores;

import com.castores.modelos.Usuario;
import com.castores.servicios.ServicioUsuario;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
        Usuario usuario = new Usuario();
        model.addAttribute("usuario", usuario);
        model.addAttribute("username", usuario.getUsername()); // Asegura que el atributo exista
        model.addAttribute("password", ""); // Asegura que el atributo exista
        model.addAttribute("roles", servicioUsuario.obtenerTodosRoles());
        return "admin/usuarios/formulario";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Long id, Model model) {
        Usuario usuario = servicioUsuario.obtenerUsuarioPorId(id);
        model.addAttribute("usuario", usuario);
        model.addAttribute("username", usuario.getUsername()); // Asegura que el atributo exista
        model.addAttribute("password", ""); // Asegura que el atributo exista
        model.addAttribute("roles", servicioUsuario.obtenerTodosRoles());
        return "admin/usuarios/formulario";
    }

    @PostMapping(value = {"/guardar", "/guardar/{id}"})
    public String guardarUsuario(@PathVariable(required = false) Long id,
            @ModelAttribute("usuario") Usuario usuario,
            @RequestParam("rolesIds") List<Long> rolesIds, RedirectAttributes redirectAttributes) {

        if (id != null) {
            usuario.setId(id);
        }

        try {
            servicioUsuario.guardarUsuario(usuario, rolesIds);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario guardado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Error al guardar usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");

            return (id != null) ? "redirect:/admin/usuarios/editar/" + id
                    : "redirect:/admin/usuarios/nuevo";
        }

        return "redirect:/admin/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            servicioUsuario.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
            redirectAttributes.addFlashAttribute("tipoMensaje", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensaje",
                    "Error al eliminar usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("tipoMensaje", "error");
        }
        return "redirect:/admin/usuarios";
    }
}
