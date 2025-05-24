package com.autismo.neuroprevia.configuration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException {

        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

        for (GrantedAuthority role : roles) {
            if (role.getAuthority().equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/home");
                return;
            } else if (role.getAuthority().equals("ROLE_DOCTOR")) {
                response.sendRedirect("/especialista/home");
                return;
            } else if (role.getAuthority().equals("ROLE_PACIENTE")) {
                response.sendRedirect("/paciente/home");
                return;
            }
        }
        response.sendRedirect("/login");
    }
}
