package ar.com.auth.dtos;

import ar.com.auth.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private String userName;
    private List<String> userRoles;
    private Boolean isEnabled;

    public static UserDTO from(Optional<User> user) {
        return builder()
                .userName(user.map(User::getUsername).orElse(null))
                .userRoles(user.map(value -> value.getUserRoles().stream().map(role -> role.getRoleName().name()).toList()).orElse(null))
                .isEnabled(user.map(User::getIsEnabled).orElse(null))
                .build();
    }
}
