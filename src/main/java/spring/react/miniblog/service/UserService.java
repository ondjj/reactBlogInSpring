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
        User user1 = new User("user1", "1111");
        User user2 = new User("user2", "2222");
        User user3 = new User("user3", "3333");
        User user4 = new User("user4", "4444");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
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
