package gym.gym.Controllers;

import gym.gym.Models.SuperAdmin;
import gym.gym.Services.SuperAdminService;
import gym.gym.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/superadmin")
public class SuperAdminController {
    @Autowired
    private SuperAdminService superAdminService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody SuperAdmin superAdmin) {
        SuperAdmin saved = superAdminService.saveSuperAdmin(superAdmin);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        if (superAdminService.authenticate(username, password)) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("role", "SUPERADMIN");
            String token = jwtUtil.generateToken(username, claims);
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }
}
