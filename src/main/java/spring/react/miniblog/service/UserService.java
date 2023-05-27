package spring.react.miniblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring.react.miniblog.domain.User;
import spring.react.miniblog.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUsernameExists(String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

    public boolean isPasswordMatch(String username, String password) {
        User user = userRepository.findByUsername(username);
        return user != null && user.getPassword() != null && user.getPassword().equals(password);
    }
}
