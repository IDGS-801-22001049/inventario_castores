package com.castores.controladores;

import com.castores.modelos.Producto;
import com.castores.servicios.ServicioProducto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductosController {

    private final ServicioProducto servicioProducto;
    private static final String REDIRECT_PRODUCTOS = "redirect:/productos";

    public ProductosController(ServicioProducto servicioProducto) {
        this.servicioProducto = servicioProducto;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INVENTARIO')")
    public String listarProductos(Model model, @RequestParam(required = false) String estado,
            @RequestParam(required = false) String mensaje,
            @RequestParam(required = false) String error) {

        List<Producto> productos;
        if (estado != null && !estado.isEmpty()) {
            productos = servicioProducto.obtenerProductosPorEstado(estado);
        } else {
            productos = servicioProducto.obtenerTodosProductos();
        }

        model.addAttribute("productos", productos);
        if (mensaje != null)
            model.addAttribute("mensaje", mensaje);
        if (error != null)
            model.addAttribute("error", error);

        return "inventario/lista";
    }

    @GetMapping("/agregar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String mostrarFormularioAgregar(Model model) {
        model.addAttribute("producto", new Producto());
        return "inventario/agregar";
    }

    @PostMapping("/agregar")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String procesarAgregarProducto(@ModelAttribute Producto producto,
            RedirectAttributes redirectAttributes) {
        try {
            servicioProducto.guardarProducto(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado exitosamente");
            return REDIRECT_PRODUCTOS;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Error al agregar producto: " + e.getMessage());
            return "redirect:/productos/agregar";
        }
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        try {
            Producto producto = servicioProducto.obtenerProductoPorId(id);
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado");
            }
            model.addAttribute("producto", producto);
            return "inventario/editar";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return REDIRECT_PRODUCTOS;
        }
    }

    @PostMapping("/editar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String procesarEditarProducto(@PathVariable Long id, @ModelAttribute Producto producto,
            RedirectAttributes redirectAttributes) {
        try {
            producto.setId(id);
            servicioProducto.guardarProducto(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar: " + e.getMessage());
        }
        return REDIRECT_PRODUCTOS;
    }

    @PostMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            servicioProducto.eliminarProducto(id);
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar: " + e.getMessage());
        }
        return REDIRECT_PRODUCTOS;
    }

    @PostMapping("/{id}/cambiar-estado")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String cambiarEstadoProducto(@PathVariable Long id, @RequestParam String estado,
            RedirectAttributes redirectAttributes) {
        try {
            Producto producto = servicioProducto.obtenerProductoPorId(id);
            if (producto == null) {
                throw new RuntimeException("Producto no encontrado");
            }
            producto.setEstado(estado);
            servicioProducto.guardarProducto(producto);
            redirectAttributes.addFlashAttribute("mensaje", "Estado actualizado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return REDIRECT_PRODUCTOS;
    }

    @GetMapping("/disponibles")
    @ResponseBody
    public List<Producto> obtenerProductosDisponibles() {
        return servicioProducto.obtenerProductosDisponibles();
    }
}
