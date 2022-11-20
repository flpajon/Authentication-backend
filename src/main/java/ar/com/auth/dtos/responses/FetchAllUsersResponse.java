package ar.com.auth.dtos.responses;

import ar.com.auth.dtos.UserDTO;
import ar.com.auth.enums.Status;
import ar.com.auth.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Optional;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FetchAllUsersResponse {
    private Status status;
    private List<UserDTO> usersList;

    public static FetchAllUsersResponse from(List<Optional<User>> users) {
        return builder()
                .usersList(users.stream().map(UserDTO::from).toList())
                .status(Status.STATUS_OK)
                .build();
    }
}
