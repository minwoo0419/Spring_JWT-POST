package example.post.dto.post.response;

import lombok.Data;

@Data
public class SavePostResponse {
    private Long id;
    private Long userId;
    public SavePostResponse(Long id, Long userId){

        this.id = id;
        this.userId = userId;
    }
}
