package ar.com.auth.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class SignupRequest {
    private String userName;
    private String userPassword;
    private List<String> userRoles;
}
