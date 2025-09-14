package gym.gym.Controllers;

import gym.gym.Models.Admin;
import gym.gym.Services.AdminService;
import gym.gym.Services.MemberService;
import gym.gym.Models.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import gym.gym.Security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtUtil jwtUtil;
    @PostMapping("/member/login")
    public ResponseEntity<?> memberLogin(@RequestBody Map<String, Object> loginRequest) {
        String email = (String) loginRequest.get("email");
        String password = (String) loginRequest.get("password");

        return memberService.authenticateMember(email, password)
                .map(member -> {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("memberId", member.getId());
                    claims.put("gymId", member.getGym().getId());
                    claims.put("role", "MEMBER");
                    String token = jwtUtil.generateToken(member.getEmail(), claims);

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("memberId", member.getId());
                    response.put("gymId", member.getGym().getId());
                    response.put("email", member.getEmail());
                    response.put("firstName", member.getFirstName());
                    response.put("lastName", member.getLastName());
                    response.put("phone", member.getPhone());
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Identifiants invalides")));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<?> Adminlogin(@RequestBody Map<String, Object> loginRequest) {
        String username = (String) loginRequest.get("username");
        String password = (String) loginRequest.get("password");

        return adminService.authenticateAdmin(username, password)
                .map(admin -> {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("adminId", admin.getId());
                    claims.put("gymId", admin.getGym().getId());
                    claims.put("role", "ADMIN");
                    String token = jwtUtil.generateToken(admin.getUsername(), claims);

                    Map<String, Object> response = new HashMap<>();
                    response.put("token", token);
                    response.put("adminId", admin.getId());
                    response.put("gymId", admin.getGym().getId());
                    response.put("username", admin.getUsername());
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "Identifiants invalides")));
    }
}
