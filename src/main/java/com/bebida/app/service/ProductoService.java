package com.bebida.app.service;

import com.bebida.app.dto.ProductoDTO;
import com.bebida.app.entity.Producto;
import com.bebida.app.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ProductoDTO obtenerPorId(Long idProducto) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + idProducto));
        return convertirADTO(producto);
    }
    
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerDisponibles() {
        return productoRepository.findByStockGreaterThan(0).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public ProductoDTO crear(ProductoDTO productoDTO) {
        validarProducto(productoDTO);
        Producto producto = convertirAEntidad(productoDTO);
        Producto guardado = productoRepository.save(producto);
        return convertirADTO(guardado);
    }
    
    @Transactional
    public ProductoDTO actualizar(Long idProducto, ProductoDTO productoDTO) {
        Producto existente = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + idProducto));
        
        actualizarDatos(existente, productoDTO);
        Producto actualizado = productoRepository.save(existente);
        return convertirADTO(actualizado);
    }
    
    @Transactional
    public void actualizarStock(Long id, Integer cantidad) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        
        int nuevoStock = producto.getStock() + cantidad;
        if (nuevoStock < 0) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + producto.getStock());
        }
        
        producto.setStock(nuevoStock);
        productoRepository.save(producto);
    }
    
    @Transactional
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }
    
    private void validarProducto(ProductoDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (dto.getPrecioUnitario() == null){ 
            throw new RuntimeException("El precio debe ser mayor o igual a 0");
        }
        if (dto.getStock() == null || dto.getStock() < 0) {
            throw new RuntimeException("El stock debe ser mayor o igual a 0");
        }
    }
    
    private void actualizarDatos(Producto producto, ProductoDTO dto) {
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(dto.getCategoria());
        producto.setStock(dto.getStock());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setPrecioUnitario(dto.getPrecioUnitario());
    }
    
    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(producto.getIdProducto());
        dto.setNombre(producto.getNombre());
        dto.setDescripcion(producto.getDescripcion());
        dto.setCategoria(producto.getCategoria());
        dto.setStock(producto.getStock());
        dto.setImagenUrl(producto.getImagenUrl());
        dto.setPrecioUnitario(producto.getPrecioUnitario());
        return dto;
    }
    
    private Producto convertirAEntidad(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setCategoria(dto.getCategoria());
        producto.setStock(dto.getStock());
        producto.setImagenUrl(dto.getImagenUrl());
        producto.setPrecioUnitario(dto.getPrecioUnitario());
        return producto;
    }
    
    @Transactional(readOnly = true)
    public List<ProductoDTO> ordenarPorPrecio(String orden) {
        List<Producto> productos;

        if ("asc".equalsIgnoreCase(orden)) {
            productos = productoRepository.findAllByOrderByPrecioUnitarioAsc();
        } else {
            productos = productoRepository.findAllByOrderByPrecioUnitarioDesc();
        }

        return productos.stream()
                .map(this::convertirADTO)
                .toList();
    }
}
