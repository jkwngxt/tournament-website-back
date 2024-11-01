package ku.th.tournamentwebsiteback.service;

import ku.th.tournamentwebsiteback.entity.User;
import ku.th.tournamentwebsiteback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Value("${osu.client.id}")
    private String clientId;

    @Value("${osu.client.secret}")
    private String clientSecret;

    @Value("${osu.redirect.uri}")
    private String redirectUri;

    @Value("${osu.token.url}")
    private String tokenUrl;

    public String getAuthorizationUrl() {
        return "https://osu.ppy.sh/oauth/authorize?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code&scope=identify";
    }

    public Map<String, Object> processOAuthCallback(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientSecret);
        requestBody.put("code", code);
        requestBody.put("grant_type", "authorization_code");
        requestBody.put("redirect_uri", redirectUri);

        Map<String, Object> response = restTemplate.postForObject(tokenUrl, requestBody, Map.class);
        Map<String, Object> result = new HashMap<>();

        if (response != null && response.get("access_token") != null) {
            String accessToken = (String) response.get("access_token");

            // fetch data by using osu! API
            String apiUrl = "https://osu.ppy.sh/api/v2/me/osu";
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<Map> userDataResponse = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, Map.class);
            Map<String, Object> userData = userDataResponse.getBody();

            if (userData != null) {
                Integer userId = (Integer) userData.get("id");
                String username = (String) userData.get("username");
                String profileImageUrl = (String) userData.get("avatar_url");

                User user = userRepository.findById(userId).orElse(new User());
                user.setUserId(userId);
                user.setUsername(username);
                user.setProfileImageUrl(profileImageUrl);

                // Save or update user in the database
                userRepository.save(user);

                // Generate a new JWT token
                String jwtToken = tokenService.generateToken(userId);

                result.put("status", "Login successful");
                result.put("token", jwtToken);
                result.put("profileImageUrl", profileImageUrl);
            }
        } else {
            result.put("status", "Login failed");
        }
        return result;
    }
}
