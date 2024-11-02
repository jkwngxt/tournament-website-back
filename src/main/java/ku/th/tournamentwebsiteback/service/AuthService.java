package ku.th.tournamentwebsiteback.service;

import ku.th.tournamentwebsiteback.entity.User;
import ku.th.tournamentwebsiteback.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);
        requestBody.add("code", code);
        requestBody.add("grant_type", "authorization_code");
        requestBody.add("redirect_uri", redirectUri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(tokenUrl, requestEntity, Map.class);
        Map<String, Object> response = responseEntity.getBody();
        Map<String, Object> result = new HashMap<>();

        if (response != null && response.get("access_token") != null) {
            String accessToken = (String) response.get("access_token");
            String apiUrl = "https://osu.ppy.sh/api/v2/me/osu";
            HttpHeaders apiHeaders = new HttpHeaders();
            apiHeaders.set("Authorization", "Bearer " + accessToken);
            HttpEntity<String> apiEntity = new HttpEntity<>(apiHeaders);

            ResponseEntity<Map> userDataResponse = restTemplate.exchange(apiUrl, HttpMethod.GET, apiEntity, Map.class);
            Map<String, Object> userData = userDataResponse.getBody();

            if (userData != null) {
                Integer userId = (Integer) userData.get("id");
                String username = (String) userData.get("username");
                String profileImageUrl = (String) userData.get("avatar_url");
                Map<String, Object> statistics = (Map<String, Object>) userData.get("statistics");
                Integer globalRank = statistics != null ? (Integer) statistics.get("global_rank") : null;
                Map<String, Object> countryData = (Map<String, Object>) userData.get("country");
                String country = (countryData != null && countryData.get("name") != null)
                        ? (String) countryData.get("name")
                        : "Unknown";

                User user = userRepository.findById(userId).orElse(new User());
                user.setUserId(userId);
                user.setUsername(username);
                user.setProfileImageUrl(profileImageUrl);
                user.setRank(globalRank);
                user.setCountry(country);

                userRepository.save(user);
                String jwtToken = tokenService.generateToken(userId);

                result.put("status", "Login successful");
                result.put("token", jwtToken);
                result.put("profileImageUrl", profileImageUrl);
            } else {
                result.put("status", "Failed to fetch user data");
            }
        } else {
            result.put("status", "Login failed");
        }
        return result;
    }
}
