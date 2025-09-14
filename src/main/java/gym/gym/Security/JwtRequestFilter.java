package gym.gym.Security;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import gym.gym.Repositories.GymRepo;
import gym.gym.Models.Gym;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {


    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private GymRepo gymRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Token invalide, on laisse username à null
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.isTokenValid(jwt)) {
                Claims claims = jwtUtil.extractAllClaims(jwt);
                // Blocage automatique si isPaid == false (sauf superadmin)
                Object role = claims.get("role");
                if (role == null || !role.toString().equals("SUPERADMIN")) {
                    Object gymIdObj = claims.get("gymId");
                    if (gymIdObj != null) {
                        Long gymId;
                        if (gymIdObj instanceof Integer) {
                            gymId = ((Integer) gymIdObj).longValue();
                        } else if (gymIdObj instanceof Long) {
                            gymId = (Long) gymIdObj;
                        } else if (gymIdObj instanceof String) {
                            gymId = Long.parseLong((String) gymIdObj);
                        } else {
                            gymId = null;
                        }
                        if (gymId != null) {
                            Gym gym = gymRepo.findById(gymId).orElse(null);
                            if (gym != null && !gym.isPaid()) {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                response.getWriter().write("Gym access blocked: payment required");
                                return;
                            }
                        }
                    }
                }
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.emptyList());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        chain.doFilter(request, response);
    }
}
