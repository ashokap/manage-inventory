package com.example.manageinventory.services;

import com.example.manageinventory.models.Product;
import com.example.manageinventory.models.ProductStatus;
import com.example.manageinventory.models.User;
import com.example.manageinventory.repositories.UserRepository;
import com.example.manageinventory.view_models.UserViewModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
}
