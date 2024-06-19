package com.example.bookdombackend.controllers;

import com.example.bookdombackend.dto.usuario.LoginUsuario;
import com.example.bookdombackend.dto.usuario.RegisterUsuario;
import com.example.bookdombackend.dto.usuario.UsuarioDTO;
import com.example.bookdombackend.exceptions.Authorization.BadCredentialsException;
import com.example.bookdombackend.model.entity.Usuario;
import com.example.bookdombackend.model.service.authentication.AuthenticationService;
import com.example.bookdombackend.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Operaciones relacionadas con la autenticacion de permisos")
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUsuario loginRequest) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(loginRequest.getNombreUsuario());
            usuario.setPassword(loginRequest.getPassword());
            UsuarioDTO authenticatedUser = authenticationService.authenticate(usuario);

            String token = jwtTokenProvider.createToken(authenticatedUser.getNombreUsuario(), authenticatedUser.getRolUsuario().name());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", authenticatedUser);

            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUsuario registerRequest) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(registerRequest.getNombreUsuario());
            usuario.setEmail(registerRequest.getEmail());
            usuario.setPassword(registerRequest.getPassword());

            Usuario registeredUser = authenticationService.signup(usuario);

            String token = jwtTokenProvider.createToken(registeredUser.getNombreUsuario(), registeredUser.getRolUsuario().name());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("user", registeredUser);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration");
        }
    }
}

