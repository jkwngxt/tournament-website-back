package ku.th.tournamentwebsiteback.config;

import ku.th.tournamentwebsiteback.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authz) -> authz
//                        .requestMatchers("/login", "/callback").permitAll()
//
//                        .requestMatchers("/tournaments").permitAll()
//                        .requestMatchers("/tournaments/add").authenticated()
//                        .requestMatchers("/tournaments/update/**").authenticated()
//                        .requestMatchers("/tournaments/validate/**").authenticated()
//                        .requestMatchers("/tournaments/**").permitAll()
//
//                        //ยังไม่จัดการสิทธิ์
//                        .requestMatchers("/teams").permitAll()
//                        .requestMatchers("/teams/**").permitAll()
//                        .requestMatchers("/lobby/**").permitAll()
                                .anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

