package com.example.manageinventory.services;

import com.example.manageinventory.models.*;
import com.example.manageinventory.repositories.UserRepository;
import com.example.manageinventory.view_models.UserViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity getListOfUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(this.userRepository.findAll());
    }

    public ResponseEntity getUserById(int id) {
        User userObj = userRepository.findUserById(id);
        if (userObj == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User With Id: %d not found", id));
        }
        return ResponseEntity.status(HttpStatus.OK).body(userObj);
    }

    //Helper method to get user by email. Used by Authentication module
    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public ResponseEntity signUpNewUser(UserViewModel user) {
        User userObj = new User();
        BeanUtils.copyProperties(user, userObj, "id", "active");
        userObj.setActive(true);
        userRepository.saveAndFlush(userObj);
        return ResponseEntity.status(HttpStatus.CREATED).body(userObj);
    }

    public ResponseEntity updateUserDetails(int id, UserViewModel user) {
        User userObj = userRepository.findUserById(id);
        if (userObj == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User With Id: %d not found", id));
        }
        System.out.println(user.toString());
        BeanUtils.copyProperties(user, userObj, "id");
//        userObj.setActive(user.getActive());
        userObj = userRepository.saveAndFlush(userObj);

        return ResponseEntity.status(HttpStatus.OK).body(userObj);

    }

    public ResponseEntity updateUserMarkAsDelete(int id) {
        User userObj = userRepository.findUserById(id);
        if (userObj == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User With Id: %d not found", id));
        }
        userObj.setActive(false);
        userRepository.saveAndFlush(userObj);

        return ResponseEntity.status(HttpStatus.OK).body("User has been marked as Inactive");
    }

    public UserViewModel mapToUserView(User user){
        UserViewModel userViewModel = new UserViewModel();

        userViewModel.setId(user.getId());
        userViewModel.setActive(user.getActive());
        userViewModel.setContactNumber(user.getContactNumber());
        userViewModel.setDisplayName(user.getDisplayName());
        userViewModel.setEmail(user.getEmail());
        userViewModel.setUserCode(user.getUserCode());
        userViewModel.setType(user.getType());
        return userViewModel;
    }

    public ResponseEntity getUserConfigurations() {
        //Return a Hash with all the relevant Static/Config data related to users
        Map<String, Object> userConfig = new HashMap<>();

        userConfig.put("UserType", UserType.values());
        userConfig.put("VendorType", VendorType.values());
        return ResponseEntity.status(HttpStatus.OK).body(userConfig);
    }
}
