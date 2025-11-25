package com.bebida.app.service;

import com.bebida.app.dto.UsuarioDTO;
import com.bebida.app.entity.Rol;
import com.bebida.app.entity.Tipo_usuario_enum;
import com.bebida.app.entity.Usuario;
import com.bebida.app.entity.UsuarioRol;
import com.bebida.app.repository.RolRepository;
import com.bebida.app.repository.UsuarioRepository;
import com.bebida.app.repository.UsuarioRolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private UsuarioRolRepository usuarioRolRepository;
    
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorDni(String Dni) {
        Usuario usuario = usuarioRepository.findByDni(Dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con dni: " + Dni));
        return convertirADTO(usuario);
    }
    
    @Transactional(readOnly = true)
    public UsuarioDTO obtenerPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
        return convertirADTO(usuario);
    }
    
    @Transactional
    public UsuarioDTO crear(UsuarioDTO usuarioDTO) {
        validarUsuario(usuarioDTO);
        Usuario usuario = convertirAEntidad(usuarioDTO);
        Usuario guardado = usuarioRepository.save(usuario);
        return convertirADTO(guardado);
    }
    
    @Transactional
    public UsuarioDTO actualizar(String Dni, UsuarioDTO usuarioDTO) {
        Usuario existente = usuarioRepository.findByDni(Dni)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con dni: " + Dni));
        
        actualizarDatos(existente, usuarioDTO);
        Usuario actualizado = usuarioRepository.save(existente);
        return convertirADTO(actualizado);
    }
    
    @Transactional
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }
    
    private void validarUsuario(UsuarioDTO dto) {
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new RuntimeException("El email es obligatorio");
        }
        if (dto.getDni() == null || dto.getDni().isEmpty()) {
            throw new RuntimeException("El DNI es obligatorio");
        }
    }
    
    private void actualizarDatos(Usuario usuario, UsuarioDTO dto) {
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setTelefono(dto.getTelefono());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
    }
    
    private UsuarioDTO convertirADTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setDni(usuario.getDni());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setTotalCompras(0);
        dto.setTotalGastado(0.0);
        
        if (usuario.getUsuarioRoles() != null && !usuario.getUsuarioRoles().isEmpty()) {
            dto.setRol(usuario.getUsuarioRoles().get(0).getRol().getRol().toString()); // Obtenemos el nombre del rol (Ej: "ADMIN")
        } else {
            dto.setRol("SIN ROL");
        }
        return dto;
    }
    
    private Usuario convertirAEntidad(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setDni(dto.getDni());
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setContrasena(dto.getContrasena());
        usuario.setTelefono(dto.getTelefono());
        usuario.setFechaNacimiento(dto.getFechaNacimiento());
        return usuario;
    }
    
    @Transactional
    public void cambiarRol(Long idUsuario, String nombreRol) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        //Transformamos el String ("VENDEDOR") al Enum (Tipo_usuario_enum.VENDEDOR)
        Tipo_usuario_enum rolEnum;
        try {
            rolEnum = Tipo_usuario_enum.valueOf(nombreRol); // Esto busca la constante exacta
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rol no válido: " + nombreRol);
        }

        //  Ahora buscamos usando el Enum
        Rol rolNuevo = rolRepository.findByRol(rolEnum)
                 .orElseThrow(() -> new RuntimeException("Rol no encontrado en BD: " + nombreRol));

        // 3. ASIGNACIÓN (Borrar viejos y poner nuevo)
        List<UsuarioRol> rolesActuales = usuarioRolRepository.findByUsuario(usuario);
        usuarioRolRepository.deleteAll(rolesActuales);

        UsuarioRol nuevoUsuarioRol = new UsuarioRol();
        nuevoUsuarioRol.setUsuario(usuario);
        nuevoUsuarioRol.setRol(rolNuevo);
        usuarioRolRepository.save(nuevoUsuarioRol);
    }
}

