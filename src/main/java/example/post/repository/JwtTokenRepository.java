package example.post.repository;

import example.post.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JwtTokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUserId(Long userId);
}
