package example.post.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Date createAt;
    private Date updateAt;
    public Comment(Post post, String content, User user){
        this.post = post;
        this.content = content;
        this.createAt = new Date();
        this.updateAt = new Date();
        this.user = user;
    }
    public Comment updateComment(String content){
        this.content = content;
        this.updateAt = new Date();
        return this;
    }
}
