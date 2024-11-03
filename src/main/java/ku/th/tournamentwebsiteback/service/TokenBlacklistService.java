package ku.th.tournamentwebsiteback.service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.PostConstruct;
import ku.th.tournamentwebsiteback.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenBlacklistService {
    private Cache<String, Boolean> blacklist;

    @Value("${jwt.token.validity}")
    private long tokenValidity;

    @Autowired
    private TokenService tokenService;

    @PostConstruct
    public void init() {
        blacklist = Caffeine.newBuilder()
                .expireAfterWrite(tokenValidity + 1000 * 60, TimeUnit.MILLISECONDS)
                .build();
    }

    public void blacklistToken(String token) {
        try {
            if (token != null && tokenService.isTokenValid(token, tokenService.extractUserId(token))) {
                long remainingValidity = tokenService.getExpirationTime(token) - System.currentTimeMillis();
                if (remainingValidity > 0) {
                    blacklist = Caffeine.newBuilder()
                            .expireAfterWrite(remainingValidity, TimeUnit.MILLISECONDS)
                            .build();
                    blacklist.put(token, true);
                }
            }
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid token for blacklisting", e);
        }
    }

    public boolean isTokenBlacklisted(String token) {
        Boolean isBlacklisted = blacklist.getIfPresent(token);
        return isBlacklisted != null && isBlacklisted;
    }
}
