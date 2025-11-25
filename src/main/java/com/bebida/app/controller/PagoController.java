package com.bebida.app.controller;

import com.bebida.app.dto.PagoDTO;
import com.bebida.app.service.PagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pagos")
public class PagoController {
    
    @Autowired
    private PagoService pagoService;
    
    // Ver historial de pagos (admin)
    @GetMapping("/admin/historial")
    public String verHistorial(Model model) {
        List<PagoDTO> pagos = pagoService.obtenerTodos();
        model.addAttribute("pagos", pagos);
        model.addAttribute("titulo", "Historial de Pagos");
        return "pagos/historial";
    }
    
    // Ver pagos de una venta específica
    @GetMapping("/venta/{idVenta}")
    public String pagosPorVenta(@PathVariable Long idVenta, Model model) {
        try {
            List<PagoDTO> pagos = pagoService.obtenerPorVenta(idVenta);
            model.addAttribute("pagos", pagos);
            model.addAttribute("idVenta", idVenta);
            model.addAttribute("titulo", "Pagos de Venta #" + idVenta);
            return "pagos/por-venta";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/ventas/admin/historial";
        }
    }
    
    // Ver detalle de pago
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        try {
            PagoDTO pago = pagoService.obtenerPorId(id);
            model.addAttribute("pago", pago);
            model.addAttribute("titulo", "Detalle de Pago #" + id);
            return "pagos/detalle";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/pagos/admin/historial";
        }
    }
}