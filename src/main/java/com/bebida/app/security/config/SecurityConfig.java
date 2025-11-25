package com.bebida.app.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            .authorizeHttpRequests(auth -> auth
                
                    //Cosas estaticas para el publico
                .requestMatchers("/css/**", "/js/**", "/images/**", "/style.css", "/static/**").permitAll()
                
               
                .requestMatchers("/", "/index", "/inicio", "/error").permitAll()
                .requestMatchers("/usuarios/login", "/usuarios/registro").permitAll()
                 
                //Seguridad por roles
                .requestMatchers("/usuarios/admin/**").hasRole("Administrador")
                    
                //Gestion de inventario
                .requestMatchers("/productos/admin/**").hasAnyRole("Administrador", "Vendedor")
                    
                //Catalogo de productos para que compren
                .requestMatchers("/productos/**").permitAll()
                    
                 //Cualquier otra cosa que requiere login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/usuarios/login") 
                .loginProcessingUrl("/login") 
                .usernameParameter("email")   
                .passwordParameter("contrasena") 
                .defaultSuccessUrl("/", true) 
                .permitAll() 
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/usuarios/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

