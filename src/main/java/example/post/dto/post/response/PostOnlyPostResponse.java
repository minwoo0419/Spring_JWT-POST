package example.post.dto.post.response;

import lombok.Data;

import java.util.Date;

@Data
public class PostOnlyPostResponse {
    private Long userId;
    private Long id;
    private String title;
    private String content;
    private Date createAt;

    public PostOnlyPostResponse(Long id, String title, String content, Date createAt, Long userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.userId = userId;
    }
}
