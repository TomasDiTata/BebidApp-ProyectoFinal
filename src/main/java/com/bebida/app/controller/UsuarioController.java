package com.bebida.app.controller;

import com.bebida.app.dto.UsuarioDTO;
import com.bebida.app.dto.UsuarioRegistroDTO;
import com.bebida.app.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired 
    private PasswordEncoder passwordEncoder; //es un encriptador
    
    // Listar todos los usuarios (admin)
    @GetMapping("/admin")
    public String listarUsuarios(Model model) {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("titulo", "Administrar Usuarios");
        return "usuarios/admin";
    }
    
    //Cambiar roles (admin)
    @PostMapping("/admin/rol")
    public String cambiarRolUsuario(@RequestParam Long idUsuario,
                                     @RequestParam String nuevoRol,
                                      RedirectAttributes redirectAttributes){
        try{
            usuarioService.cambiarRol(idUsuario, nuevoRol);
            redirectAttributes.addFlashAttribute("mensaje", "Rol actualizado correctamente");
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("error", "Error al cambiar rol: " + e.getMessage());
        }
        return "redirect:/usuarios/admin";
    }
    
    // Ver perfil de usuario
    @GetMapping("/perfil/{id}")
    public String verPerfil(@PathVariable String dni, Model model) {
        try {
            UsuarioDTO usuario = usuarioService.obtenerPorDni(dni);
            model.addAttribute("usuario", usuario);
            return "usuarios/perfil";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/";
        }
    }
    
    // Formulario de registro
    @GetMapping("/registro")
    public String formularioRegistro(Model model) {
        model.addAttribute("usuario", new UsuarioRegistroDTO());
        return "usuarios/registro";
    }
    
    // Procesar registro
    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") UsuarioRegistroDTO registroDTO,
                                   BindingResult result,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "usuarios/registro";
        }
        
        try {
            // Convertir UsuarioRegistroDTO a UsuarioDTO
            UsuarioDTO nuevoUsuario = new UsuarioDTO();
            nuevoUsuario.setDni(registroDTO.getDni());
            nuevoUsuario.setNombre(registroDTO.getNombre());
            nuevoUsuario.setApellido(registroDTO.getApellido());
            nuevoUsuario.setEmail(registroDTO.getEmail());
            nuevoUsuario.setTelefono(registroDTO.getTelefono());
            nuevoUsuario.setFechaNacimiento(registroDTO.getFechaNacimiento());
            
            //Tomamos contraseña del formulario, encriptamos y guardamos
            String passEncriptada = passwordEncoder.encode(registroDTO.getContrasena());
            nuevoUsuario.setContrasena(passEncriptada);
            
            usuarioService.crear(nuevoUsuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario registrado exitosamente");
            return "redirect:/usuarios/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios/registro";
        }
    }
    
    // Formulario de login (placeholder - implementar con Spring Security)
    @GetMapping("/login")
    public String formularioLogin() {
        return "usuarios/login";
    }
    
    // Editar perfil
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable String dni, Model model) {
        try {
            UsuarioDTO usuario = usuarioService.obtenerPorDni(dni);
            model.addAttribute("usuario", usuario);
            return "usuarios/editar";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/usuarios/perfil/" + dni;
        }
    }
    
    // Actualizar perfil
    @PostMapping("/actualizar/{id}")
    public String actualizarUsuario(@PathVariable String dni,
                                   @ModelAttribute UsuarioDTO usuarioDTO,
                                   RedirectAttributes redirectAttributes) {
        try {
            usuarioService.actualizar(dni, usuarioDTO);
            redirectAttributes.addFlashAttribute("mensaje", "Perfil actualizado exitosamente");
            return "redirect:/usuarios/perfil/" + dni;
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios/editar/" + dni;
        }
    }
    
    // Eliminar usuario (admin)
    @GetMapping("/admin/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios/admin";
    }
}
