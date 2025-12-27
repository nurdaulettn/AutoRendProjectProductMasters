package kz.nurdaulet.AutoRent.controller;

import jakarta.validation.Valid;
import kz.nurdaulet.AutoRent.dto.Request.LoginRequestDto;
import kz.nurdaulet.AutoRent.dto.Request.RegisterRequestDto;
import kz.nurdaulet.AutoRent.dto.Response.AuthResponseDto;
import kz.nurdaulet.AutoRent.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto request) {
        AuthResponseDto response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginRequestDto request) {
        return authService.login(request);
    }
    @PostMapping("/logout")
    public String logout() {
        return "Logged out successfully";
    }
}
