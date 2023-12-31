package example.post.dto.post.request;

import lombok.Data;

@Data
public class UpdatePostRequest {
    private String title;
    private String content;
}
