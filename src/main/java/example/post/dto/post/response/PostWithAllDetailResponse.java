package example.post.dto.post.response;

import example.post.dto.comment.response.CommentInPost;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostWithAllDetailResponse {
    private PostOnlyPostResponse post;
    private List<CommentInPost> comments = new ArrayList<>();
    public PostWithAllDetailResponse(PostOnlyPostResponse post, List<CommentInPost> comments){
        this.post = post;
        this.comments = comments;
    }
}
