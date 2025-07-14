package gym.gym.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // désactive la protection CSRF (utile pour Postman)
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // autorise toutes les requêtes sans authentification
                )
                .httpBasic(Customizer.withDefaults()); // ou supprime ceci si tu ne veux même pas de basic auth

        return http.build();
    }
}