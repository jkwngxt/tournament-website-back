package ku.th.tournamentwebsiteback.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ku.th.tournamentwebsiteback.exception.InvalidTokenException;
import ku.th.tournamentwebsiteback.exception.UserNotFoundException;
import ku.th.tournamentwebsiteback.service.TokenBlacklistService;
import ku.th.tournamentwebsiteback.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private TokenBlacklistService blacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            try {
                boolean isBlacklist = blacklistService.isTokenBlacklisted(token);
                if (isBlacklist) {
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"Token is blacklisted. Please re-login to obtain a new token.\"}");
                    return;
                }

                Integer userId = tokenService.extractUserId(token);
                if (userId != null && tokenService.isTokenValid(token, userId)) {
                    List<SimpleGrantedAuthority> authorities = tokenService.getAuthoritiesByUserId(userId);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userId, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (InvalidTokenException | UserNotFoundException e) {
                logger.error("Authentication error: ", e);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
