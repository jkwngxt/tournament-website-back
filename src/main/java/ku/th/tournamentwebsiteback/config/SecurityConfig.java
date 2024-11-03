package ku.th.tournamentwebsiteback.config;

import ku.th.tournamentwebsiteback.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PutMapping;

import java.io.IOException;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Public Endpoints
                        .requestMatchers("/login", "/callback").permitAll()
                        .requestMatchers("/tournaments", "/tournaments/latest", "/tournaments/current").permitAll()

                        // Admin-Specific Endpoints
                        .requestMatchers(HttpMethod.POST, "/tournaments").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/tournaments/update/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/qualifier-matches/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "staffs/user/**/tournament/**/validate").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "teams/**/validate").hasRole("ADMIN")

                        // Allow all authenticated requests on certain paths
                        .anyRequest().authenticated()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                handleAuthException(response, "Unauthorized", HttpServletResponse.SC_UNAUTHORIZED)
                        )
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                handleAuthException(response, "Forbidden", HttpServletResponse.SC_FORBIDDEN)
                        )
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private void handleAuthException(HttpServletResponse response, String error, int status) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);

        String detailedMessage;
        if (status == HttpServletResponse.SC_UNAUTHORIZED) {
            detailedMessage = "Access denied: Invalid or expired token. Please login again.";
        } else if (status == HttpServletResponse.SC_FORBIDDEN) {
            detailedMessage = "Access forbidden: You do not have permission to access this resource.";
        } else {
            detailedMessage = "An unexpected error occurred.";
        }

        response.getWriter().write("{\"error\": \"" + error + "\", \"message\": \"" + detailedMessage + "\"}");
    }
}
