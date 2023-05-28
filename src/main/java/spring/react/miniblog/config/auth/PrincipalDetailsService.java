package spring.react.miniblog.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring.react.miniblog.domain.User;
import spring.react.miniblog.repository.UserRepository;

// http://localhost:8080/login => 여기서 동작을 안한다. 필터 생성 필요 JwtAuthenticationFilter
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService :loadUserByUsername 진입");
        User user = userRepository.findByUsername(username);

        // session.setAttribute("loginUser", user);
        return new PrincipalDetails(user);
    }
}
