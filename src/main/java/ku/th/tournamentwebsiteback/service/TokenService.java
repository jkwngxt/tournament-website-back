package ku.th.tournamentwebsiteback.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import ku.th.tournamentwebsiteback.constants.Roles;
import ku.th.tournamentwebsiteback.entity.User;
import ku.th.tournamentwebsiteback.exception.InvalidTokenException;
import ku.th.tournamentwebsiteback.exception.UserNotFoundException;
import ku.th.tournamentwebsiteback.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TokenService {

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.validity}")
    private long tokenValidity;

    private Key secretKey;

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Integer userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Error parsing JWT token", e);
            throw new InvalidTokenException("Invalid JWT token", e);
        }
    }

    public Integer extractUserId(String token) {
        return Integer.parseInt(extractClaims(token).getSubject());
    }

    public long getExpirationTime(String token) {
        return extractClaims(token).getExpiration().getTime();
    }

    public boolean isTokenValid(String token, Integer userId) {
        try {
            Claims claims = extractClaims(token);
            Integer extractedUserId = Integer.parseInt(claims.getSubject());
            return extractedUserId.equals(userId) && !claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            logger.error("Error validating token", e);
            throw new InvalidTokenException("Invalid JWT token", e);
        }
    }

    public List<SimpleGrantedAuthority> getAuthoritiesByUserId(Integer userId) {
        User user = getUserById(userId);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (user.getAdmin() != null) {
            authorities.add(new SimpleGrantedAuthority(Roles.ROLE_ADMIN));
        } else {
            authorities.add(new SimpleGrantedAuthority(Roles.ROLE_USER));
        }
        return authorities;
    }

    private User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }
}
