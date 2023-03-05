package ar.com.auth.services;

import ar.com.auth.dtos.requests.UpdateUserRequest;
import ar.com.auth.dtos.responses.DisableUserRespose;
import ar.com.auth.dtos.responses.EnableUserRespose;
import ar.com.auth.dtos.responses.FetchAllUsersResponse;
import ar.com.auth.dtos.responses.UpdateUserResponse;
import ar.com.auth.enums.Roles;
import ar.com.auth.model.Role;
import ar.com.auth.model.User;
import ar.com.auth.repositories.RoleRepository;
import ar.com.auth.repositories.UserRepository;
import ar.com.auth.services.implementations.UserServiceImplement;
import org.hibernate.FetchNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TestUserService {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private UserServiceImplement userService;

    private final String USER_NAME = "admin";
    private final String NON_EXISTS_USER_NAME = "non_exist_user";
    private final String USER_PASSWORD = "1234";
    private final String USER_ROLE = "ROLE_ADMIN_USER";

    private final Role roleMocked = Role.builder()
            .roleId("UUID")
            .roleDescription(USER_ROLE)
            .roleName(Roles.valueOf(USER_ROLE))
            .roleUsers(Collections.emptyList())
            .build();

    private final User userMocked = User.builder()
            .isEnabled(true)
            .userRoles(List.of(roleMocked))
            .userName(USER_NAME)
            .userPassword(USER_PASSWORD)
            .build();

    @BeforeEach
    public void init() {
        when(userRepository.findAll()).thenReturn(List.of(userMocked));
        when(userRepository.existsUserByUserName(USER_NAME)).thenReturn(Boolean.TRUE);
        when(userRepository.findUserByUserName(USER_NAME)).thenReturn(userMocked);
        when(userRepository.save(any(User.class))).thenReturn(userMocked);
        when(userRepository.findUserByUserNameAndIsEnabledTrue(USER_NAME)).thenReturn(Optional.of(userMocked));

        when(roleRepository.findRoleByRoleName(Roles.parse(USER_ROLE))).thenReturn(Optional.of(roleMocked));
    }

    @Test
    public void testFetchAllUsers() {
        FetchAllUsersResponse fetchAllUsersResponse = userService.fetchAllUsers();
        assertEquals(FetchAllUsersResponse.from(List.of(Optional.of(userMocked))), fetchAllUsersResponse);
    }

    @Test
    public void testUpdateUserAndUserExist() {
        UpdateUserRequest updateUserRequestMocked = UpdateUserRequest.builder().userName(USER_NAME).userRoles(List.of(USER_ROLE)).build();
        UpdateUserResponse updateUserResponse = userService.updateUser(updateUserRequestMocked);
        assertEquals(UpdateUserResponse.from(Optional.of(userMocked)), updateUserResponse);
    }

    @Test
    public void testUpdateUserAndUserNonExist() {
        UpdateUserRequest updateUserRequestMocked = UpdateUserRequest.builder().userName(NON_EXISTS_USER_NAME).userRoles(List.of(USER_ROLE)).build();
        UpdateUserResponse updateUserResponse = userService.updateUser(updateUserRequestMocked);
        assertEquals(UpdateUserResponse.from(Optional.empty()), updateUserResponse);
    }

    @Test
    public void testUpdateUserAndRoleNonExist() {
        String USER_ROLE_NON_EXISTENT = "ROLE_NON_EXISTENT";
        UpdateUserRequest updateUserRequestMocked = UpdateUserRequest.builder().userName(USER_NAME).userRoles(List.of(USER_ROLE_NON_EXISTENT)).build();
        FetchNotFoundException thrown = assertThrows(FetchNotFoundException.class, () ->
                userService.updateUser(updateUserRequestMocked)
        );
        assertEquals(MessageFormat.format("Entity `Role` with identifier value `role {0} not found` does not exist", USER_ROLE_NON_EXISTENT), thrown.getMessage());
    }

    @Test
    public void testDisableUserAndUserExist() {
        DisableUserRespose disableUserRespose = userService.disableUser(USER_NAME);
        assertEquals(DisableUserRespose.from(Optional.of(userMocked)), disableUserRespose);
    }

    @Test
    public void testDisableUserAndUserNonExist() {
        DisableUserRespose disableUserRespose = userService.disableUser(NON_EXISTS_USER_NAME);
        assertEquals(DisableUserRespose.from(Optional.empty()), disableUserRespose);
    }

    @Test
    public void testEnableUserAndUserExist() {
        EnableUserRespose enableUserRespose = userService.enableUser(USER_NAME);
        assertEquals(EnableUserRespose.from(Optional.of(userMocked)), enableUserRespose);
    }

    @Test
    public void testEnableUserAndUserNonExist() {
        EnableUserRespose enableUserRespose = userService.enableUser(NON_EXISTS_USER_NAME);
        assertEquals(EnableUserRespose.from(Optional.empty()), enableUserRespose);
    }

    @Test
    public void testLoadUserByUsernameAndUserExist() {
        UserDetails userDetails = userService.loadUserByUsername(USER_NAME);
        assertEquals(userMocked, userDetails);
    }

    @Test
    public void testLoadUserByUsernameAndUserNonExist() {
        UsernameNotFoundException thrown = assertThrows(UsernameNotFoundException.class, () ->
                userService.loadUserByUsername(NON_EXISTS_USER_NAME)
        );
        assertEquals(MessageFormat.format("username {0} not found", NON_EXISTS_USER_NAME), thrown.getMessage());

    }
}
