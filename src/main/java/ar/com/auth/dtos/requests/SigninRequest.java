package ar.com.auth.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {
    private String userName;
    private String userPassword;
}
