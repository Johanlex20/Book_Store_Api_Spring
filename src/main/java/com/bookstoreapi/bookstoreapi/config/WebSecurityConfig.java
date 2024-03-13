package com.bookstoreapi.bookstoreapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.bookstoreapi.bookstoreapi.security.JWTConfigurer;
import com.bookstoreapi.bookstoreapi.security.TokenProvider;
@Configuration
public class WebSecurityConfig {

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
        return new TokenProvider("chLhMF9w3mwDutysbQxsX8x4CGwZef4mayTGSmbAG2BUsXbYFKvXrVfnPCa62PJxp9TuHxx4PQAS2yGUTBAPy3Dy53j8Uj2wb2AQ3nK8VLg7tUx9HCzHATEp", 2592000L);
    }

}
