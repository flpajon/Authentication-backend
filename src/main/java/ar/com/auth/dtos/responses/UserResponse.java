package ar.com.auth.dtos.responses;

import ar.com.auth.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserResponse {
    private String userName;
    private List<String> userRoles;
    private Boolean isEnabled;

    public static UserResponse from(User user) {
        return builder()
                .userName(user.getUsername())
                .build();
    }
}
