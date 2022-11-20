package ar.com.auth.services.implementations;

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
import ar.com.auth.services.UserService;
import org.hibernate.FetchNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImplement implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public FetchAllUsersResponse fetchAllUsers() {
        return FetchAllUsersResponse.from(userRepository.findAll().stream().map(Optional::of).toList());
    }

    @Override
    public UpdateUserResponse updateUser(UpdateUserRequest updateUserRequest) {
        if (userRepository.existsUserByUserName(updateUserRequest.getUserName())) {
            List<Role> userRoles = updateUserRequest.getUserRoles().stream().map(role -> roleRepository.findRoleByRoleName(Roles.valueOf(role))
                    .orElseThrow(() -> new FetchNotFoundException(
                            "Role",
                            MessageFormat.format("role {0} not found", role)
                    ))).toList();
            User currentUser = userRepository.findUserByUserName(updateUserRequest.getUserName());
            User updatedUser = new User(updateUserRequest.getUserName(), currentUser.getUserPassword(), userRoles, currentUser.getIsEnabled());
            return UpdateUserResponse.from(Optional.of(userRepository.save(updatedUser)));
        }
        return UpdateUserResponse.from(Optional.empty());
    }

    @Override
    public DisableUserRespose disableUser(String userName) {
        if (userRepository.existsUserByUserName(userName)) {
            User user = userRepository.findUserByUserName(userName);
            user.setIsEnabled(false);
            return DisableUserRespose.from(Optional.of(userRepository.save(user)));
        }
        return DisableUserRespose.from(Optional.empty());
    }

    @Override
    public EnableUserRespose enableUser(String userName) {
        if (userRepository.existsUserByUserName(userName)) {
            User user = userRepository.findUserByUserName(userName);
            user.setIsEnabled(true);
            return EnableUserRespose.from(Optional.of(userRepository.save(user)));
        }
        return EnableUserRespose.from(Optional.empty());
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findUserByUserNameAndIsEnabledTrue(userName)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("username {0} not found", userName)
                ));
    }
}
