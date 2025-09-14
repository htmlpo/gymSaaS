package gym.gym.Controllers;



import gym.gym.Models.Member;
import gym.gym.Services.MemberService;
import gym.gym.Security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberService memberService;


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
    public List<Member> getAllMembers(HttpServletRequest request) {
        Long gymId = extractGymIdFromToken(request);
        return memberService.getAllMembersByGymId(gymId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id, HttpServletRequest request) {
        Long gymId = extractGymIdFromToken(request);
        Optional<Member> member = memberService.getMemberByIdAndGymId(id, gymId);
        return member.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Member createMember(@RequestBody Member member) {
        return memberService.saveMember(member);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, HttpServletRequest request, @RequestBody Member updatedMember) {
        Long gymId = extractGymIdFromToken(request);
        return memberService.getMemberByIdAndGymId(id, gymId)
                .map(member -> {
                    member.setFirstName(updatedMember.getFirstName());
                    member.setLastName(updatedMember.getLastName());
                    member.setEmail(updatedMember.getEmail());
                    member.setPhone(updatedMember.getPhone());
                    member.setPassword(updatedMember.getPassword());
                    return ResponseEntity.ok(memberService.saveMember(member));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id, HttpServletRequest request) {
        Long gymId = extractGymIdFromToken(request);
        if (memberService.getMemberByIdAndGymId(id, gymId).isPresent()) {
            memberService.deleteMember(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}