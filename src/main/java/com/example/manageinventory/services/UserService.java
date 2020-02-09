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
        return null;
    }

    public ResponseEntity signUpNewUser(UserViewModel user) {
        User userObj = new User();
        BeanUtils.copyProperties(user, userObj, "id", "isActive");
        userObj.setActive(true);
        userRepository.saveAndFlush(userObj);
        System.out.println("\n User Object Before Saving: "+userObj);
        return ResponseEntity.status(HttpStatus.CREATED).body(userObj);
    }

    public ResponseEntity updateUserDetails(int id, UserViewModel user) {
        return null;
    }

    public ResponseEntity updateUserMarkAsDelete(int id) {
        return null;
    }
}
