package com.example.manageinventory.authentication;


import com.example.manageinventory.repositories.UserRepository;
import com.example.manageinventory.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Service
public class AppUserDetailService implements UserDetailsService {
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        com.example.manageinventory.models.User user = userRepository.findUserByEmail(s);
        if(user == null){
            throw new UsernameNotFoundException("User not found with userid: " + s);
        }

        return new User(s, user.getPassword(),new ArrayList<>());
    }

}
