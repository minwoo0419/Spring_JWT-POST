package example.post.dto.post.request;

import lombok.Data;

@Data
public class SavePostRequest {
    private String title;
    private String content;
}
