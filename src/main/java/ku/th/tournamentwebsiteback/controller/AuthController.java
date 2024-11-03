package ku.th.tournamentwebsiteback.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ku.th.tournamentwebsiteback.response.LoginResponse;
import ku.th.tournamentwebsiteback.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;


    @GetMapping("/login")
    public void login(HttpServletResponse response) {
        try {
            response.sendRedirect(authService.getAuthorizationUrl());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Redirection failed");
        }
    }

    @GetMapping("/callback")
    public LoginResponse callback(@RequestParam String code) {
        return authService.processOAuthCallback(code);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        authService.logout(token);
        return ResponseEntity.ok("Logout successful");
    }
}