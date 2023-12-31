package example.post.repository;

import example.post.domain.Comment;
import example.post.domain.Like;
import example.post.domain.Post;
import example.post.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findAllByPost(Post post);
}
