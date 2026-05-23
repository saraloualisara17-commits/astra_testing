package com.wash.laundry_app.auth;


import com.wash.laundry_app.users.Role;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;



@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(c ->
                        c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(c -> c

                        // Orders endpoints
                        .requestMatchers(HttpMethod.POST, "/api/orders").hasAnyRole("ADMIN", "EMPLOYE")
                        .requestMatchers(HttpMethod.GET, "/api/orders/{id}").hasAnyRole("ADMIN", "EMPLOYE", "LIVREUR")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/{id}/status").hasAnyRole("ADMIN", "EMPLOYE")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/{id}/delivery-driver").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/orders/{id}/payments").hasAnyRole("ADMIN", "EMPLOYE", "LIVREUR")

                        // Clients endpoints
                        .requestMatchers(HttpMethod.GET, "/api/clients").hasAnyRole("ADMIN", "EMPLOYE")
                        .requestMatchers(HttpMethod.GET, "/api/clients/{id}").hasAnyRole("ADMIN", "EMPLOYE", "LIVREUR")
                        .requestMatchers("/api/clients/**").hasAnyRole("ADMIN", "EMPLOYE", "LIVREUR")

                        // Users endpoints
                        .requestMatchers("/api/users").hasRole("ADMIN")
                        .requestMatchers("/api/users/**").hasRole("ADMIN")

                        // Statistics
                        .requestMatchers("/api/statistics/**").hasAnyRole("ADMIN", "EMPLOYE")

                        // Catalog and images
                        .requestMatchers("/api/catalog/**").hasAnyRole("ADMIN", "EMPLOYE", "LIVREUR")
                        .requestMatchers("/api/images/**").hasAnyRole("ADMIN", "EMPLOYE", "LIVREUR")
                        .requestMatchers("/api/upload/**").hasAnyRole("ADMIN", "EMPLOYE", "LIVREUR")

                        // Admin-specific routes
                        .requestMatchers("/admin/commandes/{id}", "/api/admin/commandes/{id}").hasRole("ADMIN")
                        .requestMatchers("/admin/commandes/{id}/status", "/api/admin/commandes/{id}/status").hasRole("ADMIN")
                        .requestMatchers("/admin/client/{id}", "/api/admin/client/{id}").hasRole("ADMIN")
                        .requestMatchers("/admin/clients/{id}", "/api/admin/clients/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/admin/stats/**").hasRole("ADMIN")
                        .requestMatchers("/api/admin/catalog/**").hasRole("ADMIN")
                        .requestMatchers("/admin/create-user").hasRole("ADMIN")
                        .requestMatchers("/admin/update-user/**").hasRole("ADMIN")
                        .requestMatchers("/admin/active-users", "/api/admin/active-users").hasRole("ADMIN")
                        .requestMatchers("/admin/active-user/**").hasRole("ADMIN")
                        .requestMatchers("/admin/inactive-user/**").hasRole("ADMIN")
                        .requestMatchers("/admin/inactive-users", "/api/admin/inactive-users").hasRole("ADMIN")
                        .requestMatchers("/admin/change-user-password/**").hasRole("ADMIN")
                        .requestMatchers("/admin/clients", "/admin/clients/**").hasRole("ADMIN")
                        .requestMatchers("/admin/**", "/api/admin/**").hasRole("ADMIN")

                        // Livreur routes
                        .requestMatchers("/livreur/**", "/api/livreur/**").hasAnyRole("LIVREUR", "ADMIN")

                        // Employe routes
                        .requestMatchers("/employe/**", "/api/employe/**").hasAnyRole("EMPLOYE", "ADMIN")

                        // Commandes and notifications (authenticated)
                        .requestMatchers("/api/commandes/{id}/payments").authenticated()
                        .requestMatchers("/api/notifications/**").authenticated()
                        .requestMatchers("/api/commandes/{id}/receipt/**").authenticated()
                        .requestMatchers("/api/commandes/**").authenticated()
                        .requestMatchers("/api/payment-types").authenticated()

                        // Public endpoints
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/logout").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/refresh").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/ws/**").permitAll()
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/public/**").permitAll()

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c -> {
                    c.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    c.accessDeniedHandler((request, response, accessDeniedException) ->
                            response.setStatus(HttpStatus.FORBIDDEN.value()));
                });
        return http.build();
    }
}
