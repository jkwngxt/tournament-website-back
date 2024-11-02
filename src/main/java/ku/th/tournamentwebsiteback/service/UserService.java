package ku.th.tournamentwebsiteback.service;

import ku.th.tournamentwebsiteback.repository.UserRepository;
import ku.th.tournamentwebsiteback.response.UserProfileResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    public List<UserProfileResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserProfileResponse.class))
                .collect(Collectors.toList());
    }

    public UserProfileResponse getUserByUserId(Integer id) {
        return modelMapper.map(userRepository.findById(id), UserProfileResponse.class);
    }
}
