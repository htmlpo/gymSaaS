package gym.gym.Controllers;


import gym.gym.Models.Gym;
import gym.gym.Services.GymService;
import gym.gym.Security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/gym")
public class GymController {


    @Autowired
    private GymService gymService;

    @Autowired
    private JwtUtil jwtUtil;

    private boolean isSuperAdmin(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Claims claims = jwtUtil.extractAllClaims(token);
            Object role = claims.get("role");
            return role != null && role.toString().equals("SUPERADMIN");
        }
        return false;
    }


    @GetMapping
    public ResponseEntity<?> getAllGyms(HttpServletRequest request) {
        if (!isSuperAdmin(request)) {
            return ResponseEntity.status(403).body("Access denied: SUPERADMIN only");
        }
        return ResponseEntity.ok(gymService.getAllGyms());
    }


    @PostMapping
    public ResponseEntity<?> createGym(@RequestBody Gym gym, HttpServletRequest request) {
        if (!isSuperAdmin(request)) {
            return ResponseEntity.status(403).body("Access denied: SUPERADMIN only");
        }
        return ResponseEntity.ok(gymService.saveGym(gym));
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getGymsById(@PathVariable Long id, HttpServletRequest request) {
        if (!isSuperAdmin(request)) {
            return ResponseEntity.status(403).body("Access denied: SUPERADMIN only");
        }
        Optional<Gym> gym = gymService.getGymById(id);
        return gym.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGym(@PathVariable Long id, HttpServletRequest request) {
        if (!isSuperAdmin(request)) {
            return ResponseEntity.status(403).body("Access denied: SUPERADMIN only");
        }
        if (gymService.getGymById(id).isPresent()) {
            gymService.deleteGym(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint pour activer/désactiver un gym (isPaid)
    @PatchMapping("/{id}/isPaid")
    public ResponseEntity<?> setGymPaid(@PathVariable Long id, @RequestBody boolean isPaid, HttpServletRequest request) {
        if (!isSuperAdmin(request)) {
            return ResponseEntity.status(403).body("Access denied: SUPERADMIN only");
        }
        Optional<Gym> gymOpt = gymService.getGymById(id);
        if (gymOpt.isPresent()) {
            Gym gym = gymOpt.get();
            gym.setPaid(isPaid);
            gymService.saveGym(gym);
            return ResponseEntity.ok(gym);
        }
        return ResponseEntity.notFound().build();
    }



}
