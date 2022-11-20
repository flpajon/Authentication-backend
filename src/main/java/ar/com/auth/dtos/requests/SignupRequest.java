package ar.com.auth.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SignupRequest {
    private String userName;
    private String userPassword;
    private List<String> userRoles;
}
