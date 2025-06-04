package com.autismo.neuroprevia.configuration;

import com.autismo.neuroprevia.service.DetallesUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final DetallesUsuarioService detallesUsuarioService;
    private final CustomAuthenticationSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String ADMIN ="ADMIN";
        String DOCTOR = "DOCTOR";
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers("/admin/**").hasRole(ADMIN);
                    auth.requestMatchers(
                            "/", "/login", "/logout",
                            "/images/**", "/css/**", "/js/**",
                            "/registrarse/**", "/registrarPaciente/**"
                            , "/especialista/**", "/actuator/**"
                    ).permitAll(); // Si hay alguna ruta que este dando problemas, pueden agregarla aca, para probar
                    auth.requestMatchers("/admin/home","/examenes/**", "/preguntas/**", "/respuesta/**").hasAnyRole(ADMIN, DOCTOR);
                            auth.anyRequest().authenticated();
                }
                
                )
                .formLogin(formLogin -> {
                    formLogin.loginPage("/login");
                    formLogin.successHandler(successHandler);
                    formLogin.failureUrl("/login?error");
                }).logout(logout -> {
                    logout.logoutUrl("/logout");
                    logout.logoutSuccessUrl("/");
                })
                .headers(headers ->
                        headers.addHeaderWriter(
                                new XFrameOptionsHeaderWriter(
                                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN
                                )
                        )
                );

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(detallesUsuarioService);
        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
