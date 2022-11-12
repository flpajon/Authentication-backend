package ar.com.auth.controllers;

import ar.com.auth.dtos.requests.SignUpRequest;
import ar.com.auth.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authentication")
public class AuthenticationController {

    private final static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private UserService userService;

    @PostMapping("signup")
    public ResponseEntity signUp(@RequestBody SignUpRequest signUpRequest){
        try{
            userService.signupUser(signUpRequest);
            return ResponseEntity.ok().build();
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
