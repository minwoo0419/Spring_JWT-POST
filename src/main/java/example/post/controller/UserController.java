package example.post.controller;

import example.post.domain.User;
import example.post.dto.token.TokenDto;
import example.post.dto.user.request.SignInRequest;
import example.post.dto.user.request.SignUpRequest;
import example.post.dto.user.response.SignInResponse;
import example.post.dto.user.response.SignUpResponse;
import example.post.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    @PostMapping("/signup")
    public SignUpResponse signUp(@RequestBody SignUpRequest signUpRequest){
        User user = userService.signUpUser(signUpRequest);
        return new SignUpResponse(user.getName());
    }
    @PostMapping("/login")
    public TokenDto signIn(@RequestBody SignInRequest signInRequest){
        log.info(signInRequest.getName());
        return userService.signInUser(signInRequest);
    }
    @PostMapping("/reissue")
    public TokenDto reissue(){
        return userService.reissue();
    }
}
