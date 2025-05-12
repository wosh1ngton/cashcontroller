package br.com.cashcontroller.service;

import br.com.cashcontroller.model.Role;
import br.com.cashcontroller.model.User;
import br.com.cashcontroller.repository.UserRepository;
import br.com.cashcontroller.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = new User();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER); // Default role
        
        userRepository.save(user);
        
        var token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        
        var user = userRepository.findByUsername(request.username())
                .orElseThrow();
        
        var token = jwtService.generateToken(user);
        return new AuthenticationResponse(token);
    }

    // Request/Response classes
    public record AuthenticationRequest(String username, String password) {}
    public record RegisterRequest(String username, String password) {}
    public record AuthenticationResponse(String token) {}
}
