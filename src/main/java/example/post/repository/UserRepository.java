package example.post.repository;

import example.post.domain.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByName(String name);
    @NonNull
    Optional<User> findById(@NonNull Long id);
    boolean existsByName(String name);
}
