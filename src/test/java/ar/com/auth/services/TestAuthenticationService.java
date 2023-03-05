package ar.com.auth.services;

import ar.com.auth.dtos.requests.SigninRequest;
import ar.com.auth.dtos.requests.SignupRequest;
import ar.com.auth.dtos.responses.RefreshTokenResponse;
import ar.com.auth.dtos.responses.SigninResponse;
import ar.com.auth.dtos.responses.SignupResponse;
import ar.com.auth.enums.Roles;
import ar.com.auth.model.Role;
import ar.com.auth.model.User;
import ar.com.auth.repositories.RoleRepository;
import ar.com.auth.repositories.UserRepository;
import ar.com.auth.services.implementations.AuthenticationServiceImplement;
import ar.com.auth.utils.TokenGenerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hibernate.FetchNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class TestAuthenticationService {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TokenGenerator tokenGenerator;
    @InjectMocks
    AuthenticationServiceImplement authenticationService;

    private final String USER_NAME = "admin";
    private final String NEW_USER_NAME = "new_user";
    private final String USER_PASSWORD = "1234";
    private final String USER_ROLE = "ROLE_ADMIN_USER";
    private final String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiJ9-1";
    private final String REFRESH_TOKEN = "eyJhbGciOiJSUzI1NiJ9-2";

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

    private final User newUserMocked = User.builder()
            .isEnabled(true)
            .userRoles(List.of(roleMocked))
            .userName(NEW_USER_NAME)
            .userPassword(USER_PASSWORD)
            .build();

    @BeforeEach
    public void init() {
        Mockito.when(userRepository.findUserByUserNameAndIsEnabledTrue(USER_NAME)).thenReturn(Optional.of(userMocked));
        Mockito.when(userRepository.existsUserByUserName(USER_NAME)).thenReturn(true);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(newUserMocked);

        Mockito.when(roleRepository.findRoleByRoleName(Roles.valueOf(USER_ROLE))).thenReturn(Optional.ofNullable(roleMocked));

        Mockito.when(passwordEncoder.matches(USER_PASSWORD, userMocked.getPassword())).thenReturn(true);
        Mockito.when(passwordEncoder.encode(USER_PASSWORD)).thenReturn(USER_PASSWORD);

        Mockito.when(tokenGenerator.createAccessToken(userMocked)).thenReturn(ACCESS_TOKEN);
        Mockito.when(tokenGenerator.createRefreshToken(userMocked)).thenReturn(REFRESH_TOKEN);
    }

    @Test
    public void testSignupUserWhenUserAlreadyExist() {
        SignupRequest signupRequestMocked = SignupRequest.builder().userName(USER_NAME).userPassword(USER_PASSWORD).userRoles(List.of(USER_ROLE)).build();
        SignupResponse signupResponse = authenticationService.signupUser(signupRequestMocked);
        assertEquals(SignupResponse.from(Optional.empty()), signupResponse);
    }

    @Test
    public void testSignupUserWhenUserIsNew() {
        SignupRequest signupRequestMocked = SignupRequest.builder().userName(NEW_USER_NAME).userPassword(USER_PASSWORD).userRoles(List.of(USER_ROLE)).build();
        SignupResponse signupResponse = authenticationService.signupUser(signupRequestMocked);
        assertEquals(SignupResponse.from(Optional.of(newUserMocked)), signupResponse);
    }

    @Test
    public void testSignupUserWhenRoleDoesntExist() {
        String USER_ROLE_NON_EXISTENT = "ROLE_NON_EXISTENT";
        SignupRequest signupRequestMocked = SignupRequest.builder().userName(NEW_USER_NAME).userPassword(USER_PASSWORD).userRoles(List.of(USER_ROLE, USER_ROLE_NON_EXISTENT)).build();
        FetchNotFoundException thrown = assertThrows(FetchNotFoundException.class, () ->
                authenticationService.signupUser(signupRequestMocked)
        );
        assertEquals(MessageFormat.format("Entity `Role` with identifier value `role {0} not found` does not exist", USER_ROLE_NON_EXISTENT), thrown.getMessage());
    }

    @Test
    public void testSigninUserWhenUserAndPasswordAreCorrects() {
        SigninRequest signinRequestMocked = SigninRequest.builder().userName(USER_NAME).userPassword(USER_PASSWORD).build();
        SigninResponse signinResponse = authenticationService.signinUser(signinRequestMocked);
        assertEquals(SigninResponse.from(Optional.of(userMocked), ACCESS_TOKEN, REFRESH_TOKEN), signinResponse);
    }

    @Test
    public void testSigninUserWhenUserAndPasswordAreIncorrects() {
        SigninRequest signinRequestMocked = SigninRequest.builder().userName("nimda").userPassword("4321").build();
        SigninResponse signinResponse = authenticationService.signinUser(signinRequestMocked);
        assertEquals(SigninResponse.from(Optional.empty(), null, null), signinResponse);
    }

    @Test
    public void testSigninUserWhenUserIsIncorrect() {
        SigninRequest signinRequestMocked = SigninRequest.builder().userName("nimda").userPassword(USER_PASSWORD).build();
        SigninResponse signinResponse = authenticationService.signinUser(signinRequestMocked);
        assertEquals(SigninResponse.from(Optional.empty(), null, null), signinResponse);
    }

    @Test
    public void testSigninUserWhenPasswordIsIncorrect() {
        SigninRequest signinRequestMocked = SigninRequest.builder().userName(USER_NAME).userPassword("4321").build();
        SigninResponse signinResponse = authenticationService.signinUser(signinRequestMocked);
        assertEquals(SigninResponse.from(Optional.empty(), null, null), signinResponse);
    }

    @Test
    public void testRefreshToken() {
        RefreshTokenResponse refreshTokenResponse = authenticationService.refreshToken(userMocked);
        assertEquals(RefreshTokenResponse.from(Optional.of(userMocked), ACCESS_TOKEN, REFRESH_TOKEN), refreshTokenResponse);
    }
}
