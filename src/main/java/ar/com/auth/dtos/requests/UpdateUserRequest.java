package ar.com.auth.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UpdateUserRequest {
    private String userName;
    private List<String> userRoles;
}
