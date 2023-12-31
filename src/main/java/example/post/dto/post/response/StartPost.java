package example.post.dto.post.response;

import lombok.Data;

import java.util.List;

@Data
public class StartPost {
    private Long count;
    private List<PostOnlyPostResponse> posts;
    public StartPost(Long count, List<PostOnlyPostResponse> posts){
        this.count = count;
        this.posts = posts;
    }
}
