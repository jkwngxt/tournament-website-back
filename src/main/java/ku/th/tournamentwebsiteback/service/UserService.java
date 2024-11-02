package ku.th.tournamentwebsiteback.service;

import ku.th.tournamentwebsiteback.entity.User;
import ku.th.tournamentwebsiteback.response.UserProfileResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private ModelMapper modelMapper;

    public UserProfileResponse toGeneralProfileDTO(User user) {
        return modelMapper.map(user, UserProfileResponse.class);
    }
}
