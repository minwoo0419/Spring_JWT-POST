package example.post.dto.user.request;

import lombok.Data;

@Data
public class SignUpRequest {
    private String name;
    private String password;

}
