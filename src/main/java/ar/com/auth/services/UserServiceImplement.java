package ar.com.auth.services;

import ar.com.auth.dtos.requests.SignUpRequest;
import ar.com.auth.enums.Roles;
import ar.com.auth.model.Role;
import ar.com.auth.model.User;
import ar.com.auth.repositories.RoleRepository;
import ar.com.auth.repositories.UserRepository;
import org.hibernate.FetchNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImplement implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User fetchUserByUserUserName(String userName) {
        return userRepository.findUserByUserName(userName)
                .orElseThrow(() -> new FetchNotFoundException(
                        "User",
                        MessageFormat.format("username {0} not found", userName)
                ));
    }

    @Override
    public User signupUser(SignUpRequest signUpRequest) {
        List<Role> userRoles = signUpRequest.getUserRoles().stream().map(role -> roleRepository.findRoleByRoleName(Roles.valueOf(role))
                        .orElseThrow(() -> new FetchNotFoundException(
                                "Role",
                                MessageFormat.format("role {0} not found", role)
                        ))).collect(Collectors.toList());
        User user = new User(signUpRequest.getUserName(), passwordEncoder.encode(signUpRequest.getUserPassword()), userRoles, true);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findUserByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException(
                        MessageFormat.format("username {0} not found", userName)
                ));
    }
}
