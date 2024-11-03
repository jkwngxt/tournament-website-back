package ku.th.tournamentwebsiteback.service;

import ku.th.tournamentwebsiteback.entity.User;
import ku.th.tournamentwebsiteback.repository.UserRepository;
import ku.th.tournamentwebsiteback.response.LoginResponse;
import ku.th.tournamentwebsiteback.response.UserProfileResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private TokenBlacklistService blacklistService;

    @Autowired
    private ModelMapper modelMapper;

    public String getAuthorizationUrl() {
        return String.format("https://osu.ppy.sh/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code&scope=identify",
                clientId, redirectUri);
    }

    public LoginResponse processOAuthCallback(String code) {
        String accessToken = fetchAccessToken(code);
        if (accessToken == null) {
            return new LoginResponse("Login failed", null, null);
        }

        Map<String, Object> userData = fetchUserData(accessToken);
        if (userData != null) {
            return handleUserData(userData);
        } else {
            return new LoginResponse("Failed to fetch user data", null, null);
        }
    }

    private String fetchAccessToken(String code) {
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

        return (String) responseEntity.getBody().get("access_token");
    }

    private Map<String, Object> fetchUserData(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "https://osu.ppy.sh/api/v2/me/osu";
        HttpHeaders apiHeaders = new HttpHeaders();
        apiHeaders.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> apiEntity = new HttpEntity<>(apiHeaders);
        ResponseEntity<Map> userDataResponse = restTemplate.exchange(apiUrl, HttpMethod.GET, apiEntity, Map.class);
        return userDataResponse.getBody();
    }

    private LoginResponse handleUserData(Map<String, Object> userData) {
        Integer userId = (Integer) userData.get("id");
        User user = userRepository.findById(userId).orElse(new User());
        user.setUserId(userId);
        user.setUsername((String) userData.get("username"));
        user.setProfileImageUrl((String) userData.get("avatar_url"));

        Map<String, Object> statistics = (Map<String, Object>) userData.get("statistics");
        user.setRank(statistics != null ? (Integer) statistics.get("global_rank") : null);

        Map<String, Object> countryData = (Map<String, Object>) userData.get("country");
        user.setCountry(countryData != null && countryData.get("name") != null ? (String) countryData.get("name") : "Unknown");

        userRepository.save(user);

        // Create UserProfileResponse
        UserProfileResponse userProfile = modelMapper.map(user, UserProfileResponse.class);

        // Return LoginResponse with JWT token and user profile
        String jwtToken = tokenService.generateToken(userId);

        return new LoginResponse("Login successful", jwtToken, userProfile);
    }

    public void logout(String token) {
        blacklistService.blacklistToken(token);
    }
}
