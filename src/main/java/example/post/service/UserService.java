package example.post.service;

import example.post.domain.Token;
import example.post.domain.User;
import example.post.dto.token.TokenDto;
import example.post.dto.user.request.SignInRequest;
import example.post.dto.user.request.SignUpRequest;
import example.post.repository.JwtTokenRepository;
import example.post.repository.UserRepository;
import example.post.util.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenRepository jwtTokenRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    String HEADER_STRING = "Bearer ";
    @Transactional
    public User signUpUser(SignUpRequest signUpRequest){
        if (userRepository.existsByName(signUpRequest.getName())){
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        String encodedPassword = encoder.encode(signUpRequest.getPassword());
        return userRepository.save(new User(signUpRequest.getName(), encodedPassword));
    }
    @Transactional
    public ResponseEntity<String> signInUser(SignInRequest signInRequest){
        if (!userRepository.existsByName(signInRequest.getName())){
            throw new RuntimeException("존재하지 않는 회원입니다.");
        }
        String encodedPassword = findByName(signInRequest.getName()).getPassword();
        if (!encoder.matches(signInRequest.getPassword(), encodedPassword)){
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(findByName(signInRequest.getName()).getId(), encodedPassword);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = jwtTokenProvider.generateToken(authentication);
        Token token = new Token(findByName(signInRequest.getName()).getId(), tokenDto);
        jwtTokenRepository.save(token);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", HEADER_STRING + token.getAccessToken());
        httpHeaders.add("ReAuthorization", HEADER_STRING + token.getRefreshToken());
        return ResponseEntity.ok().headers(httpHeaders).body("ok");
    }
    @Transactional
    public ResponseEntity<String> reissue(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        if (!jwtTokenProvider.validateToken(refreshToken)){
            throw new RuntimeException("Refresh Token이 유효하지 않습니다");
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        Token token = findByUserId(Long.valueOf(authentication.getName()));
        if (!token.getRefreshToken().equals(refreshToken)){
            throw new RuntimeException("Refresh Token이 일치하지 않습니다");
        }
        TokenDto tokens = jwtTokenProvider.generateToken(authentication);
        Token newToken = token.updateToken(tokens);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", HEADER_STRING + token.getAccessToken());
        httpHeaders.add("ReAuthorization", HEADER_STRING + token.getRefreshToken());
        return ResponseEntity.ok().headers(httpHeaders).body("ok");
    }
    public Token findByUserId(Long userId){
        return jwtTokenRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다"));
    }
    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다"));
    }
    public User findByName(String name){
        return userRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다"));
    }
}
