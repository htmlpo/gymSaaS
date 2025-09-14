package gym.gym.Controllers;


import gym.gym.Models.Admin;
import gym.gym.Models.Member;
import gym.gym.Services.AdminService;
import gym.gym.Security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;


    @Autowired
    private JwtUtil jwtUtil;

    private Long extractGymIdFromToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = jwtUtil.extractAllClaims(token);
            Object gymIdObj = claims.get("gymId");
            if (gymIdObj instanceof Integer) {
                return ((Integer) gymIdObj).longValue();
            } else if (gymIdObj instanceof Long) {
                return (Long) gymIdObj;
            } else if (gymIdObj instanceof String) {
                return Long.parseLong((String) gymIdObj);
            }
        }
        throw new RuntimeException("Impossible d'extraire le gymId du token JWT");
    }

    @GetMapping
    public List<Admin> getAllAdmins(HttpServletRequest request) {
        Long gymId = extractGymIdFromToken(request);
        return adminService.getAllAdminsByGymId(gymId);
    }

    @PostMapping
    public Admin saveAdmin(@RequestBody Admin admin) {

        return adminService.saveAdmin(admin);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id, HttpServletRequest request) {
        Long gymId = extractGymIdFromToken(request);
        if (adminService.getAdminByIdAndGymId(id, gymId).isPresent()) {
            adminService.deleteAdmin(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAdmin(@PathVariable Long id, HttpServletRequest request, @RequestBody Admin adminData) {
        Long gymId = extractGymIdFromToken(request);
        boolean updated = adminService.updateAdminById(id, gymId, adminData);
        if (updated) {
            return ResponseEntity.ok("Admin updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin not found.");
        }
    }

}
