package com.autismo.neuroprevia.configuration;

import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.service.UsuarioService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UsuarioService usuarioService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return correo -> usuarioService.findByCorreo(correo)
                .map(u -> User.builder()
                        .username(u.getCorreo())
                        .password(u.getPassword())
                        .roles(u.getRol())
                        .build()
                )
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        var p = new DaoAuthenticationProvider();
        p.setUserDetailsService(userDetailsService());
        p.setPasswordEncoder(passwordEncoder());
        return p;
    }

    private AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            // 1) Recupera el correo (username) del Authentication
            String correo = authentication.getName();

            // 2) Busca el Usuario completo en base de datos
            Usuario usuario = usuarioService.findByCorreo(correo)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            // 3) Guárdalo en sesión
            request.getSession().setAttribute("usuarioLogueado", usuario);

            // 4) Ahora redirige según rol
            var roles = authentication.getAuthorities().stream()
                    .map(a -> a.getAuthority()).toList();
            if (roles.contains("ROLE_ADMIN")) {
                response.sendRedirect("/admin/home");
            } else if (roles.contains("ROLE_DOCTOR")) {
                response.sendRedirect("/doctor/home");
            } else {
                response.sendRedirect("/paciente/home");
            }
        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authProvider())
                .csrf(csrf->csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/registrarse",
                                "/registrarPaciente",   // <— aquí
                                "/css/**",
                                "/js/**"
                        ).permitAll()
                        .requestMatchers("/paciente/**").hasRole("PACIENTE")
                        .requestMatchers("/doctor/**"   ).hasRole("DOCTOR")
                        .requestMatchers("/admin/**"    ).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("correo")
                        .passwordParameter("password")
                        .successHandler(successHandler())
                        .failureUrl("/login?error")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );
        return http.build();
    }
}
