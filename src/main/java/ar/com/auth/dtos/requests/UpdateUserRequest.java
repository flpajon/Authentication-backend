package ar.com.auth.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateUserRequest {
    private String userName;
    private List<String> userRoles;
}
