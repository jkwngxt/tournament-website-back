package ku.th.tournamentwebsiteback.controller;

import ku.th.tournamentwebsiteback.response.UserProfileResponse;
import ku.th.tournamentwebsiteback.service.SecurityService;
import ku.th.tournamentwebsiteback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    SecurityService securityService;

    @GetMapping
    public List<UserProfileResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserProfileResponse getUserById(@PathVariable Integer id) {
        return userService.getUserByUserId(id);
    }

    @GetMapping("/me")
    public UserProfileResponse getCurrentUser() {
        Integer userId = securityService.getCurrentUserId();
        return userService.getUserByUserId(userId);
    }

}
