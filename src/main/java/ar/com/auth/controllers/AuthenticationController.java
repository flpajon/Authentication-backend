package ar.com.auth.controllers;

import ar.com.auth.dtos.requests.SigninRequest;
import ar.com.auth.dtos.requests.SignupRequest;
import ar.com.auth.model.User;
import ar.com.auth.services.UserService;
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
    private UserService userService;

    @PostMapping("signup")
    public ResponseEntity<?> signUp(@RequestBody SignupRequest signupRequest){
        try{
            return ResponseEntity.ok(userService.signupUser(signupRequest));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("signin")
    public ResponseEntity<?> signIn(@RequestBody SigninRequest signinRequest){
        try{
            return ResponseEntity.ok(userService.signinUser(signinRequest));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("refreshToken")
    public ResponseEntity<?> refreshToken(@AuthenticationPrincipal User user){
        try{
            return ResponseEntity.ok(userService.refreshToken(user));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
