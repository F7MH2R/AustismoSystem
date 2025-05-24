package com.autismo.neuroprevia.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class UsuarioPrincipal implements UserDetails {
    private final Usuario usuario;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority authority;
        String DOCTOR = "DOCTOR";
        String ADMIN = "ADMIN";
        if(DOCTOR.equals(usuario.getRol())) {
            authority = new SimpleGrantedAuthority("ROLE_DOCTOR");
        } else if (ADMIN.equals(usuario.getRol())) {
            authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        } else {
            authority = new SimpleGrantedAuthority("ROLE_PACIENTE");
        }

        return List.of(authority);
    }

    @Override
    public String getPassword() {
        return this.usuario.getPassword();
    }

    @Override
    public String getUsername() {
        String NOMBRE_COMPLETO = "%s %s";
        return String.format(NOMBRE_COMPLETO, this.usuario.getNombre(), this.usuario.getApellido());
    }
}
