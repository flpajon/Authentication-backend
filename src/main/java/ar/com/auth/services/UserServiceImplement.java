package ar.com.auth.services;

import ar.com.auth.dtos.requests.SigninRequest;
import ar.com.auth.dtos.requests.SignupRequest;
import ar.com.auth.dtos.responses.FetchAllUsersResponse;
import ar.com.auth.dtos.responses.RefreshTokenResponse;
import ar.com.auth.dtos.responses.SigninResponse;
import ar.com.auth.dtos.responses.SignupResponse;
import ar.com.auth.enums.Roles;
import ar.com.auth.model.Role;
import ar.com.auth.model.User;
import ar.com.auth.repositories.RoleRepository;
import ar.com.auth.repositories.UserRepository;
import ar.com.auth.utils.TokenGenerator;
import org.hibernate.FetchNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImplement implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenGenerator tokenGenerator;

    @Override
    public SignupResponse signupUser(SignupRequest signUpRequest) {
        if (!userRepository.existsUserByUserName(signUpRequest.getUserName())) {
            List<Role> userRoles = signUpRequest.getUserRoles().stream().map(role -> roleRepository.findRoleByRoleName(Roles.valueOf(role))
                    .orElseThrow(() -> new FetchNotFoundException(
                            "Role",
                            MessageFormat.format("role {0} not found", role)
                    ))).collect(Collectors.toList());
            User user = new User(signUpRequest.getUserName(), passwordEncoder.encode(signUpRequest.getUserPassword()), userRoles, true);
            return SignupResponse.from(Optional.of(userRepository.save(user)));
        }
        return SignupResponse.from(Optional.empty());
    }

    @Override
    public SigninResponse signinUser(SigninRequest signinRequest) {
        Optional<User> user = userRepository.findUserByUserNameAndIsEnabledTrue(signinRequest.getUserName());
        if (user.isPresent()
                && user.get().getUsername().equals(signinRequest.getUserName())
                && passwordEncoder.matches(signinRequest.getUserPassword(), user.get().getPassword())){
            String accessToken = tokenGenerator.createAccessToken(user.get());
            String refreshToken = tokenGenerator.createRefreshToken(user.get());
            return SigninResponse.from(user, accessToken, refreshToken);
        }
        return SigninResponse.from(Optional.empty(), null, null);
    }

    @Override
    public FetchAllUsersResponse fetchAllUsers() {
        return FetchAllUsersResponse.from(userRepository.findAll().stream().map(Optional::of).toList());
    }

    @Override
    public RefreshTokenResponse refreshToken(User user) {
        String accessToken = tokenGenerator.createAccessToken(user);
        String refreshToken = tokenGenerator.createRefreshToken(user);
        return RefreshTokenResponse.from(Optional.of(user),accessToken, refreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findUserByUserNameAndIsEnabledTrue(userName)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("username {0} not found", userName)
                ));
    }
}
