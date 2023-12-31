package example.post.service;

import example.post.domain.Comment;
import example.post.domain.Post;
import example.post.domain.User;
import example.post.dto.comment.request.SaveCommentRequest;
import example.post.dto.comment.request.UpdateCommentRequest;
import example.post.dto.comment.response.SaveCommentResponse;
import example.post.repository.CommentRepository;
import example.post.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;
    @Transactional
    public Comment saveComment(SaveCommentRequest request){
        Post post = postService.findById(request.getPostId());
        User user = userService.findById(SecurityUtil.getCurrentUserId());
        return commentRepository.save(new Comment(post, request.getContent(), user));
    }
    public List<Comment> findAllCommentByPost(Post post){
        return commentRepository.findAllByPost(post);
    }
    @Transactional
    public Comment updateComment(Long id, UpdateCommentRequest request) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다."));
        return comment.updateComment(request.getContent());
    }
    public String deleteComment(Long id){
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않습니다."));
        commentRepository.delete(comment);
        return "ok";
    }
}
