package com.castores.controladores;

import com.castores.modelos.Movimiento;
import com.castores.modelos.Producto;
import com.castores.servicios.ServicioMovimiento;
import com.castores.servicios.ServicioProducto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/movimientos")
public class MovimientosController {

    @Autowired
    private ServicioMovimiento servicioMovimiento;

    @Autowired
    private ServicioProducto servicioProducto;

    @GetMapping("/historial")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INVENTARIO')")
    public String mostrarHistorial(Model model) {
        List<Movimiento> movimientos = servicioMovimiento.obtenerTodosMovimientos();
        model.addAttribute("movimientos", movimientos);
        return "movimientos/historial";
    }

    @GetMapping("/salida")
    @PreAuthorize("hasRole('ROLE_INVENTARIO')")
    public String mostrarFormularioSalida(Model model) {
        List<Producto> productos = servicioProducto.obtenerProductosDisponibles();
        model.addAttribute("movimiento", new Movimiento());
        model.addAttribute("productos", productos);
        return "movimientos/salida";
    }

    @PostMapping("/salida")
    @PreAuthorize("hasRole('ROLE_INVENTARIO')")
    public String registrarSalida(@ModelAttribute Movimiento movimiento,
            RedirectAttributes redirectAttributes) {
        movimiento.setTipo("SALIDA");
        movimiento.setFecha(LocalDateTime.now());

        servicioMovimiento.registrarMovimiento(movimiento);

        Producto producto = servicioProducto.obtenerProductoPorId(movimiento.getProducto().getId());
        producto.setCantidad(producto.getCantidad() - movimiento.getCantidad());
        servicioProducto.guardarProducto(producto);

        redirectAttributes.addFlashAttribute("mensaje", "Salida registrada exitosamente");
        return "redirect:/movimientos/historial";
    }
}
