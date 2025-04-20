package com.castores.controladores;

import com.castores.modelos.Producto;
import com.castores.servicios.ServicioProducto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private ServicioProducto servicioProducto;

    @GetMapping("/lista")
    public String listarProductos(Model model) {
        List<Producto> productos = servicioProducto.obtenerTodosProductos();
        model.addAttribute("productos", productos);
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
    public String agregarProducto(@ModelAttribute Producto producto,
            RedirectAttributes redirectAttributes) {
        servicioProducto.guardarProducto(producto);
        redirectAttributes.addFlashAttribute("mensaje", "Producto agregado exitosamente");
        return "redirect:/inventario/lista";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String mostrarFormularioEditar(@PathVariable Long id, Model model) {
        Producto producto = servicioProducto.obtenerProductoPorId(id);
        model.addAttribute("producto", producto);
        return "inventario/editar";
    }

    @PostMapping("/editar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editarProducto(@PathVariable Long id, @ModelAttribute Producto producto,
            RedirectAttributes redirectAttributes) {
        producto.setId(id);
        servicioProducto.guardarProducto(producto);
        redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
        return "redirect:/inventario/lista";
    }

    @PostMapping("/aumentar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole(''ROLE_INVENTARIO'')")
    @ResponseBody
    public ResponseEntity<?> aumentarInventario(@PathVariable("id") Long id,
            @RequestParam("cantidad") int cantidad) {
        try {
            servicioProducto.aumentarCantidadProducto(id, cantidad);
            return ResponseEntity.ok("Inventario aumentado");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{id}/disminuir")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String disminuirInventario(@PathVariable Long id, @RequestParam int cantidad,
            RedirectAttributes redirectAttributes) {
        try {
            servicioProducto.disminuirCantidad(id, cantidad);
            redirectAttributes.addFlashAttribute("mensaje", "Inventario disminuido exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/inventario/lista";
    }

    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        servicioProducto.eliminarProducto(id);
        redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado exitosamente");
        return "redirect:/inventario/lista";
    }

    @GetMapping("/por-estado")
    @ResponseBody
    public List<Producto> obtenerProductosPorEstado(@RequestParam String estado) {
        return servicioProducto.obtenerProductosPorEstado(estado);
    }
}
