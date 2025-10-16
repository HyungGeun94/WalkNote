package be.stepnote.config.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private String role;
    private String nickname;
    private String username;
}
