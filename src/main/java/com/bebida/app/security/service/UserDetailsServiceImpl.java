package com.bebida.app.security.service;

import com.bebida.app.entity.Usuario;
import com.bebida.app.repository.UsuarioRepository;
import com.bebida.app.security.model.CustomUserDetails;
import jakarta.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String Email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(Email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + Email));

        return new CustomUserDetails(usuario);
    }
}
