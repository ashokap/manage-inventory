package com.example.manageinventory.controllers;

import com.example.manageinventory.authentication.AppUserDetailService;
import com.example.manageinventory.authentication.JwtUtils;
import com.example.manageinventory.authentication.TokenResponse;
import com.example.manageinventory.constants.APIConstants;
import com.example.manageinventory.services.UserService;
import com.example.manageinventory.view_models.LoginUserViewModel;
import com.example.manageinventory.view_models.UserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = APIConstants.User.USER_ROOT)
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AppUserDetailService appUserDetailService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    /**
     * //Get list of users in the inventory
     * @return
     */
    @GetMapping
    public ResponseEntity listOfUsers() {
        try {
            return this.userService.getListOfUsers();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     *
     * @param user
     * @return
     * @throws Exception
     */
    @PostMapping(path = APIConstants.User.USER_LOGIN)
    public ResponseEntity loginUser(@RequestBody LoginUserViewModel user) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );

        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect userid or password", e);
        }
        //  If we reached this far, we can ask the JwtUtils to provide us the token
        final UserDetails userDetails = appUserDetailService
                .loadUserByUsername(user.getUsername());

        final String jwt = jwtUtils.generateToken(userDetails);

        System.out.println("User token: "+jwt);

        return ResponseEntity.ok(new TokenResponse(jwt));
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
