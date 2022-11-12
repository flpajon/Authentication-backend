package ar.com.auth.services;

import ar.com.auth.dtos.requests.SignUpRequest;
import ar.com.auth.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    public User fetchUserByUserUserName(String userName);
    public User signupUser(SignUpRequest signUpRequest);
}
