package example.post.dto.comment.request;

import lombok.Data;

@Data
public class SaveCommentRequest {
    private Long postId;
    private String content;
}
