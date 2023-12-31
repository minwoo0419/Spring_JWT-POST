package example.post.controller;

import example.post.domain.Comment;
import example.post.dto.comment.request.SaveCommentRequest;
import example.post.dto.comment.request.UpdateCommentRequest;
import example.post.dto.comment.response.SaveCommentResponse;
import example.post.dto.comment.response.UpdateCommentResponse;
import example.post.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/comment")
    public SaveCommentResponse saveComment(@RequestBody SaveCommentRequest request){
        Comment comment = commentService.saveComment(request);
        return new SaveCommentResponse(comment.getId(), comment.getUser().getId());
    }
    @PatchMapping("/comment/{commentId}")
    public UpdateCommentResponse updateComment(@PathVariable Long commentId, @RequestBody UpdateCommentRequest request){
        Comment comment = commentService.updateComment(commentId, request);
        return new UpdateCommentResponse(comment.getId(), comment.getUser().getId());
    }
    @DeleteMapping("/comment/{commentId}")
    public String deleteComment(@PathVariable Long commentId){
        return commentService.deleteComment(commentId);
    }
}
