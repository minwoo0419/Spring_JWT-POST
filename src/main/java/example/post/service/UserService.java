package example.post.service;

import example.post.domain.Token;
import example.post.domain.User;
import example.post.dto.token.TokenDto;
import example.post.dto.user.request.SignInRequest;
import example.post.dto.user.request.SignUpRequest;
import example.post.dto.user.response.SignUpResponse;
import example.post.repository.JwtTokenRepository;
import example.post.repository.UserRepository;
import example.post.util.JwtTokenProvider;
import example.post.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenRepository jwtTokenRepository;
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
    public TokenDto signInUser(SignInRequest signInRequest){
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
        return tokenDto;
    }
    @Transactional
    public TokenDto reissue() {
        Token tokenDto = findByUserId(SecurityUtil.getCurrentUserId());
        if (!jwtTokenProvider.validateToken(tokenDto.getRefreshToken())){
            throw new RuntimeException("Refresh Token이 유효하지 않습니다");
        }
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken());
        Token refreshToken = findByUserId(Long.valueOf(authentication.getName()));
        if (!refreshToken.getRefreshToken().equals(tokenDto.getRefreshToken())){
            throw new RuntimeException("토큰이 일치하지 않습니다");
        }
        TokenDto token = jwtTokenProvider.generateToken(authentication);
        Token newToken = refreshToken.updateToken(token);
        jwtTokenRepository.save(newToken);
        return token;
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
