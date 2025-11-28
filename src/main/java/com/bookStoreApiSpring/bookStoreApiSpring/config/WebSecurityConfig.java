package com.bookStoreApiSpring.bookStoreApiSpring.config;
import com.bookStoreApiSpring.bookStoreApiSpring.security.jwt.JWTConfigurer;
import com.bookStoreApiSpring.bookStoreApiSpring.security.jwt.TokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

    @Value("${app.jwt.Secret}")
    private String jwtSecret;

    @Value("${app.jwt.validity-in-seconds}")
    private Long jwtValidityInSeconds;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JWTConfigurer jwtConfigurer = new JWTConfigurer(tokenProvider());

        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        a -> a
                                .requestMatchers("/api/admin/**") // por ej: /api/admin/books/1
                                .hasRole("ADMIN")
                                .anyRequest() // por ej. /api/books
                                .permitAll()
                )
                .sessionManagement(h -> h.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .apply(jwtConfigurer);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider(jwtSecret, jwtValidityInSeconds);
    }

}
