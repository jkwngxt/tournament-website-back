package ku.th.tournamentwebsiteback.controller;

import jakarta.servlet.http.HttpServletRequest;
import ku.th.tournamentwebsiteback.request.AuthRequest;
import ku.th.tournamentwebsiteback.response.LoginResponse;
import ku.th.tournamentwebsiteback.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest request) {
        LoginResponse response = authService.signup(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7);
        authService.logout(token);
        return ResponseEntity.ok("Logout successful");
    }
}
