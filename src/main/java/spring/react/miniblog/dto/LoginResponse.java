package spring.react.miniblog.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring.react.miniblog.domain.User;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginResponse {

    private String token;
    private String message;
    private String username;

    // 생성자, 게터/세터, toString 등의 메서드 생략

    public LoginResponse(String token, String message, String username) {
        this.token = token;
        this.message = message;
        this.username = username;
    }


    // ... 필요한 추가 필드 및 메서드 정의 ...
}
