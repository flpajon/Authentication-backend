package ar.com.auth.controllers;

import ar.com.auth.dtos.requests.SigninRequest;
import ar.com.auth.dtos.requests.SignupRequest;
import ar.com.auth.dtos.responses.RefreshTokenResponse;
import ar.com.auth.dtos.responses.SigninResponse;
import ar.com.auth.dtos.responses.SignupResponse;
import ar.com.auth.model.User;
import ar.com.auth.services.AuthenticationService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;
    @ApiOperation(value = "This method is used to signup a new user.")
    @PostMapping("signup")
    public ResponseEntity<SignupResponse> signUp(@RequestBody SignupRequest signupRequest){
        try{
            return ResponseEntity.ok(authenticationService.signupUser(signupRequest));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    @ApiOperation(value = "This method is used to signin a exist user.")
    @PostMapping("signin")
    public ResponseEntity<SigninResponse> signIn(@RequestBody SigninRequest signinRequest){
        try{
            return ResponseEntity.ok(authenticationService.signinUser(signinRequest));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
    @ApiOperation(value = "This method is used to refresh the access token.")
    @GetMapping("refreshToken")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@AuthenticationPrincipal User user){
        try{
            return ResponseEntity.ok(authenticationService.refreshToken(user));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
