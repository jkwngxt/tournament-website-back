package ku.th.tournamentwebsiteback.service;

import ku.th.tournamentwebsiteback.exception.UnauthorizedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    public Integer getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Integer) {
            return (Integer) principal;
        } else {
            throw new UnauthorizedException("Invalid user principal.");
        }
    }
}
