package ar.com.auth.services;

import ar.com.auth.dtos.requests.UpdateUserRequest;
import ar.com.auth.dtos.responses.DisableUserRespose;
import ar.com.auth.dtos.responses.EnableUserRespose;
import ar.com.auth.dtos.responses.FetchAllUsersResponse;
import ar.com.auth.dtos.responses.UpdateUserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    FetchAllUsersResponse fetchAllUsers();
    UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest);
    DisableUserRespose disableUser(String userName);
    EnableUserRespose enableUser(String userName);
}
