package com.autismo.neuroprevia.service;

import com.autismo.neuroprevia.model.Usuario;
import com.autismo.neuroprevia.model.UsuarioPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DetallesUsuarioService implements UserDetailsService {
    private final UsuarioService service;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Usuario> usuario = service.obtenerPorEmail(username);
        if (usuario.isEmpty()) {
            throw new UsernameNotFoundException("El usuario no es valido");
        }

        return new UsuarioPrincipal(usuario.get());
    }
}
