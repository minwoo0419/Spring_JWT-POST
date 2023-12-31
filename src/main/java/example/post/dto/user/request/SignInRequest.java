package example.post.dto.user.request;

import lombok.Data;

@Data
public class SignInRequest {
    private Long id;
    private String name;
    private String password;
}
