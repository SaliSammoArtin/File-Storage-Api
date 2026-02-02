package se.sali.webbapplikation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.sali.webbapplikation.dto.LoginRequest;
import se.sali.webbapplikation.dto.LoginResponse;
import se.sali.webbapplikation.dto.RegisterRequest;
import se.sali.webbapplikation.dto.RegisterResponse;
import se.sali.webbapplikation.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {

        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
}
