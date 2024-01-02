package example.post.service;

import example.post.domain.User;
import example.post.dto.token.TokenDto;
import example.post.dto.user.request.SignInRequest;
import example.post.dto.user.request.SignUpRequest;
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
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
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
        User user = findById(Long.valueOf(authentication.getName()));
        return getStringResponseEntity(tokenDto, user);
    }

    private ResponseEntity<String> getStringResponseEntity(TokenDto tokenDto, User user) {
        user.updateToken(tokenDto.getRefreshToken());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", tokenDto.getGrantType() + " " + tokenDto.getAccessToken());
        httpHeaders.add("ReAuthorization", tokenDto.getGrantType() + " " + tokenDto.getRefreshToken());
        return ResponseEntity.ok().headers(httpHeaders).body("ok");
    }

    @Transactional
    public ResponseEntity<String> reissue(HttpServletRequest request) {
        String refreshToken = jwtTokenProvider.resolveRefreshToken(request);
        if (!jwtTokenProvider.validateToken(refreshToken)){
            throw new RuntimeException("Refresh Token이 유효하지 않습니다");
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        User user = findById(Long.valueOf(authentication.getName()));
        if (!user.getRefreshToken().equals(refreshToken)){
            throw new RuntimeException("Refresh Token이 일치하지 않습니다");
        }
        TokenDto tokens = jwtTokenProvider.generateToken(authentication);
        return getStringResponseEntity(tokens, user);
    }
    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다"));
    }
    public User findByName(String name){
        return userRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다"));
    }
}
