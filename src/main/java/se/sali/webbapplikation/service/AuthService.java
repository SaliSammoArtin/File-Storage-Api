package se.sali.webbapplikation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.sali.webbapplikation.dto.LoginRequest;
import se.sali.webbapplikation.dto.LoginResponse;
import se.sali.webbapplikation.dto.RegisterRequest;
import se.sali.webbapplikation.dto.RegisterResponse;
import se.sali.webbapplikation.exeption.UserAlreadyExistsException;
import se.sali.webbapplikation.model.User;
import se.sali.webbapplikation.repository.IUserRepository;
import se.sali.webbapplikation.security.JWTService;

import java.util.Optional;


@Service
public class AuthService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    public RegisterResponse register(RegisterRequest request) {

        String username = request.getUsername().trim();
        userRepository.findByUsername(username);

        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(); }
        String hash = passwordEncoder.encode(request.getPassword());

        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(hash);

        User saved = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setId(saved.getId());
        response.setUsername(saved.getUsername());

        return response;
    }

    public LoginResponse login(LoginRequest request) {
        String username = request.getUsername().trim();

        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }
        User user = userOpt.get();
        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPasswordHash());

        if (!passwordMatches) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtService.generateToken(user.getId());

        LoginResponse response = new LoginResponse();
        response.setMessage("Login successful");
        response.setToken(token);

        return response;
    }
}
