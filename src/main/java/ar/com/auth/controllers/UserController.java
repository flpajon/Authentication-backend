package ar.com.auth.controllers;

import ar.com.auth.dtos.requests.UpdateUserRequest;
import ar.com.auth.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("all")
    public ResponseEntity<?> fetchAllUsers(){
        try{
            return ResponseEntity.ok(userService.fetchAllUsers());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("update")
    public ResponseEntity<?> updateUser(@RequestBody UpdateUserRequest updateUserRequest){
        try{
            return ResponseEntity.ok(userService.updateUser(updateUserRequest));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
