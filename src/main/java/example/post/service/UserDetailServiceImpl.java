package example.post.service;

import com.sun.jdi.LongValue;
import example.post.dto.token.TokenDto;
import example.post.repository.UserRepository;
import example.post.util.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(username);
        return userRepository.findById(Long.valueOf(username))
                .map(this::createUserDetail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
    private User createUserDetail(example.post.domain.User user){
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getAuthority());
        return new User(
                String.valueOf(user.getId()),
                passwordEncoder.encode(user.getPassword()),
                Collections.singleton(grantedAuthority)
        );
    }

}
