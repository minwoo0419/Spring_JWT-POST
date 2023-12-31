package example.post.dto.comment.response;

import lombok.Data;

import java.util.Date;

@Data
public class CommentInPost {
    private Long id;
    private Long userId;
    private String content;
    private Date createAt;
    public CommentInPost(Long id, String content, Date createAt, Long userId){
        this.id = id;
        this.content = content;
        this.createAt = createAt;
        this.userId = userId;
    }
}
