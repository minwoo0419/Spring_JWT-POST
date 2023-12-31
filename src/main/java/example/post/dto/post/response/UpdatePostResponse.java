package example.post.dto.post.response;

import lombok.Data;

@Data
public class UpdatePostResponse {
    private Long id;
    private Long userId;
    public UpdatePostResponse(Long id, Long userId){

        this.id = id;
        this.userId = userId;
    }
}
