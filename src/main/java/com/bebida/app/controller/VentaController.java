package com.bebida.app.controller;

import com.bebida.app.dto.PagoRequestDTO;
import com.bebida.app.dto.ProductoDTO;
import com.bebida.app.dto.UsuarioDTO;
import com.bebida.app.dto.VentaDTO;
import com.bebida.app.dto.VentaRequestDTO;
import com.bebida.app.dto.DetalleVentaRequestDTO;
import com.bebida.app.entity.Venta;
import com.bebida.app.repository.VentaRepository;
import com.bebida.app.service.PagoService;
import com.bebida.app.service.ProductoService;
import com.bebida.app.service.UsuarioService;
import com.bebida.app.service.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal; // Para saber quién está logueado
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ventas")
public class VentaController {
    
    @Autowired
    private VentaService ventaService;
    
    @Autowired
    private PagoService pagoService;
    
    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private UsuarioService usuarioService; // Necesitamos esto para buscar al usuario
    
    @Autowired
    private com.bebida.app.repository.VentaRepository ventaRepository;
    
    // 1. PANTALLA DE CHECKOUT (Resumen antes de pagar)
    @GetMapping("/checkout")
    public String checkout(HttpSession session, Model model, Principal principal, RedirectAttributes redirectAttributes) {
        // Validar carrito
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute("carrito");
        if (carrito == null || carrito.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Tu carrito está vacío.");
            return "redirect:/productos";
        }
        
        // Obtener datos del usuario logueado
        UsuarioDTO usuario = usuarioService.obtenerPorEmail(principal.getName());
        
        // Calcular total
        BigDecimal total = calcularTotal(carrito);
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("total", total);
        model.addAttribute("titulo", "Finalizar Compra");
        
        return "ventas/checkout";
    }
    
    // 2. PROCESAR LA COMPRA (Acción de pagar)
    @PostMapping("/procesar")
    public String procesarVenta(@RequestParam String metodoPago,
                               HttpSession session,
                               Principal principal,
                               RedirectAttributes redirectAttributes) {
        try {
            Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute("carrito");
            if (carrito == null || carrito.isEmpty()) {
                return "redirect:/productos";
            }
            
            // Buscar usuario real
            UsuarioDTO usuario = usuarioService.obtenerPorEmail(principal.getName());
            
            // Armar el DTO de Venta
            VentaRequestDTO ventaRequest = new VentaRequestDTO();
            ventaRequest.setIdCliente(usuario.getIdUsuario()); // Usamos el ID del usuario logueado
            ventaRequest.setMetodoPago(metodoPago);
            
            List<DetalleVentaRequestDTO> detalles = new ArrayList<>();
            for (Map.Entry<Long, Integer> entry : carrito.entrySet()) {
                DetalleVentaRequestDTO detalle = new DetalleVentaRequestDTO();
                detalle.setIdProducto(entry.getKey());
                detalle.setCantidad(entry.getValue());
                detalles.add(detalle);
            }
            ventaRequest.setDetalles(detalles);
            
            // Guardar Venta
            VentaDTO ventaGuardada = ventaService.crear(ventaRequest);
            
            // Guardamos el registro del pago asociado a la venta
            PagoRequestDTO pagoRequest = new PagoRequestDTO();
            pagoRequest.setIdVenta(ventaGuardada.getIdVenta());
            
            
            pagoRequest.setMetodoPago(metodoPago);

            pagoRequest.setMonto(ventaGuardada.getTotal());
            pagoService.crear(pagoRequest);
    
            session.removeAttribute("carrito");
            
            return "redirect:/ventas/confirmacion/" + ventaGuardada.getIdVenta();
            
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la compra: " + e.getMessage());
            return "redirect:/ventas/checkout";
        }
    }
    
    // 3. PANTALLA DE CONFIRMACIÓN (Ticket)
    @GetMapping("/confirmacion/{id}")
    public String confirmacion(@PathVariable Long id, Model model, Principal principal) {
        try {
            VentaDTO venta = ventaService.obtenerPorId(id);
            UsuarioDTO usuario = usuarioService.obtenerPorEmail(principal.getName());

            // Seguridad: Que nadie vea las compras de otro
            
            model.addAttribute("venta", venta);
            model.addAttribute("titulo", "¡Compra Exitosa!");
            return "ventas/confirmacion";
        } catch (RuntimeException e) {
            return "redirect:/productos";
        }
    }
    
    private BigDecimal calcularTotal(Map<Long, Integer> carrito) {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<Long, Integer> entry : carrito.entrySet()) {
            ProductoDTO p = productoService.obtenerPorId(entry.getKey());
            total = total.add(p.getPrecioUnitario().multiply(BigDecimal.valueOf(entry.getValue())));
        }
        return total;
    }
    
    
    //El cliente puede ver su historial de compras
    @GetMapping("/historial")
    public String historialCompras(Model model, Principal principal) {
        // Buscamos al usuario logueado
        UsuarioDTO usuario = usuarioService.obtenerPorEmail(principal.getName());
        
        // 2. Buscamos sus ventas
        List<Venta> ventas = ventaRepository.findByCliente_IdUsuarioOrderByFechaDesc(usuario.getIdUsuario());
        
        model.addAttribute("ventas", ventas);
        model.addAttribute("titulo", "Mis Compras");
        return "ventas/historial";
    }
}