package com.example.manageinventory.controllers;

import com.example.manageinventory.constants.APIConstants;
import com.example.manageinventory.models.User;
import com.example.manageinventory.repositories.UserRepository;
import com.example.manageinventory.services.UserService;
import com.example.manageinventory.services.UserService;
import com.example.manageinventory.view_models.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = APIConstants.User.USER_ROOT)
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * //Get list of users in the inventory
     * @return
     */
    @GetMapping
    public ResponseEntity listOfUsers() {
        try {
            System.out.println("ROOT: "+APIConstants.User.USER_ROOT);
            return this.userService.getListOfUsers();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * View User details
     * @param id
     * @return
     */

    //Get details of a user
    @GetMapping(path = APIConstants.User.USER_GET_UPDATE_DELETE)
    public ResponseEntity getUserById(
            @PathVariable int id) {
        try {
            return this.userService.getUserById(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * Create/register a new User. Manufacturer should be created before hand
     * @param user
     * @return
     */

    @PostMapping
    public ResponseEntity create(@RequestBody final UserViewModel user) {
        try {
            return this.userService.signUpNewUser(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @PutMapping(path = APIConstants.User.USER_GET_UPDATE_DELETE)
    public ResponseEntity updateUser(@RequestBody final UserViewModel user,
                                        @PathVariable int id) {
        try{
            return this.userService.updateUserDetails(id, user);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }


    }

    /**
     * API to soft delete User.
     * @param id
     * @return
     */
    @DeleteMapping(path = APIConstants.User.USER_GET_UPDATE_DELETE)
    public ResponseEntity deleteUser(@PathVariable int id) {
        try{
            return this.userService.updateUserMarkAsDelete(id);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
