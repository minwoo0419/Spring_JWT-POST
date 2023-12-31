package example.post.domain;

import example.post.dto.token.TokenDto;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class Token {
    @Id
    private Long userId;
    private String grantType;
    private String accessToken;
    private String refreshToken;
    public Token(Long userId, TokenDto tokenDto){
        this.userId = userId;
        this.grantType = tokenDto.getGrantType();
        this.accessToken = tokenDto.getAccessToken();
        this.refreshToken = tokenDto.getRefreshToken();
    }
    public Token updateToken(TokenDto tokenDto){
        this.accessToken = tokenDto.getAccessToken();
        this.refreshToken = tokenDto.getRefreshToken();
        return this;
    }
}
