package com.example.manageinventory.controllers;

import com.example.manageinventory.authentication.AppUserDetailService;
import com.example.manageinventory.authentication.JwtUtils;
import com.example.manageinventory.authentication.TokenResponse;
import com.example.manageinventory.constants.APIConstants;
import com.example.manageinventory.services.UserService;
import com.example.manageinventory.view_models.LoginUserViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin

public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AppUserDetailService appUserDetailService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    /**
     *
     * @param user
     * @return
     * @throws Exception
     */
//    @PostMapping("/api/1/ims/login")
//    public ResponseEntity loginUser(@RequestBody LoginUserViewModel user) throws Exception {
//        try {
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
//            );
//
//        } catch (BadCredentialsException e) {
//            throw new Exception("Incorrect userid or password", e);
//        }
//        //  If we reached this far, we can ask the JwtUtils to provide us the token
//        final UserDetails userDetails = appUserDetailService
//                .loadUserByUsername(user.getUsername());
//
//        final String jwt = jwtUtils.generateToken(userDetails);
//
//        System.out.println("User token: "+jwt);
//
//        return ResponseEntity.ok(new TokenResponse(jwt));
//    }
}
