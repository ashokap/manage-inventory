package com.example.manageinventory.authentication;

import com.example.manageinventory.models.User;
import com.example.manageinventory.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    UserRepository userRepository;
    public User getLoggedInUserObject(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = "";
        if (principal instanceof UserDetails) {
            email = ((UserDetails)principal).getUsername();
        } else {
            email = principal.toString();
        }
        User user = userRepository.findUserByEmail(email);
        if(user == null){
            throw new UsernameNotFoundException("User with email Id not present");
        }
        return user;
    }
}
