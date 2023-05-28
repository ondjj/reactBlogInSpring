package spring.react.miniblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.react.miniblog.domain.User;
import spring.react.miniblog.dto.LoginRequest;
import spring.react.miniblog.dto.LoginResponse;
import spring.react.miniblog.repository.UserRepository;
import spring.react.miniblog.service.UserService;

@RestController
public class LoginController {

    private final UserService userService;
    @Autowired private  UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public LoginController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/join")
    public String join(@RequestBody User users) {
        users.setPassword(bCryptPasswordEncoder.encode(users.getPassword()));
        users.setRoles("ROLE_USER");
        userRepository.save(users);
        return "회원가입 완료";
    }

    /**TODO: 2023-05-27, Sat, 23:53  -JEON
    *  TASK: 로그인 API 설정
     * 해야 할 일 : H2 DB 설정, 엔티티 관계 설정, JWT 설정, Spring Security 설정
    */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 로그인 처리 로직 구현
        String username = request.getUsername();
        String password = request.getPassword();

        boolean isUsernameExists = userService.isUsernameExists(username);
        boolean isPasswordMatch = userService.isPasswordMatch(username, password);

        if (isUsernameExists && isPasswordMatch){
            // 사용자 인증에 성공하면 액세스 토큰을 생성하고 응답에 포함시킵니다.
//            String accessToken = generateAccessToken(request.getUsername());

            // 로그인 성공 응답 데이터 생성
//            LoginResponse response = new LoginResponse(accessToken,"200 OK", username);

            // 로그인 성공 응답 반환
            return ResponseEntity.ok("OK");
        }else {
            LoginResponse response = new LoginResponse(null, "아이디 또는 비밀번호가 올바르지 않습니다.", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    private String generateAccessToken(String username) {
        // 액세스 토큰 생성 로직 구현
        // 예를 들어, JWT 생성 등의 방법으로 액세스 토큰을 생성할 수 있습니다.

        // 생성된 액세스 토큰 반환
        String accessToken = "sample_access_token";
        return accessToken;
    }
}
