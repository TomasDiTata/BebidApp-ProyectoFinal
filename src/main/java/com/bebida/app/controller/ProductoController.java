package com.bebida.app.controller;

import com.bebida.app.dto.ProductoDTO;
import com.bebida.app.dto.CrearProductoDTO;
import com.bebida.app.dto.ProductoStockDTO;
import com.bebida.app.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    // ========== VISTAS PARA CLIENTES ==========
    
    // Catálogo principal (página de inicio)
    @GetMapping
    public String listarProductos(Model model) {
        List<ProductoDTO> productos = productoService.obtenerDisponibles();
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Catálogo de Bebidas");
        return "productos/catalogo";
    }
    
    // Buscar productos por categoría
    @GetMapping("/categoria/{categoria}")
    public String buscarPorCategoria(@PathVariable String categoria, Model model) {
        List<ProductoDTO> productos = productoService.obtenerPorCategoria(categoria);
        model.addAttribute("productos", productos);
        model.addAttribute("categoriaActual", categoria);
        model.addAttribute("titulo", "Categoría: " + categoria);
        return "productos/catalogo";
    }
    
    // Buscar productos por nombre
    @GetMapping("/buscar")
    public String buscarProductos(@RequestParam String query, Model model) {
        List<ProductoDTO> productos = productoService.obtenerTodos();
        // Filtrar por nombre que contenga el query
        List<ProductoDTO> resultados = productos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(query.toLowerCase()))
                .toList();
        model.addAttribute("productos", resultados);
        model.addAttribute("busqueda", query);
        model.addAttribute("titulo", "Resultados para: " + query);
        return "productos/catalogo";
    }
    
    // Ver detalle de producto
    @GetMapping("/{id}")
    public String verDetalle(@PathVariable Long id, Model model) {
        try {
            ProductoDTO producto = productoService.obtenerPorId(id);
            model.addAttribute("producto", producto);
            return "productos/detalle";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/productos";
        }
    }
    
    // ========== PANEL ADMINISTRADOR ==========
    
    // Listar todos los productos (admin)
    @GetMapping("/admin")
    public String administrarProductos(Model model) {
        List<ProductoDTO> productos = productoService.obtenerTodos();
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "Administrar Productos");
        return "productos/admin";
    }
    
    // Formulario para crear producto nuevo
    @GetMapping("/admin/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("producto", new CrearProductoDTO());
        model.addAttribute("titulo", "Nuevo Producto");
        model.addAttribute("editMode", false);
        return "productos/formulario";
    }
    
    // Guardar producto nuevo
    @PostMapping("/admin/guardar")
    public String guardarProducto(@Valid @ModelAttribute("producto") CrearProductoDTO productoDTO,
                                  BindingResult result,
                                  RedirectAttributes redirectAttributes,
                                  Model model) { 
        
        // Si hay errores en el formulario (ej: precio negativo), se vuelve al formulario
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo Producto");
            model.addAttribute("editMode", false); 
            return "productos/formulario";
        }
        
        try {
            // Lógica de guardado
            ProductoDTO nuevoProducto = new ProductoDTO();
            nuevoProducto.setNombre(productoDTO.getNombre());
            nuevoProducto.setDescripcion(productoDTO.getDescripcion());
            nuevoProducto.setCategoria(productoDTO.getCategoria());
            nuevoProducto.setStock(productoDTO.getStock());
            nuevoProducto.setImagenUrl(productoDTO.getImagenUrl());
            nuevoProducto.setPrecioUnitario(productoDTO.getPrecioUnitario());
            
            productoService.crear(nuevoProducto);
            
            redirectAttributes.addFlashAttribute("mensaje", "Producto creado exitosamente");
            
            
            return "redirect:/productos"; 
            // --------------------------
            
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/productos";
        }
    }
    
    // Formulario para editar producto
    @GetMapping("/admin/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ProductoDTO producto = productoService.obtenerPorId(id);
            model.addAttribute("producto", producto);
            model.addAttribute("titulo", "Editar Producto");
            model.addAttribute("editMode", true);
            return "productos/formulario";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/productos/admin";
        }
    }
    
    // Actualizar producto
    @PostMapping("/admin/actualizar/{id}")
    public String actualizarProducto(@PathVariable Long id,
                                    @Valid @ModelAttribute("producto") ProductoDTO productoDTO,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "productos/formulario";
        }
        
        try {
            productoService.actualizar(id, productoDTO);
            redirectAttributes.addFlashAttribute("mensaje", "Producto actualizado exitosamente");
            return "redirect:/productos/admin";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/productos/admin/editar/" + id;
        }
    }
    
    // Actualizar solo stock
    @PostMapping("/admin/stock/{id}")
    public String actualizarStock(@PathVariable Long id,
                                  @Valid @ModelAttribute ProductoStockDTO stockDTO,
                                  RedirectAttributes redirectAttributes) {
        try {
            ProductoDTO producto = productoService.obtenerPorId(id);
            int diferencia = stockDTO.getStock() - producto.getStock();
            productoService.actualizarStock(id, diferencia);
            redirectAttributes.addFlashAttribute("mensaje", "Stock actualizado exitosamente");
            return "redirect:/productos/admin";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/productos/admin";
        }
    }
    
    // 1. PANTALLA DE CONFIRMACIÓN (GET)
    // Cuando hacen clic en el basurero, vienen aca
    @GetMapping("/admin/eliminar/{id}")
    public String confirmarEliminacion(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            ProductoDTO producto = productoService.obtenerPorId(id);
            model.addAttribute("producto", producto);
            return "productos/eliminar"; // Mostramos el HTML nuevo
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Error al cargar producto: " + e.getMessage());
            return "redirect:/productos/admin";
        }
    }

    // 2. EJECUTAR ELIMINACIÓN (POST)
    // Cuando hacen clic en "Sí, Eliminar" en la pantalla roja
    @PostMapping("/admin/eliminar")
    public String eliminarProductoDefinitivo(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            productoService.eliminar(id); 
            redirectAttributes.addFlashAttribute("mensaje", "Producto eliminado correctamente.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "No se pudo eliminar: " + e.getMessage());
        }
        return "redirect:/productos/admin";
    }
    
    @GetMapping("/ordenar/precio")
    public String ordenarPorPrecio(@RequestParam String orden, Model model) {
        List<ProductoDTO> productos = productoService.ordenarPorPrecio(orden);
        
        System.out.println("----- ORDEN: " + orden + " -----");
        productos.forEach(p -> System.out.println(p.getNombre() + " $" + p.getPrecioUnitario()));
        
        model.addAttribute("productos", productos);
        model.addAttribute("ordenActual", orden);
        model.addAttribute("titulo", "Productos ordenados por precio");
        return "productos/catalogo";
    }
}
