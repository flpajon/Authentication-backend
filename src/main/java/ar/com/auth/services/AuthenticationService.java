package ar.com.auth.services;

import ar.com.auth.dtos.requests.SigninRequest;
import ar.com.auth.dtos.requests.SignupRequest;
import ar.com.auth.dtos.responses.RefreshTokenResponse;
import ar.com.auth.dtos.responses.SigninResponse;
import ar.com.auth.dtos.responses.SignupResponse;
import ar.com.auth.model.User;

public interface AuthenticationService {
    SignupResponse signupUser(SignupRequest signUpRequest);
    SigninResponse signinUser(SigninRequest signinRequest);
    RefreshTokenResponse refreshToken(User user);
}
