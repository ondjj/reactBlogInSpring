package spring.react.miniblog.config.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.react.miniblog.config.auth.PrincipalDetails;
import spring.react.miniblog.config.jwt.JwtProperties;
import spring.react.miniblog.config.jwt.service.JwtService;
import spring.react.miniblog.dto.LoginRequest;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음
// /login 요청해서 username, password 전송하면 (post)
// UsernamePasswordAuthenticationFilter가 동작함
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService){
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    private final static SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final  static String secretKey = JwtProperties.SECRET + key;
    byte[] secretKeyBytes = secretKey.getBytes();

    // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
    // 인증 요청시에 실행되는 함수 => /login
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        System.out.println("JwtAuthenticationFilter : 진입 - 로그인 시도중");

        // 1. username, password 받아서
        // 2. 정상인지 로그인 시도를 하자. authenticationManager로 로그인 시도를 하면
        //      PrincipalDetailsService가 호출가 호출되고 loadUserByUsername() 함수 실행됨
        // 3. PrincipalDetail를 세션에 담음 ( 권한 관리를 위해서 )
        // 4. JWT을 만들어서 응답해주면 됨
        // request에 있는 username과 password를 파싱해서 자바 Object로 받기

        LoginRequest loginRequestDto = null;
        try {
/*
            원시적인 방법
            loginRequestDto username, password 담겨있음
            BufferedReader br = request.getReader();
            String input = null;
            while ((input = br.readLine()) != null){
                System.out.println(input);
            }*/
            // 1. ObjectMapper -> JSON 데이터 파싱해주는 객체
            ObjectMapper om = new ObjectMapper();
            // 2. username과 password 값을 받을 LoginRequestDTO를 생성해서 inputStream에서 LoginRequest.class 형태로 받음
            loginRequestDto = om.readValue(request.getInputStream(), LoginRequest.class);
            System.out.println("request.getInputStream().toString() = " + request.getInputStream().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("JwtAuthenticationFilter : "+loginRequestDto);

        // 유저네임패스워드 토큰 생성
        assert loginRequestDto != null;
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword());

        System.out.println("JwtAuthenticationFilter : 토큰생성완료");

        // authenticate() 함수가 호출 되면 인증 프로바이더가 유저 디테일 서비스의
        // loadUserByUsername(토큰의 첫번째 파라메터) 를 호출하고
        // UserDetails를 리턴받아서 토큰의 두번째 파라메터(credential)과
        // UserDetails(DB값)의 getPassword()함수로 비교해서 동일하면
        // Authentication 객체를 만들어서 필터체인으로 리턴해준다.

        // Tip: 인증 프로바이더의 디폴트 서비스는 UserDetailsService 타입
        // Tip: 인증 프로바이더의 디폴트 암호화 방식은 BCryptPasswordEncoder
        // 결론은 인증 프로바이더에게 알려줄 필요가 없음.
        Authentication authentication =
                authenticationManager.authenticate(authenticationToken);

        // authentication 객체가 session 영역에 저장됨
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("Authentication : "+principalDetails.getUser().getUsername());
        // authentication 객체가 session 영역에 저장을 해야하고 그 방법은 return 해주면 됨
        return authentication;
    }

    // JWT Token 생성해서 response에 담아주기
    // attemptAuthentication 실행 후 인증이 정상적으로 되었으면 실행되는 함수
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        System.out.println("successfulAuthentication 실행됨 : 인증이 완료되었다는 뜻 !");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        /**TODO: 2023-05-29, Mon, 21:1  -JEON
        *  TASK: 인증 완료 후 Access Token, Refresh Token 생성
        */
        // JWT 토큰 만들기 1 : Header 값 생성
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        // JWT 토큰 만들기 2 : claims 부분 설정 (토큰 안에 담을 내용)
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", principalDetails.getUser().getId());
        claims.put("username", principalDetails.getUser().getUsername());

        // JWT 토큰 만들기 3 : 만료 시간 설정(Access token) -> 1000 * 60L * 60L * 1 = 1시간, 500 * 60L * 60L * 1 = 30분
        Long expiredTime = 500 * 60L * 60L;
        Date date = new Date();
        date.setTime(date.getTime() + expiredTime);
        System.out.println("access_token 만료일자 : " + date);

        // JWT 토큰 만들기 4 : hmaSha 형식 key 만들기
        Key key = Keys.hmacShaKeyFor(secretKeyBytes);

        // JWT 토큰 Builder : access_token
        String access_token = Jwts.builder()
                .setHeader(headers)
                .setClaims(claims)
                .setSubject("access_token by spring")
                .setExpiration(date)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("access_token = " + access_token);

        // JWT 토큰 Builder : refresh token -> expiredTime을 24시간보다 약간 더 크게 설정
        expiredTime *= 23;
        expiredTime += 100000;
        date.setTime(System.currentTimeMillis() + expiredTime);

        String refresh_token = Jwts.builder()
                .setHeader(headers)
                .setSubject("refresh_token by Spring")
                .setExpiration(date)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("refresh_token = " + refresh_token);

        // refresh token 저장
        jwtService.setRefreshToken(principalDetails.getUser().getUsername(), refresh_token);

        // JWT 토큰 response header 에 담음 (주의 : Bearer 다음에 한 칸 띄우고 저장 해야한다.)
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + access_token);

        // access token 쿠키에도 저장
        Cookie cookie = new Cookie("access_token", access_token);
        // 쿠키는 항상 도메인 주소가 루트로 설정되어 있어야 모든 요청에서 사용 할 수 있다.
        cookie.setPath("/");
        cookie.setSecure(true);
        response.addCookie(cookie);


        /**TODO: 2023-05-29, Mon, 21:8  -JEON
        *  TASK: 단순 access token만 response 하던 과정 주석 처리
         *       테스트 후 잘 사용되면 삭제 예정
        */
//        // Hash 암호 방식
//        String jwtToken = JWT.create()
//                .withSubject(principalDetails.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis()+JwtProperties.EXPIRATION_TIME))
//                .withClaim("id", principalDetails.getUser().getId())
//                .withClaim("username", principalDetails.getUser().getUsername())
//                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
//
//        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX+jwtToken);
    }

}
