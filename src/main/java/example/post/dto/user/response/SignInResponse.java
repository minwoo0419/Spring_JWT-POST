package example.post.dto.user.response;

import lombok.Data;

@Data
public class SignInResponse {
    private String name;
    public SignInResponse(String name){
        this.name = name;
    }
}
