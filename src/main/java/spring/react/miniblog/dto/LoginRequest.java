package spring.react.miniblog.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String username;
    private String password;

    // 생성자, 게터/세터, toString 등의 메서드 생략

    // ... 필요한 유효성 검사 로직 등 추가 가능 ...
}
