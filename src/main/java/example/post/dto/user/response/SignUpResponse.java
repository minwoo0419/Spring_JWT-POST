package example.post.dto.user.response;

import lombok.Data;

@Data
public class SignUpResponse {
    private String name;
    public SignUpResponse(String name){
        this.name = name;
    }
}
