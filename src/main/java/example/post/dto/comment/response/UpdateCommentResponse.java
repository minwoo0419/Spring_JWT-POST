package example.post.dto.comment.response;

import lombok.Data;

@Data
public class UpdateCommentResponse {
    private Long id;
    private Long userId;
    public UpdateCommentResponse(Long id, Long userId){
        this.id = id;
        this.userId = userId;
    }
}
