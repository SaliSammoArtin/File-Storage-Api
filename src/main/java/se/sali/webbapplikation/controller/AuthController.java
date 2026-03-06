package se.sali.webbapplikation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sali.webbapplikation.dto.*;
import se.sali.webbapplikation.exception.UserAlreadyExistsException;
import se.sali.webbapplikation.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            RegisterResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(409).body(new ErrorResponse("Username already exists", 409));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("registration failed", 500);
            return ResponseEntity.status(500).body(error);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new ErrorResponse("Invalid credentials", 401));
        } catch (Exception e) {
            ErrorResponse error = new ErrorResponse("login failed", 500);
            return ResponseEntity.status(500).body(error);
        }
    }
}
