package ar.com.auth.dtos.responses;

import ar.com.auth.dtos.UserDTO;
import ar.com.auth.enums.Status;
import ar.com.auth.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DisableUserRespose {
    private Status status;
    private UserDTO user;

    public static DisableUserRespose from(Optional<User> user) {
        return builder()
                .user(UserDTO.from(user))
                .status(user.isPresent() ? Status.STATUS_OK : Status.STATUS_NOT_ALLOWED)
                .build();
    }
}
