package com.example.bookdombackend.model.service.usuario;

import com.example.bookdombackend.exceptions.Usuario.UsuarioNotFoundException;
import com.example.bookdombackend.model.entity.Usuario;
import com.example.bookdombackend.model.service.usuario.IUsuarioService;
import com.example.bookdombackend.security.entity.CustomUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final IUsuarioService usuarioService;

    public CustomUserDetailsService(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    @Transactional
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.findByNombreUsuario(username);
        if (usuario == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new CustomUserDetails(usuario);
    }

    @Transactional
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.findById(userId);
        if (usuario == null) {
            throw new UsernameNotFoundException("User not found with id: " + userId);
        }
        return new CustomUserDetails(usuario);
    }
}
