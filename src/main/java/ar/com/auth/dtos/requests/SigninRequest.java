package ar.com.auth.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SigninRequest {
    private String userName;
    private String userPassword;
}
