package ku.th.tournamentwebsiteback.service;

import ku.th.tournamentwebsiteback.entity.Users;
import ku.th.tournamentwebsiteback.repository.UserRepository;
import ku.th.tournamentwebsiteback.request.AuthRequest;
import ku.th.tournamentwebsiteback.response.LoginResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private TokenBlacklistService blacklistService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DTOConvertor convertor;

    public void logout(String token) {
        blacklistService.blacklistToken(token);
    }

    public LoginResponse signup(AuthRequest request) {
        Users user = modelMapper.map(request, Users.class);
        userRepository.save(user);
        String jwtToken = tokenService.generateToken(user.getUserId());
        return new LoginResponse(
                "Login successful",
                jwtToken,
                convertor.idConvertToUserProfileResponse(user.getUserId(), request.getTournamentId()));

    }
}
