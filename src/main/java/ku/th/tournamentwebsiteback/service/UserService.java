package ku.th.tournamentwebsiteback.service;

import ku.th.tournamentwebsiteback.dto.UserProfileDTO;
import ku.th.tournamentwebsiteback.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private ModelMapper modelMapper;
    public UserProfileDTO toGeneralProfileDTO(User user) {
        return modelMapper.map(user, UserProfileDTO.class);
    }
}
