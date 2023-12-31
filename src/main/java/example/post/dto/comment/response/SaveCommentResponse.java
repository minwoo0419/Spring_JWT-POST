package example.post.dto.comment.response;

import lombok.Data;

@Data
public class SaveCommentResponse {
    private Long id;
    private Long userId;

    public SaveCommentResponse(Long id, Long userId){

        this.id = id;
        this.userId = userId;
    }
}
