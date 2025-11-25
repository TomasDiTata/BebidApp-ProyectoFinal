package com.bebida.app.controller;

import com.bebida.app.dto.ProductoDTO;
import com.bebida.app.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private ProductoService productoService;

    //AGREGAR PRODUCTO AL CARRITO
    @PostMapping("/agregar")
    public String agregarAlCarrito(@RequestParam Long idProducto,
                                   @RequestParam Integer cantidad,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        try {
            
            ProductoDTO producto = productoService.obtenerPorId(idProducto);
            
            if (producto.getStock() < cantidad) {
                redirectAttributes.addFlashAttribute("error", "No hay suficiente stock. Disponibles: " + producto.getStock());
                return "redirect:/productos/" + idProducto;
            }

            
            Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute("carrito");
            if (carrito == null) {
                carrito = new HashMap<>();
            }

            
            if (carrito.containsKey(idProducto)) {
                int cantidadActual = carrito.get(idProducto);
                // Validamos que la suma no supere el stock real
                if (cantidadActual + cantidad > producto.getStock()) {
                     redirectAttributes.addFlashAttribute("error", "No puedes agregar más del stock disponible.");
                     return "redirect:/carrito";
                }
                carrito.put(idProducto, cantidadActual + cantidad);
            } else {
                carrito.put(idProducto, cantidad);
            }

            // Guardamos el carrito actualizado en la sesión
            session.setAttribute("carrito", carrito);
            
            redirectAttributes.addFlashAttribute("mensaje", "Producto agregado al carrito.");
            return "redirect:/productos"; // Redirige al catalogo para seguir comprando

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/productos";
        }
    }

    //VER EL CARRITO
    @GetMapping
    public String verCarrito(HttpSession session, Model model) {
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute("carrito");
        
        // Si no hay carrito, inicializamos uno vacío
        if (carrito == null) {
            carrito = new HashMap<>();
        }

        
        Map<ProductoDTO, Integer> productosEnCarrito = new HashMap<>();
        BigDecimal total = BigDecimal.ZERO;

        for (Map.Entry<Long, Integer> entry : carrito.entrySet()) {
            ProductoDTO p = productoService.obtenerPorId(entry.getKey());
            productosEnCarrito.put(p, entry.getValue());
            
            // Calculamos subtotal: Precio * Cantidad
            BigDecimal subtotal = p.getPrecioUnitario().multiply(BigDecimal.valueOf(entry.getValue()));
            total = total.add(subtotal);
        }

        model.addAttribute("items", productosEnCarrito);
        model.addAttribute("total", total);
        model.addAttribute("titulo", "Tu Carrito de Compras");
        
        return "ventas/carrito";
    }

    //ELIMINAR ITEM
    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id, HttpSession session) {
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute("carrito");
        if (carrito != null) {
            carrito.remove(id);
            session.setAttribute("carrito", carrito);
        }
        return "redirect:/carrito";
    }
    
    //VACIAR CARRITO
    @GetMapping("/vaciar")
    public String vaciarCarrito(HttpSession session) {
        session.removeAttribute("carrito");
        return "redirect:/carrito";
    }
}