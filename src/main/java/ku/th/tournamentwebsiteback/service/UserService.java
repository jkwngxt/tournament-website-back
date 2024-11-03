package ku.th.tournamentwebsiteback.service;

import jakarta.persistence.EntityNotFoundException;
import ku.th.tournamentwebsiteback.entity.Users;
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
    @Autowired
    private DTOConvertor convertor;

    public List<UserProfileResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> convertor.idConvertToUserProfileResponse(user.getUserId(), null))
                .collect(Collectors.toList());
    }

    public UserProfileResponse getUserByUserId(Integer id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return modelMapper.map(user, UserProfileResponse.class);
    }

}
