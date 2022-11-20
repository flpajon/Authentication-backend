package ar.com.auth.dtos.responses;

import ar.com.auth.dtos.UserDTO;
import ar.com.auth.enums.Status;
import ar.com.auth.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.Optional;

@Builder
@Data
public class RefreshTokenResponse {
    private Status status;
    private UserDTO user;
    private String accessToken;
    private String refreshToken;

    public static RefreshTokenResponse from(Optional<User> user, String accessToken, String refreshToken) {
        return builder()
                .user(UserDTO.from(user))
                .status(user.isPresent() ? Status.STATUS_OK : Status.STATUS_NOT_ALLOWED)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
