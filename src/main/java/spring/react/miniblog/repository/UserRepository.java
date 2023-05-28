package spring.react.miniblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.react.miniblog.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User save(User users);
}
