package com.example.bookdombackend.model.service.authentication;

import com.example.bookdombackend.dto.usuario.UsuarioDTO;
import com.example.bookdombackend.exceptions.Authorization.BadCredentialsException;
import com.example.bookdombackend.model.entity.Rol;
import com.example.bookdombackend.model.entity.Usuario;
import com.example.bookdombackend.model.service.usuario.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class AuthenticationService implements IAuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Usuario signup(Usuario usuario) {
        logger.info("Iniciando proceso de registro para el usuario: {}", usuario.getNombreUsuario());

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        logger.debug("Contraseña codificada para el usuario: {}", usuario.getNombreUsuario());

        usuario.setRolUsuario(Rol.USUARIO);

        logger.debug("Rol asignado para el usuario: {}", usuario.getNombreUsuario());

        try {
            Usuario nuevoUsuario = usuarioService.addUsuario(usuario);
            logger.info("Usuario registrado exitosamente: {}", nuevoUsuario.getNombreUsuario());
            return nuevoUsuario;
        } catch (Exception e) {
            logger.error("Error al registrar el usuario: {}", usuario.getNombreUsuario(), e);
            throw e;
        }
    }

    @Override
    public UsuarioDTO authenticate(Usuario usuario) {
        if (usuario.getNombreUsuario() == null || usuario.getPassword() == null) {
            throw new IllegalArgumentException("Username and password must not be null");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            usuario.getNombreUsuario(),
                            usuario.getPassword()
                    )
            );
            if (!authentication.isAuthenticated()) {
                logger.error("Authentication failed for user '{}'", usuario.getNombreUsuario());
                throw new BadCredentialsException("Invalid username or password");
            }
            logger.info("User '{}' authenticated successfully", usuario.getNombreUsuario());
        } catch (AuthenticationException e) {
            logger.error("Authentication failed for user '{}'", usuario.getNombreUsuario(), e);
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsuarioDTO(usuarioService.findByNombreUsuario(usuario.getNombreUsuario()));
    }
}


