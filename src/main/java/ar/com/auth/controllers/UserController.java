package ar.com.auth.controllers;

import ar.com.auth.dtos.requests.UpdateUserRequest;
import ar.com.auth.dtos.responses.DisableUserRespose;
import ar.com.auth.dtos.responses.EnableUserRespose;
import ar.com.auth.dtos.responses.FetchAllUsersResponse;
import ar.com.auth.dtos.responses.UpdateUserResponse;
import ar.com.auth.services.UserService;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "This method is used to return a list of users.")
    @GetMapping("all")
    public ResponseEntity<FetchAllUsersResponse> fetchAllUsers(){
        try{
            return ResponseEntity.ok(userService.fetchAllUsers());
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiOperation(value = "This method is used to update a user.")
    @PutMapping("update")
    public ResponseEntity<UpdateUserResponse> updateUser(@RequestBody UpdateUserRequest updateUserRequest){
        try{
            return ResponseEntity.ok(userService.updateUser(updateUserRequest));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiOperation(value = "This method is used to disable a user.")
    @DeleteMapping("disable")
    public ResponseEntity<DisableUserRespose> disableUser(@RequestParam(name = "userName") String userName){
        try{
            return ResponseEntity.ok(userService.disableUser(userName));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @ApiOperation(value = "This method is used to enable a user.")
    @PutMapping("enable")
    public ResponseEntity<EnableUserRespose> enableUser(@RequestParam(name = "userName") String userName){
        try{
            return ResponseEntity.ok(userService.enableUser(userName));
        }catch (Exception e){
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
