package example.post.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    private Date createAt;
    private Date updateAt;
    private String authority;
    private String refreshToken;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Like> likes = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();
    public User(String name, String password) {
        this.name = name;
        this.password = password;
        this.createAt = new Date();
        this.updateAt = new Date();
        this.authority = "ROLE_USER";
    }
    public User updateUser(String name, String password){
        this.name = name;
        this.password = password;
        return this;
    }
    public User updateToken(String refreshToken){
        this.refreshToken = refreshToken;
        return this;
    }
}
